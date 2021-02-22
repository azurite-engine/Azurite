package graphics.renderer;

import ecs.PointLight;
import ecs.SpriteRenderer;
import graphics.Shader;
import graphics.Texture;
import graphics.Window;
import org.joml.Vector3f;
import physics.Transform;
import util.Assets;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{
	/**
	 * Vertex layout
	 *
	 * position color UV tex ID float, float, float, float, float, float, float,
	 * float, float
	 */
	private final int POSITION_SIZE = 2;
	private final int COLOR_SIZE = 4;
	private final int TEXTURE_COORDS_SIZE = 2;
	private final int TEXTURE_ID_SIZE = 1;

	private final int POSITION_OFFSET = 0;
	private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
	private final int TEXTURE_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
	private final int TEXTURE_ID_OFFSET = TEXTURE_COORDS_OFFSET + TEXTURE_COORDS_SIZE * Float.BYTES;

	private final int VERTEX_SIZE = 9;
	private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

	private final SpriteRenderer[] sprites;
	private int numberOfLights;
	private int numberOfSprites;
	private boolean hasRoomLeft;

	private final List<PointLight> lights;

	private final float[] vertices;

	private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};
	private final ArrayList<Texture> textures;

	private int vaoID, vboID;
	private final int maxBatchSize;
	private final Shader shader;

	private final int zIndex;

	RenderBatch(int maxBatchSize, int zIndex) {
		shader = Assets.getShader("src/assets/shaders/default.glsl");
		lights = new ArrayList<>();

		this.sprites = new SpriteRenderer[maxBatchSize];
		this.maxBatchSize = maxBatchSize;

		vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

		this.numberOfSprites = 0;
		this.numberOfLights = 0;
		this.hasRoomLeft = true;
		this.textures = new ArrayList<>();
		this.zIndex = zIndex;
	}

	public void start() {
		// Generate and bind a Vertex Array Object
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		// Allocate space for vertices
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

		// Create and upload indices buffer
		int eboID = glGenBuffers();
		int[] indices = generateIndices();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

		// Enable Buffer attribute pointers (tell openGL what a vertex layout looks
		// like)
		glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
		glEnableVertexAttribArray(1);

		glVertexAttribPointer(2, TEXTURE_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDS_OFFSET);
		glEnableVertexAttribArray(2);

		glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}

	public void addPointLight(PointLight light) {
		numberOfLights++;
		assert numberOfLights <= 10 : "NO MORE THAN 10 LIGHTS";
		lights.add(light);
	}

	public void addSprite(SpriteRenderer sprite) {
		// Get the index and add the renderObject
		int index = this.numberOfSprites;
		this.sprites[index] = sprite;
		this.numberOfSprites++;

		if (sprite.getTexture() != null) {
			if (!textures.contains(sprite.getTexture())) {
				textures.add(sprite.getTexture());
			}
		}

		// Add properties to local vertices array
		loadVertexProperties(index);

		if (this.numberOfSprites >= this.maxBatchSize) {
			this.hasRoomLeft = false;
		}
	}

	public void render() {
		boolean rebufferData = false;
		for (int i = 0; i < numberOfSprites; i ++) {
			SpriteRenderer spr = sprites[i];
			if (spr.isDirty()) {
				loadVertexProperties(i);
				spr.setClean();
				rebufferData = true;
			}
		}
		if (rebufferData) {
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		}
		// Use shader
		shader.use();
		shader.uploadMat4f("uProjection", Window.currentScene.camera().getProjectionMatrix());
		shader.uploadMat4f("uView", Window.currentScene.camera().getViewMatrix());

		// Set lighting uniforms
		Vector2f[] lightPositions = new Vector2f[numberOfLights];
		Vector3f[] lightColors = new Vector3f[numberOfLights];
		float[] lightIntensities = new float[numberOfLights];

		for (int i = 0; i < numberOfLights; i++) {
			PointLight light = lights.get(i);
			lightPositions[i] = light.lastTransform.getPosition();
			lightColors[i] = light.color;
			lightIntensities[i] = light.intensity;
		}

		shader.uploadVec2fArray("uLightPosition", lightPositions);
		shader.uploadVec3fArray("uLightColour", lightColors);
		shader.uploadFloatArray("uIntensity", lightIntensities);
		shader.uploadFloat("uMinLighting", 0.3f);
		shader.uploadInt("uNumLights", numberOfLights);

		for (int i = 0; i < textures.size(); i ++) {
			glActiveTexture(GL_TEXTURE0 + i + 1);
			textures.get(i).bind();
		}
		shader.uploadIntArray("uTextures", textureSlots);

		// bind the VAO
		glBindVertexArray(vaoID);

		// enable vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawElements(GL_TRIANGLES, this.numberOfSprites * 6, GL_UNSIGNED_INT, 0);

		// unbind everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);

		for (int i = 0; i < textures.size(); i ++) {
			textures.get(i).unbind();
		}

		shader.detach();
	}

	private void loadVertexProperties(int index) {
		// NOTE: this function figures out how to add vertices with an origin at the
		// bottom left
		SpriteRenderer sprite = this.sprites[index];

		// FInd offset within array (4 vertices per sprite)
		int offset = index * 4 * VERTEX_SIZE;

		Vector4f color = sprite.getColorVector();
		Vector2f[] textureCoordinates = sprite.getTexCoords();
		int textureID = 0;
		if (sprite.getTexture() != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == sprite.getTexture()) {
					textureID = i + 1;
					break;
				}
			}
		}

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
			vertices[offset] = spr.position.x + (xAdd * spr.scale.x);
			vertices[offset + 1] = spr.position.y + (yAdd * spr.scale.y);

			// Load color
			vertices[offset + 2] = color.x; // Red
			vertices[offset + 3] = color.y; // Green
			vertices[offset + 4] = color.z; // Blue
			vertices[offset + 5] = color.w; // Alpha

			// Load texture coordinates
			vertices[offset + 6] = textureCoordinates[i].x;
			vertices[offset + 7] = textureCoordinates[i].y;

			// Load texture ID
			vertices[offset + 8] = textureID;

			offset += VERTEX_SIZE;
		}
	}

	private int[] generateIndices() {
		// 6 indices/quad (3/triangle)
		int[] elements = new int[6 * maxBatchSize];
		for (int i = 0; i < maxBatchSize; i++) {
			loadElementIndices(elements, i);
		}

		return elements;
	}

	private void loadElementIndices(int[] elements, int i) {
		int offsetArrayIndex = 6 * i;
		int offset = 4 * i;

		// 3, 2, 0, 0, 2, 1, 7, 6, 4, 4, 6, 5

		// Triangle 1
		elements[offsetArrayIndex] = offset + 3;
		elements[offsetArrayIndex + 1] = offset + 2;
		elements[offsetArrayIndex + 2] = offset + 0;

		// Triangle 2
		elements[offsetArrayIndex + 3] = offset + 0;
		elements[offsetArrayIndex + 4] = offset + 2;
		elements[offsetArrayIndex + 5] = offset + 1;

	}

	public boolean hasRoomLeft() {
		return hasRoomLeft;
	}

	public boolean hasTextureRoom() {
		return this.textures.size() < 8;
	}

	public boolean hasTexture (Texture tex) {
		return this.textures.contains(tex);
	}

	public int zIndex () {
		return zIndex;
	}

	@Override
	public int compareTo(RenderBatch a) {
		return Integer.compare(this.zIndex, a.zIndex);
	}
}
