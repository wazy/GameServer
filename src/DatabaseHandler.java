import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
	// TODO Probably remove this.. ingenious method of adding when they actually log in
	// that prevents unnecessary overhead in queryAuth()
	public static void queryOnline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
			
		ResultSet rst = st.executeQuery("Select * from players where online = 1;");

		/* id, name, level, class, x-pos, y-pos, online */
		while(rst.next()) {
			// add online players
			Player.onlinePlayers.add(new Player(rst.getInt(1), rst.getString(2), rst.getInt(5), rst.getInt(6))); 
			System.out.println("Online Players: " + rst.getString(2));
		}
		
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeResultSet(rst);
		DatabaseConnection.closeConnection(conn);
	}

	// init everyone to offline
	public static void turnAllOffline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		st.executeUpdate("UPDATE gameDB.players SET ONLINE = 0 WHERE 1=1");
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}
	
	public static String queryAuth(String id) {
		try {
			String player = null;
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM gameDB.players WHERE Id = " + id);	
			/* id, name, level, class, x-pos, y-pos, online */
			while(rst.next()) {
				// return online player
				Player.onlinePlayers.add(new Player(rst.getInt(1), rst.getString(2), rst.getInt(5), rst.getInt(6))); 
				System.out.println(rst.getString(2) + " is now online!");
				player = rst.getInt(1) + ", " + rst.getString(2) + ", " + rst.getInt(5) + ", " + rst.getInt(6);
			}
			st.executeUpdate("UPDATE gameDB.players SET ONLINE = 1 WHERE Id = " + id);
			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);

			return player;
		}
		catch (Exception e) { 
			e.printStackTrace(); 
			return null;
		}
	}

	public static void removeOnline(int playerId, int position) throws SQLException {
		// just going to remove a player from online status
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE gameDB.players SET ONLINE = 0 WHERE Id = " + playerId);
		
		// if (Player.onlinePlayers.contains(new Player(playerId,"Player2",50,25)));
		Player.onlinePlayers.remove(position);
		// cleanup -- important
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}
	
	// update player's coordinates here
	public static void updateCoordinates(int id, int listPosition, int x, int y) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE gameDB.players SET X-Pos = " + x + "Y-Pos = " + y + " WHERE Id = " + id);
		// update in list.. TODO put a lock on the arraylist when threads concurrently use
		Player.onlinePlayers.get(listPosition).x = x;
		Player.onlinePlayers.get(listPosition).x = y;
	}
}
