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
			ResultSet rst = st.executeQuery("SELECT * FROM gameDB.accounts WHERE Username = '" + username + "'");

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

	public static boolean addAccount(String[] accountDetails) {
		try { // try to add an account here from username and password hash
			String username = accountDetails[0];
			String hashpw = accountDetails[1];

			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();

			/* ID, Username, Password (Hashed) */
			st.executeUpdate("INSERT INTO `accounts` (`Username`, `Password`) VALUES (" +
					"'" + username + "', '" + hashpw + "');");

			/* id, name, level, class, x-pos, y-pos, online */
			st.executeUpdate("INSERT INTO `players`" + "(`Name`, `Level`, `Class`, `X-Pos`," 
					+ "`Y-Pos`, `Online`) VALUES ('" + username + "', 1, 'Rog', 0, 0, 0);");

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);

			return true;
		}
		// this will throw duplicate errors because of Unique constraint
		// just silence the error and red text and tell client it can't continue
		catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}
}
