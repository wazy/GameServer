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
				// System.out.println(scanner.next());
				while (!br.ready()) {
					//System.out.println("blocked.");
				}
				String command;
				command = br.readLine();

				if (command.equals("commands") || command.equals("help")) {
					System.out.println("This command really isn't implemented but you can type in 'shutdown'.");
				}
				else if (command.equals("shutdown")) {
					System.out.println("Console shutdown imminent!!!");
					System.exit(0);
				}
				else if (command.equals("online")) {
					System.out.print("These players are online: ");
					DatabaseHandler.queryOnline();
					System.out.print("\n");
				}
				else {
					continue;
				}
			}
		}
		catch (Exception e) {};
	}
}