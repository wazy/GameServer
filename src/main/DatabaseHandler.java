package main;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
	// will be a server console command
	public static void queryOnline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		ResultSet rst = st.executeQuery("Select * from players where online = 1;");

		/* id, name, level, class, x-pos, y-pos, online */
		while (rst.next()) {
			System.out.println(rst.getString(1));
		}

		// cleanup connection
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeResultSet(rst);
		DatabaseConnection.closeConnection(conn);
	}

	// init everyone to offline
	public static void turnAllOffline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		st.executeUpdate("UPDATE gameDB.players SET ONLINE = 0 WHERE 1");
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}

	public static String queryAuth(String username) {
		try {
			String passwordHash = null;
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM gameDB.account WHERE Username = '" + username + "'");
			
			/* ID, Username, Password (Hashed) */
			if (rst.next()) {
				int ID = rst.getInt("ID");
				passwordHash = rst.getString("Password");
				
				/* id, name, level, class, x-pos, y-pos, online */
				rst = st.executeQuery("SELECT * FROM gameDB.players WHERE Id = " + ID);
				if (rst.next()) {
					// return online player
					Player.onlinePlayers.add(new Player(rst.getInt(1), rst.getString(2), rst.getInt(5), rst.getInt(6)));
					System.out.println(rst.getString(2) + " is now online!");
					// player = rst.getInt(1) + ", " + rst.getString(2) + ", "+ rst.getInt(5) + ", " + rst.getInt(6);
				}
				st.executeUpdate("UPDATE gameDB.players SET ONLINE = 1 WHERE Id = " + ID);
				GameServer.setPlayerId(ID);	// needed for other threads.. not safe nor good implementation TODO: fixme
			}
			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);

			return passwordHash;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void removeOnline(int playerId, int position)
			throws SQLException {
		// just going to remove a player from online status
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate("UPDATE gameDB.players SET Online = 0 WHERE Id = " + playerId);
			Player.onlinePlayers.remove(position);
			// cleanup -- important
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);
			System.out.println("\nPlayer " + playerId + " is now offline.");
		} 
		catch (Exception e) {
			System.out.println("Player " + playerId + "could not be set to offline.");
			e.printStackTrace();
		}
	}

	// update player's coordinates here
	public static void updateCoordinates(int id, int x, int y) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE gameDB.players SET `X-Pos` = " + x + ", " + "`Y-Pos` = " + y + " WHERE players.Id = " + id);
		
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}
}
