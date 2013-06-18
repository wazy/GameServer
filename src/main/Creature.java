package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class Creature implements Serializable {
	private static final long serialVersionUID = -8405971951484157840L;

	public static int creatureID = 0;

	public static List<Creature> CreatureList = Collections.synchronizedList(new ArrayList<Creature>(16));
	public static int listPosition = 0; // client's position in the list init to zero
	public int id, x, y, alliance;
	public String name;
	public boolean selected = false;
	
	Creature (int id, String name, int x, int y, int alliance) {
		this.id = id;
		this.name = name;
		this.alliance = alliance;
		this.x = x;
		this.y = y;
	}
	
	boolean inBounds(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + 50 && mouseY > y && mouseY < y + 50)
			return true;
		else
			return false;
	}
	
	public static void setId(String creatureID) {
		Creature.creatureID = Integer.parseInt(creatureID);
	}
	public static int getId() {
		return Creature.creatureID;
	}
}