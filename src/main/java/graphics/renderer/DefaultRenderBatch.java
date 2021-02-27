package graphics.renderer;

import ecs.SpriteRenderer;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

public class DefaultRenderBatch extends RenderBatch {
	private final SpriteRenderer[] sprites;

	private int numberOfSprites;

	DefaultRenderBatch(int maxBatchSize, int zIndex) {
		super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
		this.sprites = new SpriteRenderer[maxBatchSize];

		this.numberOfSprites = 0;
	}

	// NOTE: this function figures out how to add vertices with an origin at the
	// bottom left
	@Override
	protected void loadVertexProperties(int index, int offset) {
		SpriteRenderer sprite = this.sprites[index];

		Vector4f color = sprite.getColorVector();
		Vector2f[] textureCoordinates = sprite.getTexCoords();
		int textureID = addTexture(sprite.getTexture());

		// Add vertex with the appropriate properties
		float xAdd = 1.0f;
		float yAdd = 1.0f;
		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 1:
					yAdd = 0.0f;
					break;
				case 2:
					xAdd = 0.0f;
					break;
				case 3:
					yAdd = 1.0f;
					break;
			}

			// Load position
			Transform spr = sprite.gameObject.getTransform();
			data[offset] = spr.position.x + (xAdd * spr.scale.x);
			data[offset + 1] = spr.position.y + (yAdd * spr.scale.y);

			// Load color
			data[offset + 2] = color.x; // Red
			data[offset + 3] = color.y; // Green
			data[offset + 4] = color.z; // Blue
			data[offset + 5] = color.w; // Alpha

			// Load texture coordinates
			data[offset + 6] = textureCoordinates[i].x;
			data[offset + 7] = textureCoordinates[i].y;

			// Load texture ID
			data[offset + 8] = textureID;

			offset += vertexCount;
		}
	}

	public void updateBuffer() {
		for (int i = 0; i < numberOfSprites; i ++) {
			SpriteRenderer spr = sprites[i];
			if (spr.isDirty()) {
				load(i);
				spr.setClean();
			}
		}
		super.updateBuffer();
	}

	public boolean addSprite(SpriteRenderer sprite) {
		// If the batch still has room, and is at the same z index as the sprite, then add it to the batch
		if (hasRoomLeft() && zIndex() == sprite.gameObject.zIndex()) {
			Texture tex = sprite.getTexture();
			if (tex == null || (hasTexture(tex) || hasTextureRoom())) {
				// Get the index and add the renderObject
				int index = this.numberOfSprites;
				this.sprites[index] = sprite;
				this.numberOfSprites++;

				// Add properties to local vertices array
				load(index);

				if (this.numberOfSprites >= this.maxBatchSize) {
					this.hasRoom = false;
				}
				return true;
			}
		}
		return false;
	}
}
