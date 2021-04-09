package pack;

public class FixPolygon {
	
	int polygon_id = -999;
	int sheet_id = -999;
	int rotation = -1;
	int x_location = -999;
	int y_location = -999;
	
	public FixPolygon(int polygon_id, int sheet_id, int rotation, int x_location, int y_location) {
		super();
		
		this.polygon_id = polygon_id;
		this.sheet_id = sheet_id;
		this.rotation = rotation;
		this.x_location = x_location;
		this.y_location = y_location;
	}

	public int getPolygon_id() {
		return polygon_id;
	}

	public void setPolygon_id(int polygon_id) {
		this.polygon_id = polygon_id;
	}

	public int getSheet_id() {
		return sheet_id;
	}

	public void setSheet_id(int sheet_id) {
		this.sheet_id = sheet_id;
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
}
