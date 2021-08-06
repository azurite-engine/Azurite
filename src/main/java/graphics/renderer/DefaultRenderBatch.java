package graphics.renderer;

import ecs.SpriteRenderer;
import graphics.Primitive;
import graphics.ShaderDatatype;
import graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import physics.Transform;

import java.util.ArrayList;
import java.util.List;

public class DefaultRenderBatch extends RenderBatch {
	private final List<SpriteRenderer> sprites;

	/**
	 * Create a default type render batch
	 *
	 * @param maxBatchSize maximum number of sprites in the batch
	 * @param zIndex       zIndex of the batch. Used for sorting.
	 */
	DefaultRenderBatch(int maxBatchSize, int zIndex) {
		super(maxBatchSize, zIndex, Primitive.QUAD, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
		this.sprites = new ArrayList<>();
		this.primitiveVertices = new float[vertexCount * 4];
	}

	/**
	 * This function figures out how to add vertices with an origin at the center
	 *
	 * @param index  index of the primitive to be loaded
	 * @param offset offset of where the primitive should start being added to the array
	 */
	@Override
	protected void loadVertexProperties(int index, int offset) {
		SpriteRenderer sprite = this.sprites.get(index);
		Vector4f color = sprite.getColorVector();
		Vector2f[] textureCoordinates = sprite.getTexCoords();

		int textureID;
		if (sprite.getTexture() != null)
			textureID = addTexture(sprite.getTexture());
		else
			textureID = 0;

		// Add vertex with the appropriate properties
		float xAdd = 0.5f;
		float yAdd = 0.5f;
		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 1:
					yAdd = -0.5f;
					break;
				case 2:
					xAdd = -0.5f;
					break;
				case 3:
					yAdd = 0.5f;
					break;
			}

			// Load position
			Transform spr = sprite.gameObject.getTransform();

			Vector2f shifted = spr.position.add(spr.getScale().div(2, new Vector2f()), new Vector2f());
			float scaledX = (xAdd * spr.scale.x);
			float scaledY = (yAdd * spr.scale.y);

			data[offset] = shifted.x + (float) ((Math.cos(spr.getRotationRadians()) * scaledX)
					- (Math.sin(spr.getRotationRadians()) * scaledY));
			data[offset + 1] = shifted.y + (float) ((Math.sin(spr.getRotationRadians()) * scaledX)
					+ (Math.cos(spr.getRotationRadians()) * scaledY));

			primitiveVertices[primitiveVerticesOffset] = data[offset];
			primitiveVertices[primitiveVerticesOffset + 1] = data[offset + 1];

			// Load color
			data[offset + 2] = color.x; // Red
			data[offset + 3] = color.y; // Green
			data[offset + 4] = color.z; // Blue
			data[offset + 5] = color.w; // Alpha

			primitiveVertices[primitiveVerticesOffset + 2] = data[offset + 2];
			primitiveVertices[primitiveVerticesOffset + 3] = data[offset + 3];
			primitiveVertices[primitiveVerticesOffset + 4] = data[offset + 4];
			primitiveVertices[primitiveVerticesOffset + 5] = data[offset + 5];

			// Load texture coordinates
			data[offset + 6] = textureCoordinates[i].x;
			data[offset + 7] = textureCoordinates[i].y;

			primitiveVertices[primitiveVerticesOffset + 6] = data[offset + 6];
			primitiveVertices[primitiveVerticesOffset + 7] = data[offset + 7];

			// Load texture ID
			data[offset + 8] = textureID;

			primitiveVertices[primitiveVerticesOffset + 8] = data[offset + 8];

			offset += vertexCount;
			primitiveVerticesOffset += vertexCount;
		}
	}

	/**
	 * Checks if any sprite is dirty (has changed any of its properties).
	 * If so, resets its data in the data[] via load().
	 * <p>
	 * Calls the RenderBatch::updateBuffer method to re-upload the data if required
	 * </p>
	 */
	@Override
	public void updateBuffer() {
		for (int i = 0; i < sprites.size(); i++) {
			if (sprites.get(i).isDirty()) {
				// Create map for the dirty quad starting at its offset and ending in its length
				super.updateBuffer(i);
				sprites.get(i).setClean();
			}
		}
	}

	/**
	 * Adds a sprite to this batch
	 *
	 * @param sprite sprite to be added
	 * @return if the sprite was successfully added to the batch
	 */
	public boolean addSprite(SpriteRenderer sprite) {
		// If the batch already contains the sprite don't add it to any other batch
		if (sprites.contains(sprite)) return true;

		// If the batch still has room, and is at the same z index as the sprite, then add it to the batch
		if (hasRoomLeft() && zIndex() == sprite.gameObject.zIndex()) {
			Texture tex = sprite.getTexture();
			if (tex == null || (hasTexture(tex) || hasTextureRoom())) {
				// Get the index and add the renderObject
				sprites.add(sprite);
				sprite.setLocation(this, sprites.size() - 1);

				// Add properties to local vertices array
				load(sprites.size() - 1);

				if (sprites.size() >= this.maxBatchSize) {
					this.hasRoom = false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the given sprite from the batch
	 *
	 * @param sprite sprite to be removed
	 */
	public void removeSprite(SpriteRenderer sprite) {
		// Confirm this sprite has been added to this batch
		if (sprite.getBatch() == this) {
			// Remove the sprite from the list
			int index = sprites.indexOf(sprite);
			sprites.remove(index);

			// Set Indices of the sprites to the new indices
			for (int i = index; i < sprites.size(); i++) {
				SpriteRenderer spr = sprites.get(i);
				spr.setLocation(this, i);
				spr.markDirty();
			}

			// Call Remove with the sprites index to remove it from the data buffer
			remove(sprite.getIndex());
		}
	}
}
