package physics;

/*
* Please do not use this class, it is currently being phased out of the engine
*/

@Deprecated
public class Vector2 {
	
	public float x;
	public float y;

	public Vector2 (double px, double py) {
		x = (float) px;
		y = (float) py;
	}
	
	public Vector2 Vector2 () {
		return this;		
	}
	
	public void add (float x2, float y2) {
		x += x2;
		y += y2;
	}
	
	public void print () {
		System.out.println("X: " + x + ", Y: " + y);
	}
}
