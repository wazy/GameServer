package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/* handle console commands here */
public class ConsoleLogger implements Runnable {
	public void run() {
		System.out.println("\nType commands below after >>> indicators.");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (true) {
				System.out.print(">>> ");
				// not ready to read anything yet
				while (!br.ready()) {
					Thread.sleep(200);
				}
				String command;
				command = br.readLine();

				if (command.equals("commands") || command.equals("help")) {
					System.out.println("Available commands are: {'online', 'shutdown', 'help', 'commands'}.");
				}
				else if (command.equals("shutdown")) {
					System.out.println("\n!!!Console shutdown imminent!!!");
					System.exit(0);
				}
				else if (command.equals("online")) {
					System.out.print("These players are online: ");
					DatabaseHandler.queryWhoIsOnline();
					System.out.print("\n");
				}
				else if (command.isEmpty()) {
					continue;
				}
				else {
					System.out.println("Unrecognized command. Try typing 'help'.");
				}
			}
		}
		catch (Exception e) {};
	}
}