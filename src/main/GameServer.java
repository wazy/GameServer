package main;
import java.io.*;
import java.net.*;

public class GameServer implements Runnable {

	private Socket connection;
	// private int ID;
	private static int playerId;

	public static void main(String[] args) {
		int port = 8149;
		int count = 0;
		try {
			// init everything
			DatabaseConnection.initConnectionPool();
			ServerSocket socket = new ServerSocket(port);

			// everyone should be offline at startup
			DatabaseHandler.turnAllOffline();

			// start thread that reads console input here
			Runnable loggerRunnable = new ConsoleLogger();
			Thread loggerThread = new Thread(loggerRunnable);
			loggerThread.start();
			
			System.out.println("GameServer initialized.. have fun!");
			while (true) {
				Socket connection = socket.accept();
				Runnable runnable = new GameServer(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	GameServer(Socket s, int i) throws IOException {
		this.connection = s;
		// inputStream = new ObjectInputStream(connection.getInputStream());
		// this.ID = i;
	}

	public void run() {
		try {
			// init output and input streams
			ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			outputStream.flush(); // don't forget to flush ;-)
			ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());

			String process = (String) inputStream.readObject();

			// client wants to authenticate, auth packet as follows
			// CMSG "auth", SMSG 1, CMSG username,
			// SMSG 1 or 0 (exists or doesn't), CMSG 1 (pong if user exists),
			// SMSG passwordHash, CMSG 1 or 0 (valid or not)
			// --> then server d/c's the client		
			if (process.contentEquals("auth")) {

				outputStream.writeInt(1);
				outputStream.flush();

				String username = (String) inputStream.readObject();

				String[] userInfoArr = DatabaseHandler.queryAuth(username);
				if (userInfoArr[1] != null) { // password not null (means exists)
					outputStream.writeInt(1); // ping
					outputStream.flush();
					inputStream.readInt(); // pong
					outputStream.writeObject(userInfoArr[1]); // write passwordHash
					outputStream.flush();
					
					int valid = inputStream.readInt(); // client matched password
					
					if (valid == 1) { // let's add to list and turn online
						Player player = new Player(Integer.parseInt(userInfoArr[0]), userInfoArr[2], 
													Integer.parseInt(userInfoArr[3]), Integer.parseInt(userInfoArr[4]));
						
						/* Player =  ID, name, x, y */
						DatabaseHandler.turnOnline(Integer.parseInt(userInfoArr[0]), player); // ID, Player object

						System.out.println("\nAuthenticated client!");
					}
					
					inputStream.close();
					outputStream.close();
				}
				else { // user does not exist
					outputStream.writeInt(0);
					outputStream.flush();

					inputStream.close();
					outputStream.close();
					
					System.out.println("\nRejected non-existing user who attempted to auth!");
				}
			}

			// registering a client account here
			// client wants to register, reg packet as follows
			// CMSG "register", SMSG 1, CMSG regDetails,
			// SMSG 1 or 0 (valid or not) --> then server d/c's the client
			else if (process.contentEquals("register")) {
				outputStream.writeInt(1);
				outputStream.flush();

				String regDetails = (String) inputStream.readObject();
				String[] accountDetails = regDetails.split(" ");
				
				boolean success = DatabaseHandler.addAccount(accountDetails);
				
				if (success) { // account created
					outputStream.writeInt(1);
					outputStream.flush();
					
					inputStream.close();
					outputStream.close();

					System.out.println("\nRegistered account name: " + accountDetails[0]);
				}
				else { // account not created
					outputStream.writeInt(0);
					outputStream.flush();

					inputStream.close();
					outputStream.close();
					
					System.out.println("\nRejected account name: " + accountDetails[0]);
				}
			}
			
			// to send player coordinates
			else if (process.contentEquals("spc")) {
				outputStream.writeInt(1);
				outputStream.flush();	
				// send other online players to client
				SendPlayerCoordinates.sendOnlinePlayers(outputStream);
			}
			// client coordinate update thread connected
			else if (process.contentEquals("update")) {
				outputStream.writeInt(1);
				outputStream.flush();
				// send other players to client -- for great justice!
				AcceptPlayerCoordinates.acceptCoordinates(inputStream);
			}
			// NYI.. this is placeholder
			else if (process.contentEquals("monster")) {
				outputStream.writeInt(1);
				outputStream.flush();
				// send & receive updates for monsters here 
				CreatureHandler.updateClient(outputStream, inputStream);
			}
			// this shouldn't happen.. but if it does?
			else {
				System.out.println("Authentication failure!");
				System.out.println(process);
				outputStream.writeInt(0);
				outputStream.flush();
				outputStream.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static int getPlayerId() {
		return GameServer.playerId;
	}

	public static void setPlayerId(int playerId) {
		GameServer.playerId = playerId;
	}
}