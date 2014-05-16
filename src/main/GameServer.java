package main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.DatabaseConnection;
import database.DatabaseHandler;

public class GameServer {

	private static ServerSocket socket = null;
	
	public static List<ClientConnection> clients =
								Collections.synchronizedList(new ArrayList<ClientConnection>(16));

	public static void main(String[] args) {
		int port = 8149;
		int count = 0;
		try {
			// init everything
			DatabaseConnection.initConnectionPool();
			socket = new ServerSocket(port);

			// all players should be offline at startup
			DatabaseHandler.turnAllOffline();

			// handling randomizing creature movements here
			Runnable creatureRunnable = new CreatureHandler();
			Thread creatureThread = new Thread(creatureRunnable);
			creatureThread.start();

			// start thread that reads console input here
			Runnable loggerRunnable = new ConsoleLogger();
			Thread loggerThread = new Thread(loggerRunnable);
			loggerThread.start();

			System.out.println("\nGameServer initialized.. have fun!");
			while (true) {
				Socket connection = socket.accept();
				Runnable runnable = new ClientConnection(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} 
		catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}