package ai.statemachine;

import java.util.function.Function;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
public class Transition {

    private final String toState;
    private final Function<State, Boolean> condition;

    public Transition(String toState, Function<State, Boolean> condition) {
        this.toState = toState;
        this.condition = condition;
    }

    public boolean isConditionMet(State currentState) {
        return condition.apply(currentState);
    }


    public String getToState() {
        return toState;
    }

}