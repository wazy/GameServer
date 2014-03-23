package main;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
	// server console command
	public static void queryWhoIsOnline() throws SQLException {
		int counter = 0;
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		ResultSet rst = st.executeQuery("SELECT * FROM players WHERE Online = 1;");

		System.out.print("{ ");

		/* id, name, level, class, x-pos, y-pos, online */
		while (rst.next()) {
			System.out.print(rst.getString(2) + " ");
			counter++;
		}

		// no online players
		if (counter == 0) {
			System.out.print("NONE ");
		}

		System.out.print("}");

		// cleanup connection
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeResultSet(rst);
		DatabaseConnection.closeConnection(conn);
	}

	// init everyone to offline
	public static void turnAllOffline() throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();

		st.executeUpdate("UPDATE players SET Online = 0 WHERE 1");
		// cleanup
		DatabaseConnection.closeStatement(st);
		DatabaseConnection.closeConnection(conn);
	}

	public static String[] queryAuth(String username) {
		try {
			int ID = 0;
			String[] userInfo = new String[6];

			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM accounts WHERE Username = '" + username + "'");

			/* ID, Username, Password (Hashed) */
			if (rst.next()) {
				ID = rst.getInt("ID");
				userInfo[0] = String.valueOf(ID);
				userInfo[1] = rst.getString("Password");

				/* id, name, level, class, x-pos, y-pos, online */
				rst = st.executeQuery("SELECT * FROM players WHERE ID = " + ID);

				System.out.println(ID);
				
				// return online player
				if (rst.next()) {
					userInfo[2] = rst.getString(2);			     // name
					userInfo[3] = String.valueOf(rst.getInt(5)); // x-pos
					userInfo[4] = String.valueOf(rst.getInt(6)); // y-pos
				}
			}

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);

			return userInfo;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void turnOnline(int ID, Player player) {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();

			Player.onlinePlayers.add(player);
			System.out.println(player.getName() + " is now online!");
			st.executeUpdate("UPDATE gameDB.players SET ONLINE = 1 WHERE ID = " + ID);

			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void removeOnline(int playerID, int position)
			throws SQLException {
		// just going to remove a player from online status
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate("UPDATE players SET Online = 0 WHERE ID = " + playerID);

			if (Player.onlinePlayers.size() > position)
				Player.onlinePlayers.remove(position);

			// cleanup -- important
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeConnection(conn);
			System.out.println("\nPlayer " + playerID + " is now offline.");
		} 
		catch (Exception e) {
			System.out.println("Player " + playerID + " could not be set to offline.");
			e.printStackTrace();
		}
	}

	// update player's coordinates here
	public static void updateCoordinates(int id, int x, int y) throws SQLException {
		Connection conn = DatabaseConnection.getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE players SET `X-Pos` = " + x + ", " + "`Y-Pos` = " + y + " WHERE ID = " + id);

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
					+ "`Y-Pos`, `Online`) VALUES ('" + username + "', 1, 'NYI', 0, 0, 0);");
			
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

	/* add all the creature spawns to send to clients */
	public static void queryCreatures() {
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rst = st.executeQuery("SELECT * FROM gameDB.creature_spawns;");

			/* GUID, Name, X-Pos, Y-Pos, Faction */
			while (rst.next()) {
				Creature creature = new Creature(rst.getInt("GUID"), rst.getString("Name"), 
										rst.getInt("X-Pos"), rst.getInt("Y-Pos"), rst.getInt("Faction"));

				Creature.creatureList.add(creature);
			}

			// cleanup
			DatabaseConnection.closeStatement(st);
			DatabaseConnection.closeResultSet(rst);
			DatabaseConnection.closeConnection(conn);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}