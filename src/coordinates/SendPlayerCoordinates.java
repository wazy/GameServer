package coordinates;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.sql.SQLException;

import database.DatabaseHandler;

import entities.Player;

public class SendPlayerCoordinates {
	public static void sendOnlinePlayers(ObjectInputStream inputStream, ObjectOutputStream outputStream,
												int playerID) throws InterruptedException, SQLException {

		System.out.println("Sending player coordinates...");

		int clientPositionInList = -1;

		try {

			outputStream.writeObject(Player.onlinePlayers);
			outputStream.flush();
			
			while (true) {

				int n = Player.onlinePlayers.size();
				outputStream.write(n);
				outputStream.flush();

				// have to specify a 0 to update other players for client
				for (int i = 0; i < n; i++) {
					Player player = Player.onlinePlayers.get(i);
					if (playerID != player.getID()) {
						System.out.println(player.getX() + ", "+ player.getY());
						outputStream.write(0);
						outputStream.writeObject(player);
						outputStream.reset();
					}
					else {
						System.out.println("Sending position packet to " + i + " out of size " + n);
						outputStream.write(1);
						clientPositionInList = i;
						outputStream.write(clientPositionInList);
					}
					outputStream.flush();
				}
				
				// pause between updates
				Thread.sleep(2000);

				// reset so we don't write cached players
				//outputStream.reset();
			}
		} 

		catch (SocketException e) { }
		catch (IOException ioe) { }

		finally {
			if (clientPositionInList >= 0 && clientPositionInList < Player.onlinePlayers.size())
				DatabaseHandler.removeOnline(playerID, clientPositionInList);
		}
	}
}
