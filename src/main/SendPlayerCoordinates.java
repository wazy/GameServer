package main;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.sql.SQLException;

public class SendPlayerCoordinates {
	public static void sendOnlinePlayers(ObjectOutputStream outputStream) throws InterruptedException, SQLException {
		int id = GameServer.getPlayerId(), position = 0;
		System.out.println("Sending player coordinates..");
		try {
			while (true) {
				position = Player.getPosition(id);
				// player has to actually be in the ArrayList
				if (position < 0) {
					System.exit(1); // should never get here
				}
				/* DEBUG INFO:
				 * System.out.println("Player ID: " + id + " is at position: " +
				 * position);
				 * System.out.println(Player.onlinePlayers.get(position).x);
				 * System.out.println(Player.onlinePlayers.get(position).y);
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
		} 
		catch (IOException ioe) {
		} 
		finally {
			DatabaseHandler.removeOnline(id, position);
		}
	}
}
