import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;

public class UpdateCoordinates {
	public static void acceptCoordinates(ObjectInputStream inputStream) throws IOException, SQLException, ClassNotFoundException {
		System.out.println("Accepting connections");
		// not actually player's name but infact the listPosition is passed as second parameter
		while (true) {
			Player player = (Player) inputStream.readObject();
			// DB transaction to update player
			DatabaseHandler.updateCoordinates(player.id, Integer.parseInt(player.name), player.x, player.y);
			
		}
	}
}
