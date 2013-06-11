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
	
    ObjectInputStream inputStream;

	GameServer(Socket s, int i) throws IOException {
		this.connection = s;
        //inputStream = new ObjectInputStream(connection.getInputStream());
		//this.ID = i;
	}
	public void run() {
		try {
			int id;
			
			// init output and input streams
			BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.flush();
		    inputStream = new ObjectInputStream(connection.getInputStream());

		    String process = (String) inputStream.readObject();

		    // client wants to authenticate
		    if (process.contentEquals("auth")) {
		    	System.out.println("\nauthenticated client");

		    	oos.writeInt(1);
		    	oos.flush();

		    	id = inputStream.readInt();
		    	// player id, name and x y coordinates (, delimited)
		    	// should be from database -- i.e. (1, testuser, 100, 200)
		    	System.out.println("Player Id = " + id);
		    	String playerInfo  = DatabaseHandler.queryAuth(id);
		    	// player doesn't exist in DB?
		    	if (playerInfo != null) {
		    		setPlayerId(id);
		    		oos.writeObject(playerInfo);
		    		oos.flush();
		    		// send other players to client
		    		UpdateClient.sendOnlinePlayers(connection, oos);
		    	}
		    	else {
		    		oos.writeObject("create");
		    		oos.flush();
		    	}
		    }
		    // client coordinate update thread connected
		    else if (process.contentEquals("update")) {
		    	System.out.println("\nauthenticated client");
		    	oos.writeInt(1);
		    	oos.flush();
		    	// send other players to client -- should be own thread
		    	UpdateCoordinates.acceptCoordinates(inputStream);
		    }
		    else {
		    	System.out.println("Authentication failure");
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
				// everyone should be offline at shutdown
				// DatabaseHandler.turnAllOffline();
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