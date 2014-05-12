package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.sql.SQLException;

public class SendPlayerCoordinates {
	public static void sendOnlinePlayers(ObjectInputStream inputStream, ObjectOutputStream outputStream,
												int playerID) throws InterruptedException, SQLException {

		System.out.println("Sending player coordinates...");

		int clientPositionInList = -1;

		try {

			outputStream.writeObject(Player.onlinePlayers);
			outputStream.flush();

			// pause between updates
			Thread.sleep(2000);
			
			while (true) {

				int n = Player.onlinePlayers.size();
				outputStream.write(n);

				for (int i = 0; i < n; i++) {
					Player player = Player.onlinePlayers.get(i);
					if (playerID != player.getID()) {
						// ID: 1 X: 600 Y: 005
						// Packet -> 001-600-005
						int packet = Player.formatPlayerUpdatePacket(
												player.getID(), player.getX(), player.getY());
						outputStream.writeInt(packet);
						System.out.println("SERVER PUP: " + packet);
					}
					else {
						System.out.println("Sending position packet to " + i + " out of size " + n);
						clientPositionInList = i;
						outputStream.writeInt(clientPositionInList);
					}
					outputStream.flush();
				}

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
