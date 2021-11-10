package ui.component;

import input.Mouse;
import ui.UIComponent;

import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class Button extends UIComponent implements TextHolder {

    private String text;

    private Consumer<Integer> onClickAction;

    public Button(String label) {
        this.text = label;
        this.onClickAction = t -> {
        };
    }

    public void addActionOnClick(Consumer<Integer> run) {
        onClickAction = onClickAction.andThen(run);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void update() {
        super.update();
        if (isMouseOnThis()) {
            for (int i = 0; i < Mouse.mouseButton.length; i++) {
                if (Mouse.mouseButton[i]) {
                    onClickAction.accept(i);
                }
            }
        }
    }

    @Override
    public void draw() {

    }

}