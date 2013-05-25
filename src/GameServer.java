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
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.flush();
				BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
				ObjectInputStream ois = new ObjectInputStream(bis);
				int character;
				StringBuffer process = new StringBuffer();
				int id;
				
				while((character = ois.readChar()) != 13) {
					process.append((char)character);
				}
				// client wants to authenticate
				if (process.toString().contentEquals("auth")) {
					System.out.println("\nauthenticated client");
					oos.writeInt(1);
					oos.flush();

					id = ois.readInt();
					// player id, name and x y coordinates (, delimited)
					// should be from database -- i.e. (1, testuser, 100, 200)
					System.out.println("Player Id = " + id);
					String playerInfo  = DatabaseHandler.queryAuth(id);
					// player doesn't exist in DB?
					if (playerInfo != null) {
						setPlayerId(id);
						oos.writeChars(playerInfo + (char) 13);
						oos.flush();
						// send other players to client
						// UpdateClient.sendOnlinePlayers(oos);
					}
					else {
						oos.writeChars("create" + (char) 13);
						oos.flush();
					}
					// oos.close();
					// ois.close();
					break;
				}
				// client coordinate update thread connected
				else if (process.toString().contentEquals("update")) {
					System.out.println("\nauthenticated client");
					oos.writeInt(1);
					oos.flush();
					// send other players to client
					// UpdateCoordinates.acceptCoordinates(ois);
				}
				else {
					System.out.println("Authentication failure");
					System.out.println(process.toString());
					oos.writeInt(0);
					oos.flush();
					oos.close();
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