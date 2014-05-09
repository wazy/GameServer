package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class Creature implements Serializable {
	private static final long serialVersionUID = -8405971951484157840L;

	public static int creatureID = 0;

	public static List<Creature> creatureList = Collections.synchronizedList(new ArrayList<Creature>(16));
	public static int listPosition = 0; // client's position in the list init to zero
	public int id, x, y, width, height, alliance;
	public String name;
	public boolean selected = false;
	
	Creature (int id, String name, int x, int y, int width, int height, int alliance) {
		this.id = id;
		this.name = name;
		this.alliance = alliance;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	boolean inBounds(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + 50 && mouseY > y && mouseY < y + 50)
			return true;
		else
			return false;
	}
	
	public int getX() {
		return this.x;
	}
	
	public void setX(int X) {
		this.x = X;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setY(int Y) {
		this.y = Y;
	}
	
	public static void setId(String creatureID) {
		Creature.creatureID = Integer.parseInt(creatureID);
	}
	public static int getId() {
		return Creature.creatureID;
	}
}