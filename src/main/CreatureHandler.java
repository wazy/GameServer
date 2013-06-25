package main;

import java.util.Random;

public class CreatureHandler implements Runnable {
	public void run() {
		// TODO further implement this..
		System.out.println("Creature handler thread started..");

		try {
			Random rand = new Random();
			
			DatabaseHandler.queryCreatures();

			while (true) {
				for (int i = 0; i < Creature.creatureList.size(); i++) {
					Creature creature = Creature.creatureList.get(i);

					int x = creature.getX();
					int y = creature.getY();

					// out of bounds of screen -- reset creature
					if (x > 640 || y > 480 || x < 0 || y < 0) {
						creature.setX(rand.nextInt(641)); // rand.nextInt(max - min + 1) + min;
						creature.setY(rand.nextInt(481));
					}
					else {
						creature.setX(x + rand.nextInt(50) - 25); // -25 to 25
						creature.setY(y + rand.nextInt(50) - 25);
					}
				}
				Thread.sleep(200);
			}
		}
		// shouldn't error here
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
