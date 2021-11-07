package ui;

import org.joml.Vector4f;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class UIFrame {

    private float x, y;
    private float w, h;

    public UIFrame() {
        this.x = 0;
        this.y = 0;
        this.h = 0;
        this.w = 0;
    }

    public UIFrame(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return w;
    }

    public void setWidth(float w) {
        this.w = w;
    }

    public float getHeight() {
        return h;
    }

    public void setHeight(float h) {
        this.h = h;
    }

    public Vector4f getAsVector() {
        return new Vector4f(x, y, w, h);
    }

    public UIFrame getSubFrame(UIAlignment alignment) {
        switch (alignment) {
            case TOP:
                return new UIFrame(x, y, w, h / 2);
            case LEFT:
                return new UIFrame(x, y, w / 2, h);
            case RIGHT:
                return new UIFrame(x + (w / 2), y, w / 2, h);
            case BOTTOM:
                return new UIFrame(x, y + (h / 2), w, h / 2);
        }
        return new UIFrame(x, y, w, h);
    }

}