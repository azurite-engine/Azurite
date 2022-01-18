package ecs;

import org.joml.Vector2f;
import util.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Tweening with easing in and out
 * for smooth movement from point to point.
 *
 * Great for moving platforms or moving AI to exact spot for desired time.
 * Also great for changing values of any property really... including colors and size
 * @author Luka "GoldSpark" Kolic
 */
public class Tween extends Component{

    public enum TweenMode{
        NO_EASING,
        EASING_IN,
        EASING_OUT,
        EASING_IN_OUT
    }



    private class TweenData{
        public Vector2f value;
        public Vector2f startPos;
        public Vector2f target;
        public float t = 0.0f;
        public float duration = 1.0f;
        public TweenMode tweenMode = TweenMode.NO_EASING;
        public boolean finished = false;
        public boolean tweenPosition = true;
    }


    private final List<TweenData> tweens = new ArrayList<>();
    //What tween are we currently processing
    private int index = 0;

    //Difference between start and end vector
    private Vector2f change = new Vector2f();
    private boolean startTween = false;
    private boolean allTweensFinished = false;



    @Override
    public void start() {

    }


    @Override
    public void update(float dt) {
        if(startTween) {
            if(tweens.get(index).tweenPosition) {
                tweenPosition();
            }else{
                tween();
            }
        }
    }


    /**
     * All the tween formulas ar being done in this function for every tween in the list
     */
    private void tween(){
        //Process current tween from the list
        TweenData data = tweens.get(index);
        //object = data.value;


        //Holding
        data.t += Engine.deltaTime();
        //Used to calculate time separate from data.t time for easing in and out
        float time = data.t;

        if(data.t < data.duration)
        {
            change.x = data.target.x - data.startPos.x;
            change.y = data.target.y - data.startPos.y;


            switch (data.tweenMode){
                case EASING_IN:

                    data.value.x = change.x *  (time /= data.duration) * time + data.startPos.x;
                    data.value.y = change.y *  (time) * time + data.startPos.y;

                    break;
                case NO_EASING:

                    data.value.x = change.x *  time / data.duration + data.startPos.x;
                    data.value.y = change.y *  time / data.duration + data.startPos.y;

                    break;
                case EASING_OUT:

                    data.value.x = -change.x * (time /= data.duration) * (time - 2) + data.startPos.x;
                    data.value.y = -change.y * (time) * (time - 2) + data.startPos.y;

                    break;
                case EASING_IN_OUT:

                    if((time /= data.duration / 2) < 1) {
                        data.value.x = change.x / 2 * time * time + data.startPos.x;
                        data.value.y = change.y / 2 * time * time + data.startPos.y;
                    }else{
                        data.value.x = -change.x / 2 * ((--time)*(time-2) - 1) + data.startPos.x;
                        data.value.y = -change.y / 2 * ((time)*(time-2) - 1) + data.startPos.y;
                    }

                    break;
                default:
                    break;
            }

        }else{

            data.value.x = data.target.x;
            data.value.y = data.target.y;



            //Mark this tween to be finished
            data.finished = true;
            index++;

            if(index >= tweens.size()) {
                startTween = false;
                tweens.clear();
                index = 0;
                allTweensFinished = true;
            }

        }
    }

    /**
     * All the tween formulas ar being done in this function for every tween in the list
     * This must be separate because GameObject's position is a primitive (why?)
     */
    private void tweenPosition(){
        //Process current tween from the list
        TweenData data = tweens.get(index);



        //Holding
        data.t += Engine.deltaTime();
        //Used to calculate time separate from data.t time for easing in and out
        float time = data.t;

        if(data.t < data.duration)
        {
            change.x = data.target.x - data.startPos.x;
            change.y = data.target.y - data.startPos.y;


            switch (data.tweenMode){
                case EASING_IN:

                    data.value.x = change.x *  (time /= data.duration) * time + data.startPos.x;
                    data.value.y = change.y *  (time) * time + data.startPos.y;


                    break;
                case NO_EASING:

                    data.value.x = change.x *  time / data.duration + data.startPos.x;
                    data.value.y = change.y *  time / data.duration + data.startPos.y;

                    break;
                case EASING_OUT:

                    data.value.x = -change.x * (time /= data.duration) * (time - 2) + data.startPos.x;
                    data.value.y = -change.y * (time) * (time - 2) + data.startPos.y;

                    break;
                case EASING_IN_OUT:

                    if((time /= data.duration / 2) < 1) {
                        data.value.x = change.x / 2 * time * time + data.startPos.x;
                        data.value.y = change.y / 2 * time * time + data.startPos.y;
                    }else{
                        data.value.x = -change.x / 2 * ((--time)*(time-2) - 1) + data.startPos.x;
                        data.value.y = -change.y / 2 * ((time)*(time-2) - 1) + data.startPos.y;
                    }

                    break;
                default:
                    break;
            }

            setPosition(data.value);

        }else{

            data.value.x = data.target.x;
            data.value.y = data.target.y;
            setPosition(data.value);


            //Mark this tween to be finished
            data.finished = true;
            index++;

            if(index >= tweens.size()) {
                startTween = false;
                tweens.clear();
                index = 0;
                allTweensFinished = true;
            }

        }
    }

    /**
     * Smoothly changes object's value from startPos to target
     * @param object Value to change using this tween. This can be Transform of an object like position or size or anything really that is Vector2f for now
     * @param startPos Starting position of the object
     * @param target Ending destination
     * @param duration How long will it take for the object to reach its destination in seconds
     * @param tweenMode Minor movement effects of the object
     */
    public void setUpTweenObject(Vector2f object, Vector2f startPos, Vector2f target, float duration, TweenMode tweenMode)
    {
        if(!startTween) {
            TweenData data = new TweenData();

            data.value = object;
            data.startPos  = new Vector2f(startPos);
            data.target    = new Vector2f(target);
            data.duration  = duration;
            data.tweenMode = tweenMode;
            data.t = 0.0f;
            data.tweenPosition = false;

            tweens.add(data);



        }
    }

    /**
     * Smoothly moves object from one position to another in desired time
     * @param startPos Starting position of the object
     * @param target Ending destination
     * @param duration How long will it take for the object to reach its destination in seconds
     * @param tweenMode Minor movement effects of the object
     */
    public void setUpTweenPosition( Vector2f startPos, Vector2f target, float duration, TweenMode tweenMode)
    {
        if(!startTween) {
            TweenData data = new TweenData();

            data.value = position();
            data.startPos  = new Vector2f(startPos);
            data.target    = new Vector2f(target);
            data.duration  = duration;
            data.tweenMode = tweenMode;
            data.t = 0.0f;
            data.tweenPosition = true;

            tweens.add(data);



        }
    }

    /**
     * After setting up all of the animations it will play them
     * NOTE! - MUST USE setUpTween function first!
     */
    public void play()
    {
        if(!startTween) {
            allTweensFinished = false;
            startTween = true;
        }
    }


    /**
     * Checks whether all of the tweens in the list have finished
     * @return true if all tweens finished
     */
    public boolean tweenFinishedAll()
    {
        return allTweensFinished;
    }


    /**
     * Checks whether current tween that is being processed has finished
     * @return true if tween finished
     */
    public boolean tweenFinished()
    {
        if(!tweens.isEmpty()){
            if(index > 0) {
                return tweens.get(index - 1).finished;
            }
        }

        return false;
    }

}
