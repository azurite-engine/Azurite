package ecs;

public abstract class Component {

	// Parent GameObject
	public GameObject gameObject = null;
	
	public void start () {}
	
	public void update (float dt) {}
	
}
