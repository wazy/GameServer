package entities;

public interface EntityInterface {
	public void update(int delta);
	public void fly(double flySpeed);
	public void teleport(int x, int y);
	//public boolean collides(Entity other);

	public void setX(int x);
	public void setY(int y);
	public void setWidth(int width);
	public void setHeight(int height);
	public void setID(int newID);
	public void setName(String newName);

	public int getX();
	public int getY();
	public int getHeight();
	public int getWidth();
	public int getID();
	public String getName();

}
