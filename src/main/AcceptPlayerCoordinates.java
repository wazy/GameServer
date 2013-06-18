package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.sql.SQLException;

public class AcceptPlayerCoordinates {
	public static void acceptCoordinates(ObjectInputStream inputStream) throws SQLException, ClassNotFoundException {
		System.out.println("Accepting player coordinates..");
		try {
			int counter = 0;
			// not actually player's name but infact the listPosition is passed
			// as second parameter (hack of course)
			while (true) {
				Player player = (Player) inputStream.readObject();
				int position = Integer.parseInt(player.name);
				// System.out.println("Received updates from client..");

				// DB transaction to update player
				if (Player.onlinePlayers.size() > position) {
					Player.onlinePlayers.get(position).update(player.x, player.y);
					if (counter >= 100) { // DB transaction is more costly -- do it infrequently
						DatabaseHandler.updateCoordinates(player.id, player.x, player.y);
						counter = 0;
					}
					// System.out.println("Processed coordinates = " + player.x + "  " + player.y);
					counter++;
				} else {
					return; // prevent out of bounds (player index not in list)
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
