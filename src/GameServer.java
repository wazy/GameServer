import java.io.*;
import java.net.*;

// (char) 13 is used to end read writes
public class GameServer implements Runnable {

	private Socket connection;
	//private int ID;
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
			
			System.out.println("GameServer initialized");
			while (true) {
				Socket connection = socket.accept();
				Runnable runnable = new GameServer(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
				System.out.println("Connection number: " + count);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	GameServer(Socket s, int i) {
		this.connection = s;
		//this.ID = i;
	}
	public void run() {
		try {
			while (true) {
				// input reader / output writer init
				BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
				OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
				BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
				InputStreamReader isr = new InputStreamReader(bis);
				int character;
				StringBuffer process = new StringBuffer();
				StringBuffer id = new StringBuffer();
				
				while((character = isr.read()) != 13) {
					process.append((char)character);
				}
				// client wants to authenticate
				if (process.toString().contentEquals("auth")) {
					System.out.println("\nauthenticated client");
					osw.write("1" + (char) 13);
					osw.flush();
					
					while((character = isr.read()) != 13) {
						id.append((char) character);
					}
					// player id, name and x y coordinates (, delimited)
					// should be from database -- i.e. (1, testuser, 100, 200)
					System.out.println("Player Id = " + id.toString());
					String playerInfo  = DatabaseHandler.queryAuth(id.toString());
					// player doesn't exist in DB?
					if (playerInfo != null) {
						setPlayerId(Integer.parseInt(id.toString()));
						osw.write(playerInfo + (char) 13);
						osw.flush();
						// send other players to client
						UpdateClient.sendOnlinePlayers(connection, bos);
					}
					else {
						osw.write("create");
						osw.flush();
					}
					osw.close();
					break;
				}
				// client coordinate update thread connected
				else if (process.toString().contentEquals("update")) {
					System.out.println("\nauthenticated client");
					osw.write("1" + (char) 13);
					osw.flush();
					// send other players to client
					UpdateCoordinates.acceptCoordinates(connection, bis);
				}
				else {
					System.out.println("Authentication failure");
					System.out.println(process.toString());
					osw.write("0"+ (char) 13);
					osw.flush();
					osw.close();
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
				// everyone should be offline at shutdown
				DatabaseHandler.turnAllOffline();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static int getPlayerId() {
		return playerId;
	}
	public static void setPlayerId(int playerId) {
		GameServer.playerId = playerId;
	}
}