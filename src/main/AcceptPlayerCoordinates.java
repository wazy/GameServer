package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.sql.SQLException;

public class AcceptPlayerCoordinates {
	//public static volatile boolean Running = false;
	
	public static void acceptCoordinates(ObjectInputStream inputStream, int playerID) throws SQLException, ClassNotFoundException {

		System.out.println("Accepting player coordinates..");

		try {
			int counter = 0;
			while (true) {

				int position = inputStream.read();
				int playerX = inputStream.read();
				int playerY = inputStream.read();

				synchronized (Player.onlinePlayers) {
					// DB transaction and list operation to update player
					if (Player.onlinePlayers.size() > position) {
						Player.onlinePlayers.get(position).update(playerX, playerY);
						if (counter >= 100) { // DB transaction is more costly -- do it infrequently
							DatabaseHandler.updateCoordinates(playerID, playerX, playerY);
							counter = 0;
						}
					}
					else if (Player.onlinePlayers.get(position-1).getID() == playerID) {
						Player.onlinePlayers.get(position-1).update(playerX, playerY);
						if (counter >= 100) {
							DatabaseHandler.updateCoordinates(playerID, playerX, playerY);
							counter = 0;
						}
					}
					else {
						System.out.println(Player.onlinePlayers.get(position-1).getID() + " " + playerID);
						System.out.println("OUT OF BOUNDS! Position: " + position + " List size: " + Player.onlinePlayers.size());
						return; // prevent out of bounds (player index not in list or moved)
					}
					counter++;
				}
			}
		}
		catch (SocketException e) { // read failed because client closed connection
			return;
		}
		catch (IOException e) { // read failed because client closed connection
			return;
		}
	}
}
