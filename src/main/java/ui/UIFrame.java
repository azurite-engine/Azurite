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

    public void set(float x, float y, float w, float h) {
        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
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

    public void ensureEnclosure(UIFrame parent) {
        //by default, it will move components away from the edges and only shrink them, if they rly dont fit into it.
        if (this.w > parent.w) {
            //shrink width to parent width
            this.w = parent.w;
            this.x = 0;
        }
        if (this.h > parent.h) {
            //shrink height to parent height
            this.h = parent.h;
            this.y = 0;
            System.out.println("correct height");
        }
        if (this.w + x > parent.w) {
            //move x to the left until it fits into it
            this.x = parent.w - this.w;
        }
        if (this.h + y > parent.h) {
            //move y to the top until it fits into it
            this.y = parent.h - this.h;
        }
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

    @Override
    public String toString() {
        return "UIFrame{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}