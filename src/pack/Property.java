package pack;

public class Property {
	
	private int id = -999;
	private int rotation = -1;
	private int x_location = -999;
	private int y_location = -999;
	private String edge = null;
	
	public Property(int id, int rotation, int x_location, int y_location) {
		super();
		this.id = id;
		this.rotation = rotation;
		this.x_location = x_location;
		this.y_location = y_location;
	}

	public Property(int id, int rotation, String edge) {
		super();
		this.id = id;
		this.rotation = rotation;
		this.edge = edge;
	}

	public Property(int id) {
		super();
		this.id = id;
	}

	public Property(int id, String edge) {
		super();
		this.id = id;
		this.edge = edge;
	}

	public Property(int id, int rotation) {
		super();
		this.id = id;
		this.rotation = rotation;
	}

	public Property(int id, int x_location, int y_location) {
		super();
		this.id = id;
		this.x_location = x_location;
		this.y_location = y_location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getX_location() {
		return x_location;
	}

	public void setX_location(int x_location) {
		this.x_location = x_location;
	}

	public int getY_location() {
		return y_location;
	}

	public void setY_location(int y_location) {
		this.y_location = y_location;
	}

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	
	
	

}
