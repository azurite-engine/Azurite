package graphics;

import event.EventData;
import event.Events;
import util.Log;
import util.specs.FramebufferSpec;
import util.specs.TextureSpec;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL45.*;

/**
 * This class is heavily inspired from TheCherno's Hazel Engine's Framebuffer
 * API
 * <p>
 * An encapsulation of an OpenGL Framebuffer
 *
 * @author VoxelRifts
 */
public class Framebuffer {
    /**
     * ß
     * Static list maintaining all framebuffers so as to delete them all in the end
     */
    private static final List<Framebuffer> fbos = new ArrayList<>();
    /**
     * Register for all FBOs that are the size of the screen
     */
    private static final List<Framebuffer> screenSize = new ArrayList<>();
    /**
     * Register for all FBOs that are half the size of the screen
     */
    private static final List<Framebuffer> halfScreenSize = new ArrayList<>();

    static {
        Events.windowResizeEvent.subscribe(Framebuffer::resizeAll);
    }

    /**
     * the framebuffer handle
     */
    private int id = -1;
    private int width;
    private int height;
    /**
     * Specification of the framebuffer's attachments
     */
    private FramebufferSpec spec;
    /**
     * The color attachment textures' specifications
     */
    private List<TextureSpec> colorAttachmentSpecs;
    /**
     * The depth attachment texture's specification. Initialized to an Invalid
     * default
     */
    private TextureSpec depthAttachmentSpec = new TextureSpec();
    /**
     * Color attachment textures to which the framebuffer renders to
     */
    private List<Texture> colorAttachmentTextures;
    /**
     * Depth attachment texture to which the framebuffer renders to
     */
    private Texture depthAttachmentTexture;

    /**
     * Default Framebuffer constructor
     *
     * @param width  int: width of the fbo
     * @param height int: height of the fbo
     * @param spec   FramebufferSpec: Specification of the framebuffer
     */
    public Framebuffer(int width, int height, FramebufferSpec spec) {
        this.width = width;
        this.height = height;
        this.spec = spec;

        this.colorAttachmentSpecs = new ArrayList<>();
        this.colorAttachmentTextures = new ArrayList<>();

        // Sort the texture formats.
        for (TextureSpec format : spec.attachments) {
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
     * Constructor used to register the framebuffer for Auto-Resizing based on its
     * size
     *
     * @param width        width of the fbo
     * @param height       height of the fbo
     * @param spec         Specification of the framebuffer
     * @param isScreenSize Is the framebuffer the size of the screen
     * @param isHalfSize   Is the framebuffer half the size of the screen
     */
    private Framebuffer(int width, int height, FramebufferSpec spec, boolean isScreenSize, boolean isHalfSize) {
        this.width = width;
        this.height = height;
        this.spec = spec;

        this.colorAttachmentSpecs = new ArrayList<>();
        this.colorAttachmentTextures = new ArrayList<>();

        // Sort the texture formats.
        for (TextureSpec format : spec.attachments) {
            if (!format.format.isDepth) {
                colorAttachmentSpecs.add(format);
            } else {
                depthAttachmentSpec = format;
            }
        }
        if (isScreenSize)
            screenSize.add(this);
        if (isHalfSize)
            halfScreenSize.add(this);
        // Generate the framebuffer
        invalidate();

        fbos.add(this);
    }

    /**
     * Constructor to encapsulate an already existing FBO.
     * Be careful though many of the methods here wont work.
     *
     * @param id int: id to encapsulate in the instance
     */
    private Framebuffer(int id) {
        this.id = id;
    }

    /**
     * Factory method to create an instance that manages the default framebuffer
     *
     * @return Framebuffer
     */
    public static Framebuffer createDefault() {
        return new Framebuffer(0);
    }

    /**
     * Factory method to create an instance that has one simple color attachment
     *
     * @return Framebuffer
     */
    public static Framebuffer createWithColorAttachment() {
        return new Framebuffer(Window.getWidth(), Window.getHeight(),
                new FramebufferSpec(new TextureSpec(TextureSpec.TextureFormat.RGBA8)), true, false);
    }

    /**
     * Factory method to create an instance that is half the size of the screen and
     * has one simple color attachment
     *
     * @return Framebuffer
     */
    public static Framebuffer createHalfResWithColorAttachment() {
        return new Framebuffer(Window.getWidth() / 2, Window.getHeight() / 4,
                new FramebufferSpec(new TextureSpec(TextureSpec.TextureFormat.RGBA8)), false, true);
    }

    /**
     * Resize all Framebuffers created via the createWithColorAttachment() or
     * createHalfResWithColorAttachment() methods
     */

    public static void resizeAll(EventData.WindowResizeEventData data) {

        for (Framebuffer f : screenSize) {
            f.resize(data.x, data.y);
        }

        for (Framebuffer f : halfScreenSize) {
            f.resize(data.x / 2, data.y / 2);
        }

    }

    /**
     * Unbinds the framebuffer i.e. binds the default framebuffer
     */
    public static void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        // int[] w = new int[4];
        // int[] h = new int[4];
        // glfwGetFramebufferSize(Window.glfwWindow(), w, h);
        // int width = w[0];
        // int height = h[0];
        glViewport(Camera.instance.getViewportPosX(), Camera.instance.getViewportPosY(),
                (int) Camera.instance.getViewportSizeX(), (int) Camera.instance.getViewportSizeY());
    }

    /**
     * Deletes all Framebuffers
     * <p>
     * Called after the X is pressed
     */
    public static void clean() {
        fbos.forEach(Framebuffer::delete);
    }

    /**
     * Checks if the fbo is a wrapper around the default one.
     * Required because created wrapper has no attachments stored in
     * colorAttachmentTextures
     *
     * @return if the framebuffer instance is a wrapper around the
     */
    public boolean isDefault() {
        return id == 0;
    }

    /**
     * Get a color texture attachment id from the framebuffer
     *
     * @param i index of the texture attachment required
     * @return the color attachment texture id at the index
     */
    public Texture getColorAttachment(int i) {
        if (colorAttachmentTextures.size() >= i)
            return colorAttachmentTextures.get(i);
        return null;
    }

    /**
     * Get the depth attachment texture from this framebuffer
     *
     * @return the depth attachment texture id
     */
    public Texture getDepthAttachment() {
        return depthAttachmentTexture;
    }

    /**
     * Copies all data from the color texture attachments of this framebuffer to the
     * texture attachments
     * of the default Framebuffer. OpenGL provides a function, glBlitFramebuffer for
     * this
     */
    public void blitColorBuffersToScreen() {
        glBlitNamedFramebuffer(this.id, 0,
                0, 0, width, height,
                0, 0, Window.getWidth(), Window.getHeight(),
                GL_COLOR_BUFFER_BIT, GL_NEAREST);
    }

    /**
     * Copies all data from the ALL texture attachments of this framebuffer to the
     * texture attachments
     * of the default Framebuffer. OpenGL provides a function, glBlitFramebuffer for
     * this
     */
    public void blitEntireFboToScreen() {
        glBlitNamedFramebuffer(this.id, 0,
                0, 0, width, height,
                0, 0, Window.getWidth(), Window.getHeight(),
                GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT, GL_LINEAR);
    }

    /**
     * Deletes the framebuffer if it was already created.
     * Then it creates a brand new framebuffer and adds new texture attachments to
     * it based on the spec
     */
    private void invalidate() {
        if (this.id != -1)
            delete();

        this.id = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.id);

        // If there are any color attachments requested, create them all
        if (colorAttachmentSpecs.size() > 0) {
            colorAttachmentTextures.clear();

            for (int i = 0; i < colorAttachmentSpecs.size(); i++) {
                TextureSpec format = colorAttachmentSpecs.get(i);
                Texture texture = new Texture(width, height, format);
                colorAttachmentTextures.add(texture);
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, GL_TEXTURE_2D, texture.getTextureID(),
                        0);
            }
        }

        // If the depth attachment spec is not the default
        if (depthAttachmentSpec.format.isDepth) {
            // Generate the depth texture
            depthAttachmentTexture = new Texture(width, height, depthAttachmentSpec);
            glFramebufferTexture2D(GL_FRAMEBUFFER, depthAttachmentSpec.format.format, GL_TEXTURE_2D,
                    depthAttachmentTexture.getTextureID(), 0);
        }

        // Check if the framebuffer is complete
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            Log.fatal("incomplete framebuffer :(");
            System.exit(-1);
        }

        // Unbind this fbo
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Resize the Framebuffer to a specific size
     * it will regenerate the entire framebuffer
     *
     * @param width  int: new width
     * @param height int new height
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        invalidate();
    }

    /**
     * Binds the framebuffer
     */
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.id);
        if (!isDefault())
            glViewport(0, 0, this.width, this.height);
        else {
            // int[] w = new int[4];
            // int[] h = new int[4];
            // glfwGetFramebufferSize(Window.glfwWindow(), w, h);
            // int width = w[0];
            // int height = h[0];
            glViewport(Camera.instance.getViewportPosX(), Camera.instance.getViewportPosY(),
                    (int) Camera.instance.getViewportSizeX(), (int) Camera.instance.getViewportSizeY());
        }
    }

    /**
     * Deletes the texture attachments and the framebuffer
     */
    public void delete() {
        colorAttachmentTextures.forEach(Texture::delete);
        if (depthAttachmentTexture != null)
            depthAttachmentTexture.delete();
        glDeleteFramebuffers(this.id);
    }

    /**
     * Get the width of this Framebuffer
     *
     * @return width of the Framebuffer
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of this Framebuffer
     *
     * @return height of the Framebuffer
     */
    public int getHeight() {
        return height;
    }
}
