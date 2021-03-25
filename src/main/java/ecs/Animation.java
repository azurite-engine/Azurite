package ecs;

public class Animation extends Component {
	private final float speed;
	private boolean loop;
	private final Sprite[] sprites;
	private SpriteRenderer spriteRendererComponent;
	private boolean running;
	private float timeAcc;
	private int pointer;

	public Animation(float speed, Sprite... sprites) {
		this(speed, true, sprites);
	}

	public Animation(float speed, boolean run, Sprite... sprites) {
		this(speed, run, true, sprites);
	}

	public Animation(float speed, boolean run, boolean loop, Sprite... sprites) {
		this.speed = speed;
		this.loop = loop;
		this.sprites = sprites;
		this.running = run;
	}

	/**
	 * Called once on Component initialization.
	 */
	@Override
	public void start() {
		spriteRendererComponent = gameObject.getComponent(SpriteRenderer.class);
		if (spriteRendererComponent == null) {
			spriteRendererComponent = new SpriteRenderer(sprites[0]);
			gameObject.addComponent(spriteRendererComponent);
		}
	}

	/**
	 * Called once per frame for each Component
	 *
	 * @param dt Engine.deltaTime
	 */
	@Override
	public void update(float dt) {
		if (running) {
			timeAcc += dt;

			if (timeAcc >= speed) {
				timeAcc = 0;
				Sprite frame = sprites[pointer++];
				spriteRendererComponent.setSprite(frame);

				if (pointer >= sprites.length) {
					if (loop) {
						pointer = 0;
					} else {
						running = false;
					}
				}
			}
		}
	}
}
