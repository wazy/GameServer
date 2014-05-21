package coordinates;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import main.ClientConnection;
import database.DatabaseHandler;
import entities.Player;

public class SendPlayerCoordinates {
	public static void sendOnlinePlayers(ObjectInputStream inputStream, ObjectOutputStream outputStream,
												int playerID) throws InterruptedException, SQLException {

		System.out.println("Sending player coordinates...");

		int clientPositionInList = -1;
		boolean alreadyDisconnected = false;

		try {

			outputStream.writeObject(Player.onlinePlayers);
			outputStream.flush();

			while (true) {

				int n = Player.onlinePlayers.size();
				outputStream.write(n);
				outputStream.flush();

				// TODO: rewrite this and make it nicer for telling players who disconnected
				if (!alreadyDisconnected && ClientConnection.isOtherClientDisconnected()) {
					System.out.println("Another player is updating their list to remove this player...");
					if (ClientConnection.getPlayerAcknowledgement().get() <= 0)
						ClientConnection.setOtherClientDisconnected(false);

					/* other client disconnected */
					outputStream.write(1);

					outputStream.write(ClientConnection.getDisconnectedPosition());
					outputStream.flush();

					ClientConnection.getPlayerAcknowledgement().decrementAndGet();
					alreadyDisconnected = true;
				}
				else {
					/* normal transmission */
					outputStream.write(0);
					// have to specify a type -> 0 to update other players for client
					// 						  -> 1 to update player's position in list
					for (int i = 0; i < n; i++) {
						Player player = Player.onlinePlayers.get(i);
						if (playerID != player.getID()) {
							//System.out.println("SPC: " + player.getX() + ", "+ player.getY());
							outputStream.write(0);
							outputStream.writeObject(player);
	
							// reset so we don't write cached players
							outputStream.reset();
						}
						else {
							//System.out.println("Sending position packet to " + i + " out of size " + n);
							outputStream.write(1);
							clientPositionInList = i;
							outputStream.write(clientPositionInList);
						}
						outputStream.flush();
						alreadyDisconnected = false;
					}
				}
				// pause between updates
				Thread.sleep(200);
			}
		} 

		catch (Exception e) {}

		finally {
			if (clientPositionInList >= 0 && clientPositionInList < Player.onlinePlayers.size()) {
				DatabaseHandler.removeOnline(playerID, clientPositionInList);

				// if other players are connected then inform them of a disconnect
				if (Player.onlinePlayers.size() > 0) {
					ClientConnection.getPlayerAcknowledgement().set(Player.onlinePlayers.size()-1);
					ClientConnection.setDisconnectedPosition(clientPositionInList);
					ClientConnection.setOtherClientDisconnected(true);
				}
			}
		}
	}
}
