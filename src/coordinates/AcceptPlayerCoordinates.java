package coordinates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.sql.SQLException;

import database.DatabaseHandler;

import entities.Player;

public class AcceptPlayerCoordinates {
	//public static volatile boolean Running = false;
	
	public static void acceptCoordinates(ObjectInputStream inputStream, int playerID) 
											throws SQLException, ClassNotFoundException, InterruptedException {

		System.out.println("Accepting player coordinates..");

		try {
			int counter = 0;
			while (true) {

				int position = inputStream.read();
				int playerX = inputStream.read();
				int playerY = inputStream.read();

				//System.out.println(position + ", " + playerX + ", " + playerY);

				// client has disconnected
				if (playerX < 0 && playerY < 0 && position < 0)
					return;

				if (position < 0)
					continue;

				synchronized (Player.onlinePlayers) {
					// DB transaction and list operation to update player
					if (Player.onlinePlayers.size() > position) {
						Player.onlinePlayers.get(position).setXY(playerX, playerY);
						if (counter >= 100) { // DB transaction is more costly -- do it infrequently
							DatabaseHandler.updateCoordinates(playerID, playerX, playerY);
							counter = 0;
						}
					}
					else if (Player.onlinePlayers.get(position-1).getID() == playerID) {
						Player.onlinePlayers.get(position-1).setXY(playerX, playerY);
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
				Thread.sleep(200);
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
