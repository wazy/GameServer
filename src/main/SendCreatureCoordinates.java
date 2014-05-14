package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entities.Creature;

public class SendCreatureCoordinates {
	public static void updateClient(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
		System.out.println("Sending creature coordinates thread started..");

		try {
			while (true) { // just send creatures to client -- magic happens in CreatureHandler
				outputStream.reset();
				outputStream.writeObject(Creature.creatureList);
				outputStream.flush();
				Thread.sleep(500);
			}
		}

		// silence error from client closing socket
		catch (Exception e) {
			return;
		}
	}
}
