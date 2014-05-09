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

		int position = -1;

		try {

			int[] placeHolder = new int[3];
			outputStream.writeObject(Player.onlinePlayers);
			outputStream.flush();

			while (true) {

				int n = Player.onlinePlayers.size();

				position = inputStream.read();
				outputStream.write(n);

				// reset so we don't write cached players
				outputStream.reset();
				for (int i = 0; i < n; i++) {
					if (i != position) {
						Player play = Player.onlinePlayers.get(i);
						placeHolder[0] = play.getID();
						placeHolder[1] = play.getX();
						placeHolder[2] = play.getY();
						outputStream.writeObject(placeHolder);
					}
				}
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
			if (position >= 0)
				DatabaseHandler.removeOnline(playerID, position);
		}
	}
}
