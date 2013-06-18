package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CreatureHandler {
	public static void updateClient(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
		// TODO further implement this..
		System.out.println("Creature thread started..");
		// this will terminate client after 16 seconds (testing at the moment)
		try {
			for (int i = 0; i < 16; i++) {
				Creature.CreatureList.add(new Creature(i, "Monster", i*20, i*20, i));
				outputStream.flush();
				outputStream.writeObject(Creature.CreatureList);
				outputStream.flush();
				Thread.sleep(1000);
			}
			outputStream.close();
			inputStream.close();
		}
		// silence error from client closing socket
		catch (Exception e) {
			return;
		}
	}
}
