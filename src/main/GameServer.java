package main;
import java.io.*;
import java.net.*;

// (char) 13 is used to end read writes
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

	ObjectInputStream inputStream;

	GameServer(Socket s, int i) throws IOException {
		this.connection = s;
		// inputStream = new ObjectInputStream(connection.getInputStream());
		// this.ID = i;
	}

	public void run() {
		try {
			int id;

			// init output and input streams
			BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.flush(); // don't forget to flush ;-)
			inputStream = new ObjectInputStream(connection.getInputStream());

			String process = (String) inputStream.readObject();

			// client wants to authenticate
			if (process.contentEquals("auth")) {
				System.out.println("\nAuthenticated client!");

				oos.writeInt(1);
				oos.flush();

				id = inputStream.readInt();
				// player id, name and x y coordinates (, delimited)
				// should be from database -- i.e. (1, testuser, 100, 200)
				System.out.println("Player Id = " + id);
				String playerInfo = DatabaseHandler.queryAuth(id);
				
				// player exists --> send info
				if (playerInfo != null) {
					setPlayerId(id);
					oos.writeObject(playerInfo);
					oos.flush();
					
					// send other online players to client (one shot)
					// routine update is handled by another (thread) connection
					SendPlayerCoordinates.sendOnlinePlayers(connection, oos);
				}
				// player doesn't exist in DB
				else {
					oos.writeObject("create");
					oos.flush();
				}
			}
			// client coordinate update thread connected
			else if (process.contentEquals("update")) {
				oos.writeInt(1);
				oos.flush();
				// send other players to client -- for great justice!
				AcceptPlayerCoordinates.acceptCoordinates(inputStream);
			}
			// NYI.. this is placeholder
			else if (process.contentEquals("monster")) {
				oos.writeInt(1);
				oos.flush();
				// send & receive updates for monsters here 
				CreatureHandler.updateClient(oos, inputStream);
			}
			// this shouldn't happen.. but if it does?
			else {
				System.out.println("Authentication failure!");
				System.out.println(process);
				oos.writeInt(0);
				oos.flush();
				oos.close();
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