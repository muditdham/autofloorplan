package pack;

public class Tuple {

	Convex convex;
	
	boolean status;
	
	public Tuple(Convex convex, boolean status) {
		super();
		this.convex = convex;
		this.status = status;
	}

	public Convex getConvex() {
		return convex;
	}

	public void setConvex(Convex convex) {
		this.convex = convex;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
