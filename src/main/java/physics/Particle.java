package physics;

import ecs.Component;
import ecs.GameObject;
import ecs.Sprite;
import ecs.SpriteRenderer;
import graphics.Color;
import graphics.Texture;
import org.joml.Vector2f;
import util.Assets;

import java.util.ArrayList;
import java.util.List;

public class Particle {

    Vector2f location;
    Vector2f velocity;
    Vector2f acceleration;

    GameObject g;

    float lifespan;

    public Particle(Vector2f l) {
        location = l;
        acceleration = new Vector2f(0, 0.05f);
        velocity = new Vector2f();

        g = new GameObject("particle", new ArrayList<Component>(), new Transform(location, new Vector2f(20, 20)), 1, true);
        g.addComponent(new SpriteRenderer(new Sprite(new Texture("src/assets/images/pepper.png")))); // DOES NOT WORK WITH QUEUE!!

        lifespan = 255;
    }

    public void update() {
        velocity.add(acceleration);
        location.add(velocity);
        g.setTransformX(location.x());
        g.setTransformY(location.y());

        lifespan -= 2.0;
        g.getComponent(SpriteRenderer.class).setAlpha(lifespan);
    }

    public void applyForce (Vector2f force) {
        velocity.add(force);
    }

    public boolean isDead() {
        return lifespan < 0.0;
    }
}
