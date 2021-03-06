package graphics;

import org.lwjgl.opengl.GL11;
import util.specs.FramebufferSpec;
import util.specs.FramebufferTextureSpec;
import util.specs.FramebufferTextureSpec.FramebufferTextureFormat;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL45.*;

/**
 * This class is heavily inspired from TheCherno's Hazel Engine's Framebuffer API
 *
 * An encapsulation of an OpenGL Framebuffer
 *
 * @author VoxelRifts
 */
public class Framebuffer {
	/** the framebuffer handle */
	private int id = -1;
	private int width;
	private int height;
	/** Specification of the framebuffer's attachments */
	private FramebufferSpec spec;

	/** The color attachment textures' specifications */
	private List<FramebufferTextureSpec> colorAttachmentSpecs;
	/** The depth attachment texture's specification. Initialized to an Invalid default */
	private FramebufferTextureSpec depthAttachmentSpec = new FramebufferTextureSpec();

	/** Color attachment textures to which the framebuffer renders to */
	private List<Integer> colorAttachmentTextures;
	/** Depth attachment texture to which the framebuffer renders to */
	private int depthAttachmentTexture;

	/** Static list maintaining all framebuffers so as to delete them all in the end */
	private static final List<Framebuffer> fbos = new ArrayList<>();

	/**
	 * Default Framebuffer constructor
	 * @param width int: width of the fbo
	 * @param height int: height of the fbo
	 * @param spec FramebufferSpec: Specification of the framebuffer
	 */
	public Framebuffer(int width, int height, FramebufferSpec spec) {
		this.width = width;
		this.height = height;
		this.spec = spec;

		this.colorAttachmentSpecs = new ArrayList<>();
		this.colorAttachmentTextures = new ArrayList<>();

		// Sort the texture formats.
		for (FramebufferTextureSpec format : spec.attachments) {
			if (!format.format.isDepth) {
				colorAttachmentSpecs.add(format);
			} else {
				depthAttachmentSpec = format;
			}
		}
		// Generate the framebuffer
		invalidate();

		fbos.add(this);
	}

	/**
	 * Constructor to encapsulate an already existing FBO.
	 * Be careful though many of the methods here wont work.
	 * @param id int: id to encapsulate in the instance
	 */
	private Framebuffer(int id) {
		this.id = id;
	}

	/**
	 * Factory method to create an instance that manages the default framebuffer
	 * @return Framebuffer
	 */
	public static Framebuffer createDefault() {
		return new Framebuffer(0);
	}

	public int fetchColorAttachment(int i) {
		if (colorAttachmentTextures.size() >= i)
			return colorAttachmentTextures.get(i);
		return -1;
	}

	public int fetchDepthAttachment() {
		return depthAttachmentTexture;
	}

	/**
	 * Copies all data from the color texture attachments of this framebuffer to the texture attachments
	 * 				of the default Framebuffer. OpenGL provides a function, glBlitFramebuffer for this
	 */
	public void blitColorBuffersToScreen() {
		glBlitNamedFramebuffer(this.id, 0,
				0, 0, width, height,
				0, 0, Window.getWidth(), Window.getHeight(),
				GL_COLOR_BUFFER_BIT, GL_LINEAR);
	}

	/**
	 * Copies all data from the ALL texture attachments of this framebuffer to the texture attachments
	 * 				of the default Framebuffer. OpenGL provides a function, glBlitFramebuffer for this
	 */
	public void blitEntireFboToScreen() {
		glBlitNamedFramebuffer(this.id, 0,
				0, 0, width, height,
				0, 0, Window.getWidth(), Window.getHeight(),
				GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT, GL_LINEAR);
	}

	/**
	 * Deletes the framebuffer if it was already created.
	 * Then it creates a brand new framebuffer and adds new texture attachments to it based on the spec
	 */
	private void invalidate() {
		if (this.id != -1)
			delete();

		this.id = glCreateFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, this.id);

		// If there are any color attachments requested, create them all
		if (colorAttachmentSpecs.size() > 0) {
			colorAttachmentTextures.clear();

			for (int i = 0; i < colorAttachmentSpecs.size(); i++) {
				FramebufferTextureSpec format = colorAttachmentSpecs.get(i);
				int texture = createColorTexture(this.width, this.height, format.format.internalFormat, format.format.format, GL_UNSIGNED_BYTE);
				colorAttachmentTextures.add(texture);

				// Set the Texture's resizing and wrap parameters as per the specification
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, format.minificationFilter.glType);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, format.magnificationFilter.glType);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, format.rFilter.glType);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, format.sFilter.glType);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, format.tFilter.glType);

				glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, id, 0);
			}
		}

		// If the depth attachment spec is not the default
		if (depthAttachmentSpec.format.isDepth) {
			// Generate the depth texture
			depthAttachmentTexture = createDepthTexture(this.width, this.height, depthAttachmentSpec.format.internalFormat);

			// Set the Texture's resizing and wrap parameters as per the specification
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, depthAttachmentSpec.minificationFilter.glType);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, depthAttachmentSpec.magnificationFilter.glType);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, depthAttachmentSpec.rFilter.glType);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, depthAttachmentSpec.sFilter.glType);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, depthAttachmentSpec.tFilter.glType);

			glFramebufferTexture2D(GL_FRAMEBUFFER, depthAttachmentSpec.format.format, GL_TEXTURE_2D, id, 0);
		}

		// Check if the framebuffer is complete
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Incomplete Framebuffer :(");
			System.exit(-1);
		}

		// Unbind this fbo
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	/**
	 * Resize the Framebuffer to a specific size
	 * it will regenerate the entire framebuffer
	 *
	 * @param width int: new width
	 * @param height int new height
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		invalidate();
	}

	/**
	 * Creates and binds a texture. Then, allocates memory for it.
	 *
	 * @param width int: width of the texture
	 * @param height int: height of the texture
	 * @param internalFormat int: format for the storage of data in memory
	 * @param format int: format for the access of data from memory
	 * @param type int: type in which data is stored
	 * @return the created texture's id
	 */
	private static int createColorTexture(int width, int height, int internalFormat, int format, int type) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);

		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, 0);

		return texture;
	}

	/**
	 * Creates and binds a texture. Then, allocates memory for it.
	 *
	 * @param width int: width of the texture
	 * @param height int: height of the texture
	 * @param internalFormat int: format for the storage of data in memory
	 * @return the created texture's id
	 */
	private static int createDepthTexture(int width, int height, int internalFormat) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);

		glTexStorage2D(GL_TEXTURE_2D, 1, internalFormat, width, height);

		return texture;
	}

	/**
	 * Binds the framebuffer
	 */
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, this.id);
	}

	/**
	 * Unbinds the framebuffer i.e. binds the default framebuffer
	 */
	public static void unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	/**
	 * Deletes the texture attachments and the framebuffer
	 */
	public void delete() {
		colorAttachmentTextures.forEach(GL11::glDeleteTextures);
		glDeleteTextures(depthAttachmentTexture);
		glDeleteFramebuffers(this.id);
	}

	/**
	 * Deletes all Framebuffers
	 *
	 * Called after the X is pressed
	 */
	public static void clean() {
		fbos.forEach(Framebuffer::delete);
	}
}
