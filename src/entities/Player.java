package entities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends AbstractMoveableEntity {

	private static final long serialVersionUID = -8405971951484157839L;

	public static List<Player> onlinePlayers = Collections.synchronizedList(
													new ArrayList<Player>(16));

	public String name;
	public boolean selected = false;
	public int id, x, y, width, height;
	
	private static final int PLAYER_WIDTH = 50;
	private static final int PLAYER_HEIGHT = 50;

	public Player (int id, String name, int x, int y, String textureName) {
		super(id, name, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, textureName);
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
