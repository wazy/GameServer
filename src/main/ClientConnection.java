package main;

import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import entities.Player;

public class ClientConnection implements Runnable {
	
	private int clientID;
	private int playerID;

	private Socket connection;
	
	ClientConnection(Socket client, int ID) {
		this.connection = client;
		this.clientID = ID;
	}

	public void run() {
		try {
			// init output and input streams
			ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			outputStream.flush(); // don't forget to flush :)
			ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());

			String process = (String) inputStream.readObject();

			// client wants to authenticate, auth packet as follows
			// CMSG "auth", SMSG 1, CMSG username,
			// SMSG 1 or 0 (exists or doesn't), CMSG 1 (pong if user exists),
			// SMSG passwordHash, CMSG 1 or 0 (valid or not)
			// --> then server d/c's the client		
			if (process.contentEquals("auth")) {

				outputStream.writeInt(1);
				outputStream.flush();

				String username = (String) inputStream.readObject();

				String[] userInfoArr = DatabaseHandler.queryAuth(username);
				if (userInfoArr[1] != null) { // password not null (means exists)
					outputStream.writeInt(1); // ping
					outputStream.flush();
					inputStream.readInt(); // pong
					outputStream.writeObject(userInfoArr[1]); // write passwordHash
					outputStream.flush();
					
					int valid = inputStream.readInt(); // password match?
					
					if (valid == 1) { // let's add to list and turn online
						
						/* Player: ID, name, x, y */
						int id = Integer.parseInt(userInfoArr[0]);
						String name = userInfoArr[2];
						int x = Integer.parseInt(userInfoArr[3]);
						int y = Integer.parseInt(userInfoArr[4]);

						setPlayerID(id);

						// send playerID to client
						outputStream.writeInt(id);
						outputStream.flush();

						Player player = new Player(id, name, x, y);

						DatabaseHandler.turnOnline(id, player); 

						GameServer.clients.add(this);

						System.out.println("\nAuthenticated client!");
					}

					inputStream.close();
					outputStream.close();
				}
				else { // user does not exist
					outputStream.writeInt(0);
					outputStream.flush();

					inputStream.close();
					outputStream.close();
					
					System.out.println("\nRejected non-existing user who attempted to auth!");
				}
			}

			// registering a client account here
			// client wants to register, reg packet as follows
			// CMSG "register", SMSG 1, CMSG regDetails,
			// SMSG 1 or 0 (valid or not) --> then server d/c's the client
			else if (process.contentEquals("register")) {
				outputStream.writeInt(1);
				outputStream.flush();

				String regDetails = (String) inputStream.readObject();
				String[] accountDetails = regDetails.split(" ");
				
				boolean success = DatabaseHandler.addAccount(accountDetails);
				
				if (success) { // account created
					outputStream.writeInt(1);
					outputStream.flush();
					
					inputStream.close();
					outputStream.close();

					System.out.println("\nRegistered account name: " + accountDetails[0]);
				}
				else { // account not created
					outputStream.writeInt(0);
					outputStream.flush();

					inputStream.close();
					outputStream.close();
					
					System.out.println("\nRejected account name: " + accountDetails[0]);
				}
			}
			
			// send player coordinates
			else if (process.contentEquals("spc")) {
				outputStream.writeInt(1);
				outputStream.flush();

				playerID = inputStream.readInt();

				// send other online players to client
				SendPlayerCoordinates.sendOnlinePlayers(inputStream, outputStream, playerID);
			}
			// client coordinate update thread connected
			else if (process.contentEquals("update")) {
				outputStream.writeInt(1);
				outputStream.flush();

				playerID = inputStream.readInt();

				// send other players to client -- for great justice!
				AcceptPlayerCoordinates.acceptCoordinates(inputStream, playerID);
			}
			// NYI.. this is placeholder
			else if (process.contentEquals("monster")) {
				outputStream.writeInt(1);
				outputStream.flush();
				// send & receive updates for monsters here 
				SendCreatureCoordinates.updateClient(outputStream, inputStream);
			}
			// this shouldn't happen.. but if it does?
			else {
				System.out.println("Authentication failure!");
				System.out.println(process);
				outputStream.writeInt(0);
				outputStream.flush();
				outputStream.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// no setter because it should not change
	public int getClientID() {
		return this.clientID;
	}
	
	public int getPlayerID() {
		return this.playerID;
	}

	// let client know which player it represents
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}
