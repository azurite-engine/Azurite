package components;

import graphics.Sprite;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 26.07.2021
 * @since 26.07.2021
 */
public class SpriteAnimation extends Component {

    private SpriteRenderer renderer;

    private HashMap<String, List<Sprite>> animations;
    private Sprite baseImage;
    private int currentRepetition = 0;
    private int currentSprite = 0;
    private float timePerSprite;
    private String currentAnimation = null;

    private String nextAnimation = null;
    private int nextRepetitions = 0;

    private float timeOnSprite = 0;

    private Consumer<String> animationEndEvent = x -> {
    };

    public SpriteAnimation(SpriteRenderer renderer, Sprite baseImage, float timePerSprite) {
        this.renderer = renderer;
        this.animations = new HashMap<>();
        this.baseImage = baseImage;
        this.timePerSprite = timePerSprite;
        this.order = SpriteRenderer.ORDER - 1;
    }

    public void setAnimation(String name, List<Sprite> sprites) {
        this.animations.put(name, sprites);
    }

    public void setBaseImage(Sprite baseImage) {
        this.baseImage = baseImage;
    }

    public void setCurrentRepetition(int currentRepetition) {
        this.currentRepetition = currentRepetition;
    }

    public void setTimePerSprite(float timePerSprite) {
        this.timePerSprite = timePerSprite;
    }

    public void nextAnimation(String nextAnimation, int nextRepetitions) {
        this.nextAnimation = nextAnimation;
        this.nextRepetitions = nextRepetitions;
    }

    public void switchAnimation(boolean instant) {
        this.currentRepetition = 0;
        if (instant)
            this.currentSprite = animations.get(currentAnimation).size() - 1;
    }

    public void setAnimationEndEvent(Consumer<String> animationEndEvent) {
        if (animationEndEvent == null)
            animationEndEvent = x -> {
            };
        this.animationEndEvent = animationEndEvent;
    }

    @Override
    public void start() {
        renderer.setSprite(baseImage);
    }

    @Override
    public void update(float dt) {

        if (currentAnimation == null) {
            //there is not animation to be played
            if (nextAnimation == null) return;
            else {
                currentAnimation = nextAnimation;
                currentSprite = 0;
                currentRepetition = nextRepetitions;
            }
        }

        timeOnSprite += dt;

        List<Sprite> currentAnimations = animations.get(currentAnimation);
        boolean lastSprite = currentSprite == currentAnimations.size() - 1;
        //last sprite and no further repetitions
        if (currentRepetition == 0 && lastSprite) {
            //call animation end event
            animationEndEvent.accept(this.currentAnimation);
            //go next animation
            if (nextAnimation != null) {
                this.currentAnimation = nextAnimation;
                this.nextAnimation = null;
                this.currentSprite = 0;
                this.currentRepetition = nextRepetitions;
                this.nextRepetitions = 0;
                Sprite sprite = currentAnimations.get(currentSprite);
                renderer.setSprite(sprite);
            } else {
                //if there is no new animation, return to the baseImage
                this.currentAnimation = null;
                this.currentSprite = 0;
                this.currentRepetition = 0;
                renderer.setSprite(baseImage);
            }
        } else if (timeOnSprite >= timePerSprite) {
            //if time is up for the current sprite
            timeOnSprite = 0;
            currentSprite++;
            //next repetition
            if (currentSprite >= currentAnimations.size()) {
                if (currentRepetition > 0) currentRepetition--;
                currentSprite = 0;
            }
            Sprite sprite = currentAnimations.get(currentSprite);
            renderer.setSprite(sprite);
        }

    }

}