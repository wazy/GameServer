package main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player implements Serializable {

	private static final long serialVersionUID = -8405971951484157839L;

	public static List<Player> onlinePlayers = Collections.synchronizedList(
													new ArrayList<Player>(16));

	public int id, x, y;
	public String name;
	public boolean selected = false;

	Player(int id, String name, int x, int y) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	void update(int dx, int dy) {
		x = dx;
		y = dy;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setID(int newID) {
		this.id = newID;
	}

	public int getID() {
		return this.id;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public String getName() {
		return this.name;
	}

	public static int getPosition(int clientID) {
		// absolutely necessary to synchronize when we iterate over collection
		synchronized (Player.onlinePlayers) { 
			// what position is the player currently at in the list
			for (int i = 0; i < Player.onlinePlayers.size(); i++) {
				if (Player.onlinePlayers.get(i).getID() == clientID) {
					return i;
				}
			}
			return -1; // shouldn't get here
		}
	}
}
