package fonts;

import ecs.GameObject;
import ecs.Sprite;
import ecs.SpriteRenderer;
import graphics.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import physics.Transform;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public class AFont {

    private String filepath;
    private int fontSize;

    private int width, height, lineHeight;
    private Map<Integer, CharInfo> characterMap;

    public int textureId;

    public AFont(String filepath, int fontSize) {
        this.filepath = filepath;
        this.fontSize = fontSize;
        this.characterMap = new HashMap<>();
        generateBitmap();
    }

    public CharInfo getCharacter(int codepoint) {
        return characterMap.getOrDefault(codepoint, new CharInfo(0, 0, 0, 0));
    }

    public void generateBitmap() {
        Font font = new Font(filepath, Font.PLAIN, fontSize);

        // Create fake image to get font information
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int estimatedWidth = (int)Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1;
        width = 0;
        height = fontMetrics.getHeight();
        lineHeight = fontMetrics.getHeight();
        int x = 0;
        int y = (int)(fontMetrics.getHeight() * 1.4f);

        for (int i=0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) {
                // Get the sizes for each codepoint glyph, and update the actual image width and height
                CharInfo charInfo = new CharInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                characterMap.put(i, charInfo);
                width = Math.max(x + fontMetrics.charWidth(i), width);

                x += charInfo.width;
                if (x > estimatedWidth) {
                    x = 0;
                    y += fontMetrics.getHeight() * 1.4f;
                    height += fontMetrics.getHeight() * 1.4f;
                }
            }
        }
        height += fontMetrics.getHeight() * 1.4f;
        g2d.dispose();

        // Create the real texture
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        for (int i=0; i < font.getNumGlyphs(); i++) {
            if (font.canDisplay(i)) {
                CharInfo info = characterMap.get(i);
                info.calculateTextureCoordinates(width, height);
                g2d.drawString("" + (char)i, info.sourceX, info.sourceY);
            }
        }
        g2d.dispose();

        uploadTexture(img);
    }

    private void uploadTexture(BufferedImage image) {
        // Taken from https://stackoverflow.com/questions/10801016/lwjgl-textures-and-strings

        int[] pixels = new int[image.getHeight() * image.getWidth()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for (int y=0; y < image.getHeight(); y++) {
            for (int x=0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                byte alphaComponent = (byte)((pixel >> 24) & 0xFF);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
                buffer.put(alphaComponent);
            }
        }
        buffer.flip();

        textureId = glGenTextures();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        buffer.clear();
    }

}
