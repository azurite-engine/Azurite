package ecs;

import com.sun.pisces.PiscesRenderer;
import graphics.Color;
import graphics.Texture;
import physics.Transform;
import util.Assets;
import util.Utils;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static graphics.Color.WHITE;

public class SpriteRenderer extends Component {

	private Vector4f color = new Color(255, 100, 100, 255).toNormalizedVec4f();
	private Sprite sprite;

	private Transform lastTransform;
	private boolean isDirty = false; // Dirty flag, tells renderer to redraw if object components have changed

	public SpriteRenderer(Vector4f color) {
		this.setColor(color);
		this.sprite = new Sprite(null);
		this.isDirty = true;
	}

	public SpriteRenderer(Color color) {
		this.setColor(color.toVec4f());
		// ^ Check .toNormalizedVec4f ^
		this.sprite = new Sprite(null);
		this.isDirty = true;
	}

	public SpriteRenderer(Sprite sprite) {
		this.sprite = sprite;
		this.color = WHITE.toNormalizedVec4f();
		this.isDirty = true;
	}
	
	public SpriteRenderer(String path) {
		this.sprite = new Sprite(Assets.getTexture(path));
		this.color = WHITE.toNormalizedVec4f();
		this.isDirty = true;
	}

	public void useGlobalFill() {

	}

	@Override
	public void start() {
		this.lastTransform = gameObject.getTransform().copy();
	}

	@Override
	public void update(float dt) {
		if (!this.lastTransform.equals(this.gameObject.getTransform())) {
			this.gameObject.getTransform().copy(this.lastTransform);
			isDirty = true;
		}
	}

	public Texture getTexture() {
		return sprite.getTexture();
	}

	public void setTexture(Texture texture) {
		if (sprite.getTexture() != texture) {
			sprite.setTexture(texture);
			isDirty = true;
		}
	}

	public Vector2f[] getTexCoords() {
		return sprite.getTextureCoordinates();
	}

	public Vector4f getColorVector() {
		return color;
	}

	public Color getColor() {
		return new Color(color.x, color.y, color.z, color.w);
	}

	public void setColor(Vector4f c) {
		if (!color.equals(c)) {
			this.color = c;
			isDirty = true;
		}
	}

	public void setColor(Color color) {
		if (!this.color.equals(color.toNormalizedVec4f())) {
			this.color = color.toNormalizedVec4f();
			isDirty = true;
		}
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		isDirty = true;
	}

	public void setAlpha (float a) {
		color.w = Utils.map(a, 0, 255, 0, 1);
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setClean() {
		isDirty = false;
	}
}
