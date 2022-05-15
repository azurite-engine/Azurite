package graphics.renderer;

import ecs.GameObject;
import graphics.Framebuffer;
import graphics.Shader;
import graphics.Texture;
import util.OrderPreservingList;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

/**
 * 
 * <p>
 * A renderer is responsible for taking collected data of a specified type,
 * formatting it for a selected shader, and rendering it. Every renderer has
 * an associated {@code RenderBatch} to render. The pipeline for processing
 * the metadata of a render batch and drawing batched data is as follows:
 *     <ol>
 *         <li>
 *             The {@code Renderer} class is extended by another class specified
 *             to render a certain type of data batch; for the sake of example,
 *             let's use a quadrilateral render batch. A shader is also created
 *             with the same vertex properties specified in the render batch.
 *         </li>
 *         <li>
 *             A {@code Shader} object is then created, encapsulating the {@code uniform}s
 *             and {@code layout}s of the shader's source code. It is then attached to
 *             this renderer.
 *         </li>
 *         <li>
 *             Based on the {@code uniform}s of the shader, various bits of data is
 *             submitted to the GPU, like camera position, pointlight position, etc.
 *         </li>
 *         <li>
 *             After shader parsing and handling, the data is rendered.
 *         </li>
 *     </ol>
 *     A renderer also specifies a {@code framebuffer}, which acts as, well, a
 *     buffer which GPU pixel data is stored in to be drawn all at once on the monitor.
 * </p>
 *
 * @see RenderBatch
 * @see Shader
 * @see Framebuffer
 */
public abstract class Renderer {
	/**
	 * Texture slots to be uploaded to the shader. You don't have to upload them in your custom renderer.
	 */
	protected final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

	/**
	 * A list of batches
	 */
	protected final List<RenderBatch> batches;
	/**
	 * Framebuffer to which this renderer will render
	 */
	public Framebuffer framebuffer;
	protected boolean noRebuffer = false;
	/**
	 * Shader to be used for rendering
	 */
	private Shader shader;

	public Renderer() {
		this.batches = new OrderPreservingList<>();
	}

	/**
	 * Create a shader
	 *
	 * @return the created shader
	 */
	protected abstract Shader createShader();

	/**
	 * Create a framebuffer
	 *
	 * @return the created fbo
	 */
	protected abstract Framebuffer createFramebuffer();

	/**
	 * Create a new Batch with appropriate parameters
	 *
	 * @return a new batch
	 */
	protected abstract RenderBatch createBatch(int zIndex);

	/**
	 * Upload the required uniforms
	 *
	 * @param shader the shader
	 */
	protected abstract void uploadUniforms(Shader shader);

	/**
	 * Rebuffer all the data into batches
	 */
	protected abstract void rebuffer();

	/**
	 * Start batches. Ready for buffering data
	 */
	private void start() {
		for (RenderBatch batch : batches) {
			batch.start();
		}
	}

	/**
	 * Finish Setting data for all batches. Upload to gpu ready for rendering
	 */
	private void finish() {
		for (RenderBatch batch : batches) {
			batch.finish();
		}
	}

	/**
	 * Get the batch in which the current data can be submitted
	 * Has to be called PER PRIMITIVE SUBMISSION
	 *
	 * @param texture
	 */
	public RenderBatch getAvailableBatch(Texture texture, int reqdZ) {
		for (RenderBatch batch : batches) {
			if (!batch.isFull && batch.zIndex() == reqdZ)
				return batch;

			if (batch.isFull_Textures)
				if (batch.hasTexture(texture) && batch.zIndex() == reqdZ)
					return batch;
		}

		// All batches full
		RenderBatch batch = createBatch(reqdZ);
		batch.init();
		batch.start();
		batches.add(batch);
		return batch;
	}

	/**
	 * Add a gameObject to the renderer, and if it contains a component that affects rendering, like a sprite or light, those are added to a batch.
	 *
	 * @param gameObject the GameObject with renderable components
	 */
	public void add(GameObject gameObject) {
	}

	/**
	 * Remove a gameObject from the renderer if it contains the component that gets rendered.
	 *
	 * @param gameObject the GameObject with renderable componentsl
	 */
	public void remove(GameObject gameObject) {
	}

	/**
	 * Creates the renderer's shader and framebuffer
	 */
	public void init() {
		shader = createShader();
		framebuffer = createFramebuffer();
	}

	/**
	 * Get a color attachment texture from the framebuffer
	 *
	 * @param index index of the required color attachment texture. Will return -1 if there is no attachment at that index.
	 * @return the texture ID of the attachment
	 */
	public Texture fetchColorAttachment(int index) {
		return framebuffer.getColorAttachment(index);
	}

	/**
	 * Loop through all render batches and render them
	 */
	public void render() {
		framebuffer.bind();
		prepare();
		shader.attach();
		uploadUniforms(shader);

		if (!noRebuffer) {
			start();
			rebuffer();
			finish();
		}

		for (RenderBatch batch : batches) {
			batch.bind();
			glDrawElements(batch.primitive.openglPrimitive, batch.getVertexCount(), GL_UNSIGNED_INT, 0);
			batch.unbind();
		}
		shader.detach();
		Framebuffer.unbind();
	}

	/**
	 * Prepare for rendering. Do anything like setting background here.
	 */
	protected abstract void prepare();

	/**
	 * Delete all the Batches.
	 */
	public void clean() {
		batches.forEach(RenderBatch::delete);
	}
}
