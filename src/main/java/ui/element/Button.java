package ui.element;

import graphics.Color;
import graphics.Sprite;
import org.lwjgl.glfw.GLFW;
import ui.Frame;
import ui.RenderableElement;
import ui.Text;
import util.Engine;

/**
 * The Button class represents a graphical user interface (GUI) button that can
 * be rendered on a frame.
 * It extends the RenderableElement class and implements the TextHolder
 * interface, allowing it to display text on the button.
 * Buttons can be created with different label text, background colors, label
 * colors, and frames.
 * Buttons can also be updated to change their appearance based on mouse
 * interactions, such as hovering over the button.
 * 
 * Usage example:
 * 
 * <pre>
 * import ui.EventHandler.Event;
 * import ui.element.Button;
 * 
 * public class Main extends Scene {
 *     // ...
 * 
 *     Button btn;
 *     public void awake() {
 *         // ...
 *         btn = new Button("Click Me!", Color.RED, Color.WHITE, new Frame(20, 20, 200, 100));  
 *         btn.getEventHandler().registerListener(Event.MOUSE_CLICK, e -> {
 *             System.out.println("Button clicked!");
 *         });
 *     }
 *
 *     public void update() {
 *         // ...
 *     }
 * }
 * </pre>
 * 
 * The above example demonstrates how to create a new Button object with label
 * text "Click Me!", a red background color, white label color, and a frame with
 * coordinates (20, 20) and size (200, 100). It also registers a mouse click
 * event listener on the button, which will print "Button clicked!" to the
 * console when the button is clicked.
 * 
 * @author Juyas
 * @author Asher Haun
 * @version 07.11.2021
 * @since 15.5.2022
 */
public class Button extends RenderableElement implements TextHolder {

    private Text label;

    /**
     * 
     * Constructs a Button object with the specified label text, background color,
     * label color, and frame.
     * 
     * @param label           The text to be displayed on the button
     * @param backgroundColor The background color of the button
     * @param labelColor      The color of the label text on the button
     * @param frame           The frame in which the button will be rendered
     */
    public Button(String label, Color backgroundColor, Color labelColor, Frame frame) {
        super(backgroundColor, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;

        Engine.scenes().currentScene().addUIElement(this);
        Engine.scenes().currentScene().uiRenderer.add(this);
    }

    /**
     * 
     * Constructs a Button object with the specified label text, image path, label
     * color, and frame.
     * 
     * @param label      The text to be displayed on the button
     * @param path       The path to the image to be used as the background of the
     *                   button
     * @param labelColor The color of the label text on the button
     * @param frame      The frame in which the button will be rendered
     */
    public Button(String label, String path, Color labelColor, Frame frame) {
        super(path, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;

        Engine.scenes().currentScene().addUIElement(this);
        Engine.scenes().currentScene().uiRenderer.add(this);
    }

    /**
     * Constructs a Button object with the specified label text, sprite texture,
     * label color, and frame.
     * 
     * @param label      The text to be displayed on the button
     * @param texture    The sprite texture to be used as the background of the
     *                   button
     * @param labelColor The color of the label text on the button
     * @param frame      The frame on which the button will be rendered
     */
    public Button(String label, Sprite texture, Color labelColor, Frame frame) {
        super(texture, frame);
        this.label = new Text(label, labelColor, 0, 0);
        this.label.setCentered(true);
        this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;

        Engine.scenes().currentScene().addUIElement(this);
        Engine.scenes().currentScene().uiRenderer.add(this);
    }

    /**
     * Returns the text currently displayed on the button.
     * 
     * @return The text currently displayed on the button
     */
    @Override
    public String getText() {
        return label.getText();
    }

    /**
     * Sets the text to be displayed on the button.
     * 
     * @param text The text to be displayed on the button
     */
    @Override
    public void setText(String text) {
        this.label.change(text);
    }

    /**
     * Updates the appearance of the button based on mouse interactions, such as
     * hovering over the button. The label position is also updated to be centered
     * on the button. This is called by the engine and should not be called by the
     * user.
     */
    @Override
    public void update() {
        super.update();

        label.setPosition(getX() + frame.getWidth() / 2, getY() + frame.getHeight() / 2 - label.getHeight() / 2);

        if (isMouseOnThis()) {
            if (tintColor != null) {
                this.setColor(tintColor);
            }
        } else {
            this.setColor(defaultColor);
        }
    }
}