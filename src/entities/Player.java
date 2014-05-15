package entities;
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

	public Player(int id, String name, int x, int y) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public void update(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
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

	// send ID + X + Y as one int (make all size three)
	public static int formatPlayerUpdatePacket(int ID, int X, int Y) {
		int one = String.valueOf(ID).length();
		int two = String.valueOf(X).length();
		int three = String.valueOf(Y).length();

		String res = helpFormatPlayerUpdatePacket(ID, one) + helpFormatPlayerUpdatePacket(X, two)
					 									+ helpFormatPlayerUpdatePacket(Y, three);
		
		return Integer.parseInt(res);
	}

	// make 10 -> 010 or 3 -> 003 for update packet format 
	public static String helpFormatPlayerUpdatePacket(int part, int length) {
		String res = String.valueOf(part);
		for (int i = length; i < 3; i++) {
			res = 0 + res;
		}
		return res;
	}
}
