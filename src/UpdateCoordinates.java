import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.SQLException;

public class UpdateCoordinates {
	public static void acceptCoordinates(Socket connection, BufferedInputStream bis) throws IOException, SQLException, ClassNotFoundException {
		System.out.println("Accepting connections");
		ObjectInputStream inputStream = new ObjectInputStream(bis);
		// not actually player's name but infact the listPosition is passed as second parameter
		while (true) {
			Player player = (Player) inputStream.readObject();
			// DB transaction to update player
			DatabaseHandler.updateCoordinates(player.id, Integer.parseInt(player.name), player.x, player.y);
			
		}
	}
}
