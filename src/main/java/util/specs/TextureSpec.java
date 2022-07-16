package util.specs;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL44.GL_MIRROR_CLAMP_TO_EDGE;

/**
 * Specification for the framebuffer's attachment textures
 *
 * @author VoxelRifts
 */
public class TextureSpec {
    public final TextureFormat format;
    public final TextureResizeFilterType minificationFilter;
    public final TextureResizeFilterType magnificationFilter;
    public final TextureWrapFilterType rFilter;
    public final TextureWrapFilterType sFilter;
    public final TextureWrapFilterType tFilter;

    // Beyond this, just some overloaded constructors for easy Spec construction
    public TextureSpec() {
        this(TextureFormat.NONE);
    }

    public TextureSpec(TextureFormat format) {
        this(format, TextureResizeFilterType.LINEAR, TextureResizeFilterType.LINEAR);
    }

    public TextureSpec(TextureFormat format, TextureResizeFilterType minificationFilter, TextureResizeFilterType magnificationFilter) {
        this(format, minificationFilter, magnificationFilter, TextureWrapFilterType.CLAMP_TO_EDGE);
    }

    public TextureSpec(TextureFormat format, TextureResizeFilterType minificationFilter, TextureResizeFilterType magnificationFilter, TextureWrapFilterType wrapFilters) {
        this(format, minificationFilter, magnificationFilter, wrapFilters, wrapFilters, wrapFilters);
    }

    public TextureSpec(TextureFormat format, TextureResizeFilterType minificationFilter, TextureResizeFilterType magnificationFilter, TextureWrapFilterType rFilter, TextureWrapFilterType sFilter, TextureWrapFilterType tFilter) {
        this.format = format;
        this.minificationFilter = minificationFilter;
        this.magnificationFilter = changeForMagnification(magnificationFilter);
        this.rFilter = rFilter;
        this.sFilter = sFilter;
        this.tFilter = tFilter;
    }

    private static TextureResizeFilterType changeForMagnification(TextureResizeFilterType t) {
        if (t.appliesToMagFilter) return t;
        else return TextureResizeFilterType.LINEAR;
    }

    /**
     * What type of data is to be stored in the attachment
     */
    public enum TextureFormat {
        // None is an invalid state
        NONE(false, GL_NONE, GL_NONE, GL_UNSIGNED_BYTE),
        RGBA8(false, GL_RGBA8, GL_RGBA, GL_UNSIGNED_BYTE),
        RED_INTEGER(false, GL_R32I, GL_RED_INTEGER, GL_UNSIGNED_BYTE),
        RED_UNSIGNED_INTEGER(false, GL_R32UI, GL_RED_INTEGER, GL_UNSIGNED_BYTE),

		/* GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT are not really internal format and format but its the best
					way (in my opinion) to add the depth formats to this enum and not make it look hideous            */

        DEPTH(true, GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT, GL_UNSIGNED_BYTE),
        DEPTH24STENCIL8(true, GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT, GL_UNSIGNED_BYTE);

        public boolean isDepth;
        public int internalFormat;
        public int format;
        public int datatype;

        /**
         * A basic property constructor
         *
         * @param isDepth        boolean: is this type applicable for depth attachments
         * @param internalFormat int: format for the storage of data in memory
         * @param format         int: format for the access of data from memory
         */
        TextureFormat(boolean isDepth, int internalFormat, int format, int datatype) {
            this.isDepth = isDepth;
            this.internalFormat = internalFormat;
            this.format = format;
            this.datatype = datatype;
        }
    }

    /**
     * How will the texture be resized if it has to be rendered to a bigger area or smaller area
     */
    public enum TextureResizeFilterType {
        LINEAR(true, GL_LINEAR),
        NEAREST(true, GL_NEAREST),
        NEAREST_MIPMAP_NEAREST(false, GL_NEAREST_MIPMAP_NEAREST),
        NEAREST_MIPMAP_LINEAR(false, GL_NEAREST_MIPMAP_LINEAR),
        LINEAR_MIPMAP_NEAREST(false, GL_LINEAR_MIPMAP_NEAREST),
        LINEAR_MIPMAP_LINEAR(false, GL_LINEAR_MIPMAP_LINEAR);

        public final boolean appliesToMagFilter;
        public final int glType;

        /**
         * A basic property constructor
         *
         * @param appliesToMagFilter boolean: is this type applicable for magnification filters
         * @param glType             int: the type that OpenGL wants.
         */
        TextureResizeFilterType(boolean appliesToMagFilter, int glType) {
            this.appliesToMagFilter = appliesToMagFilter;
            this.glType = glType;
        }
    }

    /**
     * How will the texture be wrapped
     */
    public enum TextureWrapFilterType {
        CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
        MIRROR_CLAMP_TO_EDGE(GL_MIRROR_CLAMP_TO_EDGE),
        CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
        REPEAT(GL_REPEAT),
        MIRRORED_REPEAT(GL_MIRRORED_REPEAT);

        public final int glType;

        /**
         * A basic property constructor
         *
         * @param glType int: the type that OpenGL wants.
         */
        TextureWrapFilterType(int glType) {
            this.glType = glType;
        }
    }
}
