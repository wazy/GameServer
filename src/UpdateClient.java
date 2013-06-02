import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class UpdateClient {

	public static void sendOnlinePlayers(Socket socket, ObjectOutputStream outputStream) throws InterruptedException, SQLException {
		int position = 0;
		int id = GameServer.getPlayerId();
		try {
			while (true) {
				position = Player.getPosition(id);
				// player has to actually be in the ArrayList
				if (position < 0) {
					System.exit(1); // should never get here
				}
			/*	
			    System.out.println("Player ID: " + id + " is at position: " + position);
				System.out.println(Player.onlinePlayers.get(position).x);
				System.out.println(Player.onlinePlayers.get(position).y);
			*/	
				// write player position in list to client
				outputStream.writeInt(position);
				outputStream.flush();
				
				// reset so we don't write cached players
				outputStream.reset();
				outputStream.writeObject(Player.onlinePlayers);
				outputStream.flush();
				
				// pause between updates
				Thread.sleep(1000);
			}
		}
		catch (SocketException e) {
			try {
				// throws socket exception indicating closed/lost connection from client
				System.out.println("\nPlayer " + id + " has disconnected.");
			} 
			catch (Exception e1) {	
			}
		}
		catch (IOException ioe) {
		}
		finally {
			DatabaseHandler.removeOnline(id, position);
		}
	}
}
