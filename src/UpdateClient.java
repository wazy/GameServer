import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class UpdateClient {

	public static void sendOnlinePlayers(Socket connection, BufferedOutputStream bos) throws InterruptedException, SQLException {
		int id = GameServer.getPlayerId();
		int position = Player.onlinePlayers.size() - 1;
		System.out.println(position);
		try {
			//int playerIdTestIterator = 1;
			ObjectOutputStream outputStream = new ObjectOutputStream(bos);
			// just some example code to fake multiple players
			while (true) {
				// 3 second pause
				Thread.sleep(3000);

				/* if (playerIdTestIterator > 6) {
					playerIdTestIterator = 1;
				} */
				// fake online connections
				//DatabaseHandler.queryAuth(String.valueOf(playerIdTestIterator));
				outputStream.reset();
				outputStream.writeObject(Player.onlinePlayers);
				outputStream.flush();
				//playerIdTestIterator++;
			}
		}
		catch (SocketException e) {
			try {
				// hack .. fix this if possible 
				// throws socket exception indicating closed/lost connection from client
				System.out.println("\nPlayer " + id + " has disconnected.");
				DatabaseHandler.removeOnline(id, position);
				connection.shutdownOutput();
				connection.close();
			} catch (IOException e1) {
			}
			
		}
		catch (IOException ioe) {
		}
	}
}
