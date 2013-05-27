import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DatabaseConnection {
	
	private static BoneCP connectionPool = null;
	
	public static void initConnectionPool() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.txt"));
			Class.forName("com.mysql.jdbc.Driver"); //also you need the MySQL driver
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(prop.getProperty("database"));
			config.setUsername(prop.getProperty("dbuser"));
			config.setPassword(prop.getProperty("dbpassword"));
			config.setMinConnectionsPerPartition(3);   
			config.setMaxConnectionsPerPartition(5);
			config.setPartitionCount(1); // 1*2 = 2 connections
			//config.setLazyInit(true); // depends on the application usage
			connectionPool = new BoneCP(config); // setup the connection pool
			System.out.println("Connection pool running..");
			System.out.println("This many active physical connections: " + connectionPool.getTotalCreatedConnections());
			DatabaseConnection.setConnectionPool(connectionPool);
		} 
		catch (Exception e) {
			e.printStackTrace(); // Fix this.. exception wrapping.
		}
	}

	// call at end of program to close all physical threads
	public static void shutdownConnectionPool() {
		try {
			BoneCP connectionPool = DatabaseConnection.getConnectionPool();
			System.out.println("Shutting down connection pool.");
			if (connectionPool != null) {
				connectionPool.shutdown();
				System.out.println("Connection pooling is destroyed successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// use to get a logical connection of a free physical connection
	public static Connection getConnection() {
		Connection conn = null;
		try {
			// thread-safe
			conn = getConnectionPool().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// simple close statement.. important dont forget to do this
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// dont forget this either
	public static void closeResultSet(ResultSet rSet) {
		try {
			if (rSet != null)
				rSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DO NOT FORGET TO CLOSE LOGICAL CONNECTIONS
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close(); //release the connection
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static BoneCP getConnectionPool() {
		return connectionPool;
	}

	public static void setConnectionPool(BoneCP connectionPool) {
		DatabaseConnection.connectionPool = connectionPool;
	}
}