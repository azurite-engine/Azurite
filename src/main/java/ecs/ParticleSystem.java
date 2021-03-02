package ecs;

import org.joml.Vector2f;
import physics.Particle;
import physics.Transform;

import java.util.ArrayList;
import java.util.Iterator;

public class ParticleSystem extends Component {
    int total = 10;
    ArrayList<Particle> particles = new ArrayList<Particle>();

    @Override
    public void update(float dt) {
        particles.add(new Particle(new Vector2f(100, 100)));
        Iterator<Particle> it = particles.iterator();

        while (it.hasNext()) {
            Particle p = it.next();
            p.update();

            if (p.isDead()) {
                it.remove();
            }
        }
    }
}
