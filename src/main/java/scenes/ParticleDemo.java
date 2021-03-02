package scenes;

import ecs.GameObject;
import ecs.ParticleSystem;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Camera;
import graphics.Color;
import input.Mouse;
import org.joml.Vector2f;
import physics.Particle;
import physics.Transform;
import util.Engine;

import static graphics.Graphics.setDefaultBackground;

public class ParticleDemo extends util.Scene {

    public static void main (String[] args) {
        Engine.init(1080, 720, "Particle Systems", 1.0f);
    }

    GameObject ps;
    Particle p;
    public void awake() {
        setDefaultBackground(Color.BLACK);
        camera = new Camera();

        ps = new GameObject(new Transform(100, 100, 10, 10), 1);
        ps.addComponent(new ParticleSystem());

        p = new Particle(new Vector2f(100, 100));
    }

    public void update() {
        p.update();
        ps.setTransformX(Mouse.mouseX);
        ps.setTransformY(Mouse.mouseY);
    }

}