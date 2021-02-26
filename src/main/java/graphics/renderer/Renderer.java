package graphics.renderer;

import ecs.GameObject;
import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.ShaderDatatype;
import graphics.Texture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
	private static final int MAX_BATCH_SIZE = 1000;

	private List<RenderBatch> batches;
	
	public Renderer () {
		this.batches = new ArrayList<>();
	}

	/**
	 * Loop through all render batches and call their render methods.
	 */
	public void render () {
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}

	/**
	 * Add a gameObject to the renderer, and if it contains a component that affects rendering, like a sprite or light, those are added to the batch.
	 * @param gameObject the GameObject with renderable components
	 */
	public void add (GameObject gameObject) {
		SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
		if (spr != null) {
			addSpriteRenderer(spr);
		}

		PointLight light = gameObject.getComponent(PointLight.class);
		if (light != null) {
			addPointLight(light);
		}
	}

	/**
	 * Adds the PointLight component to all Batches
	 * @param light PointLight: The PointLight component to be added
	 */
	private void addPointLight(PointLight light) {
		for (RenderBatch batch : batches) {
			batch.addPointLight(light);
		}
	}

	/**
	 * Adds the SpriteRenderer to a single batch, and creates a new batch if their is no space.
	 * @param sprite SpriteRenderer: The SpriteRenderer component to be added
	 */
	private void addSpriteRenderer (SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			// If the batch still has room, and is at the same z index as the sprite, then add it to the batch and break
			if (batch.hasRoomLeft() && batch.zIndex() == sprite.gameObject.zIndex()) {
				Texture tex = sprite.getTexture();
				if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}

		// If the conditions for all of the above batches weren't met, create a new one and add to it
		if (!added) {
			// If unable to add to previous batch, create a new one
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.zIndex(), ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT4, ShaderDatatype.FLOAT2, ShaderDatatype.FLOAT);
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}

	public void clean() {
//		batches.forEach(RenderBatch::delete);
	}
}
