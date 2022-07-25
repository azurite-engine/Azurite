/*
 * Some of this code was modified from a Font loader by:
 * Heiko Brumme
 * Copyright © 2015-2017, MIT License (MIT)
 */
package ui.fonts;

import graphics.Texture;
import org.lwjgl.BufferUtils;
import util.Log;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.*;

/**
 * This class contains a font texture for drawing text.
 * A lot of credit for the base of this file goes to @SilverTiger on GitHub, his base has been heavily modified to work in Azurite.
 *
 * @author Heiko Brumme (SilverTiger) {@link <a href="https://github.com/SilverTiger/lwjgl3-tutorial">...</a>}
 * @author Asher Haun
 */
public class Font {

    /**
     * Contains the glyphs for each char.
     */
    private final Map<Character, Glyph> glyphs;
    /**
     * Contains the font texture.
     */
    private final Texture texture;

    /**
     * Height of the font.
     */
    private int fontHeight;

    /**
     * Creates a default anti-aliased font with monospaced glyphs and default
     * size 16.
     */
    public Font() {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), true);
    }

    /**
     * Creates a default font with monospaced glyphs and default size 16.
     *
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, 16), antiAlias);
    }

    /**
     * Creates a default anti-aliased font with monospaced glyphs and specified
     * size.
     *
     * @param size Font size
     */
    public Font(int size) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), true);
    }

    /**
     * Creates a default font with monospaced glyphs and specified size.
     *
     * @param size      Font size
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(int size, boolean antiAlias) {
        this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
    }

    /**
     * Creates an anti-aliased Font from an input stream.
     *
     * @param in   The input stream
     * @param size Font size
     * @throws FontFormatException if fontFile does not contain the required
     *                             font tables for the specified format
     * @throws IOException         If font can't be read
     */
    public Font(InputStream in, int size) throws FontFormatException, IOException {
        this(in, size, true);
    }

    /**
     * Creates a Font from an input stream.
     *
     * @param in        The input stream
     * @param size      Font size
     * @param antiAlias Whether the font should be anti-aliased or not
     * @throws FontFormatException if fontFile does not contain the required
     *                             font tables for the specified format
     * @throws IOException         If font can't be read
     */
    public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
        this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
    }

    /**
     * Creates a Font from an input stream.
     *
     * @param path      Path to TTF file
     * @param size      Font size
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(String path, float size, boolean antiAlias) {
        java.awt.Font f = null;
        try {
            java.awt.Font fontRaw = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(path));
            f = fontRaw.deriveFont(size);
        } catch (Exception e) {
            Log.warn("could not load font " + path + ", using default monospaced font.", 1);
            e.printStackTrace();
        }

        glyphs = new HashMap<>();
        texture = createFontTexture(f, antiAlias);
    }

    /**
     * Creates an anti-aliased font from an AWT Font.
     *
     * @param font The AWT Font
     */
    public Font(java.awt.Font font) {
        this(font, true);
    }

    /**
     * Creates a font from an AWT Font.
     *
     * @param font      The AWT Font
     * @param antiAlias Whether the font should be anti-aliased or not
     */
    public Font(java.awt.Font font, boolean antiAlias) {
        glyphs = new HashMap<>();
        texture = createFontTexture(font, antiAlias);
    }

    /**
     * Creates a font texture from specified AWT font.
     *
     * @param font      The AWT font
     * @param antiAlias Whether the font should be anti-aliased or not
     * @return Font texture
     */
    private Texture createFontTexture(java.awt.Font font, boolean antiAlias) {
        // Loop through the characters to get charWidth and charHeight
        int imageWidth = 0;
        int imageHeight = 0;

        // Start at char #32, because ASCII 0 to 31 are just control codes
        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                // ASCII 127 is the DEL control code, so we can skip it
                continue;
            }
            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);
            if (ch == null) {
                // If char image is null that font does not contain the char
                continue;
            }

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        fontHeight = imageHeight;

        // Image for the texture
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        int x = 0;

        // Create image for the standard chars, omitting ASCII 0 to 31 because they are just control codes.
        for (int i = 32; i < 256; i++) {

            // ASCII 127 is the DEL control code, so we can skip it
            if (i == 127) continue;

            char c = (char) i;

            BufferedImage charImage = createCharImage(font, c, antiAlias);

            // If char image is null that font does not contain the char
            if (charImage == null) continue;

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            // Create glyph and draw char on image
            Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight);
            g.drawImage(charImage, x, 0, null);
            x += ch.width;
            glyphs.put(c, ch);
        }

        Texture finalTexture = bufferedImageToTexture(image);

        // Finally, calculate the UV coordinates on the generated texture and store it in each Glyph
        for (int i = 32; i < 256; i++) {
            if (glyphs.get((char) i) != null) {
                glyphs.get((char) i).calculateUVs(finalTexture);
            }
        }

        return finalTexture;
    }

    private Texture bufferedImageToTexture(BufferedImage image) {
        // Flip image Horizontally
        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        // Get charWidth and charHeight of image
        int width = image.getWidth();
        int height = image.getHeight();

        // Get pixel data of image
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // Load the pixel data into a ByteBuffer
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Pixel as RGBA: 0xAARRGGBB
                int pixel = pixels[i * width + j];
                // Red component 0xAARRGGBB >> 16 = 0x0000AARR
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Green component 0xAARRGGBB >> 8 = 0x00AARRGG
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB
                buffer.put((byte) (pixel & 0xFF));
                // Alpha component 0xAARRGGBB >> 24 = 0x000000AA
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        return new Texture().createTexture(width, height, buffer);
    }

    /**
     * Creates a char image from specified AWT font and char.
     *
     * @param font      The AWT font
     * @param c         The char
     * @param antiAlias Whether the char should be anti-aliased or not
     * @return Char image
     */
    private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
        // Creating temporary image to extract character size
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        // Get char charWidth and charHeight
        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        // Check if charWidth is 0
        if (charWidth == 0) {
            return null;
        }

        // Create image for holding the char
        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    /**
     * Gets the height of the specified text.
     *
     * @param text The text
     * @return Height in pixels of the text.
     */
    public int getHeight(CharSequence text) {
        int height = 0;
        int lineHeight = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                // Line end, add line height to stored height
                height += lineHeight;
                lineHeight = 0;
                continue;
            }

            if (c == '\r') continue;

            Glyph g = glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height);
        }
        height += lineHeight;
        return height;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public Map<Character, Glyph> getGlyphs() {
        return glyphs;
    }

    public Texture getTexture() {
        return texture;
    }
}
