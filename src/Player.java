import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

	private static final long serialVersionUID = -8405971951484157839L;

	public static ArrayList<Player> onlinePlayers = new ArrayList<Player>(16);
	
	public int id, x, y;
	public String name;
	public boolean selected = false;
	
	Player (int id, String name, int x, int y) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	void update(int dx, int dy) {
		x += dx;
		y += dy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (selected ? 1231 : 1237);
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (selected != other.selected)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}