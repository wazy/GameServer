package entities;

import java.io.Serializable;

public abstract class AbstractEntity implements EntityInterface, Serializable {

	private static final long serialVersionUID = -8405971951484157839L;

	protected String name;
	protected int id, x, y, width, height;
	
	public AbstractEntity(int id, String name, int x, int y, int width, int height) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	// Do nothing for now: stationary object
	public void update(int delta) {
		return;
	}
	
	@Override
	public void teleport(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void fly(double flyValue) {
		// Make magic happen.
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public void setName(String newName) {
		this.name = newName;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
}
