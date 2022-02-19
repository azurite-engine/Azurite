package ui;

import org.joml.Vector2f;
import org.joml.Vector4f;
import util.MathUtils;
import util.Observable;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Frame extends Observable<Void> {

    private final Observable<Float> x, y;
    private final Observable<Float> w, h;

    public Frame() {
        super();
        this.x = new Observable<>(0.0f);
        this.y = new Observable<>(0.0f);
        this.h = new Observable<>(0.0f);
        this.w = new Observable<>(0.0f);
    }

    public Frame(float x, float y, float w, float h) {
        super();
        this.x = new Observable<>(x);
        this.y = new Observable<>(y);
        this.w = new Observable<>(w);
        this.h = new Observable<>(h);
    }

    public void set(float x, float y, float w, float h) {
        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
    }

    public float getX() {
        return x.getValue();
    }

    public void setX(float x) {
        this.x.setValue(x);
    }

    public float getY() {
        return y.getValue();
    }

    public void setY(float y) {
        this.y.setValue(y);
    }

    public Vector2f getPosition () {
        return new Vector2f(x.getValue(), y.getValue());
    }

    public Vector2f getScale () {
        return new Vector2f(w.getValue(), h.getValue());
    }

    public float getWidth() {
        return w.getValue();
    }

    public void setWidth(float w) {
        this.w.setValue(w);
    }

    public float getHeight() {
        return h.getValue();
    }

    public void setHeight(float h) {
        this.h.setValue(h);
    }

    public Vector4f getAsVector() {
        return new Vector4f(x.getValue(), y.getValue(), w.getValue(), h.getValue());
    }

    public void ensureEnclosure(Frame parent) {
        //by default, it will move components away from the edges and only shrink them, if they rly dont fit into it.
        if (this.w.getValue() > parent.w.getValue()) {
            //shrink width to parent width
            this.w.setValue(parent.w.getValue());
            this.x.setValue(0.0f);
        }
        if (this.h.getValue() > parent.h.getValue()) {
            //shrink height to parent height
            this.h.setValue(parent.h.getValue());
            this.y.setValue(0.0f);
        }
        if (this.w.getValue() + x.getValue() > parent.w.getValue()) {
            //move x to the left until it fits into it
            this.x.setValue(parent.w.getValue() - this.w.getValue());
        }
        if (this.h.getValue() + y.getValue() > parent.h.getValue()) {
            //move y to the top until it fits into it
            this.y.setValue(parent.h.getValue() - this.h.getValue());
        }
    }

    public Frame getSubFrame(Alignment alignment) {
        switch (alignment) {
            case TOP:
                return new Frame(x.getValue(), y.getValue(), w.getValue(), h.getValue() / 2);
            case LEFT:
                return new Frame(x.getValue(), y.getValue(), w.getValue() / 2, h.getValue());
            case RIGHT:
                return new Frame(x.getValue() + (w.getValue() / 2), y.getValue(), w.getValue() / 2, h.getValue());
            case BOTTOM:
                return new Frame(x.getValue(), y.getValue() + (h.getValue() / 2), w.getValue(), h.getValue() / 2);
        }
        return new Frame(x.getValue(), y.getValue(), w.getValue(), h.getValue());
    }

    public boolean isInFrame(Vector2f coords) {
        return MathUtils.inRect(coords, this.x.getValue(), this.y.getValue(), this.w.getValue(), this.h.getValue());
    }

    @Override
    public String toString() {
        return "UIFrame{" +
                "x=" + x.getValue() +
                ", y=" + y.getValue() +
                ", w=" + w.getValue() +
                ", h=" + h.getValue() +
                '}';
    }
}