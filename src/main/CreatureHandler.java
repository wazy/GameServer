package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreatureHandler {
	public static void updateClient(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
		// TODO further implement this..
		System.out.println("Creature thread started..");
		
		try {
			Creature.CreatureList.add(new Creature(1, "Monster", 100, 100, 1));
			while (true) {
				//Creature.CreatureList.get(0).x = Creature.CreatureList.get(0).x + 10;
				//Creature.CreatureList.get(0).y = Creature.CreatureList.get(0).y + 10;
				
				outputStream.reset();
				outputStream.writeObject(Creature.CreatureList);
				outputStream.flush();
				Thread.sleep(1000);
			}
		}
		// silence error from client closing socket
		catch (Exception e) {
			return;
		}
	}
}
