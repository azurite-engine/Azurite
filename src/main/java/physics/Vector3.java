package physics;

public class Vector3 {
	
	public float x;
	public float y;
	public float z;
	
	public Vector3 (float px, float py, float pz) {
		x = px;
		y = py;
		z = pz;
	}
	
	public Vector3 Vector3 () {
		return this;		
	}
	
	public void add (float x2, float y2, float z2) {
		x += x2;
		y += y2;
		y += z2;
	}
	
	public void print () {
		System.out.println("X: " + x + ", Y: " + y + ", Z: " + z);
	}
}
