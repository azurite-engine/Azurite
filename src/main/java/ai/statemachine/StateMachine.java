package ai.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
public class StateMachine {

    private final HashMap<String, State> states;
    private final HashMap<String, List<Transition>> transitions;

    private String currentState;

    public StateMachine(String idleStateName, State idleState) {
        this.states = new HashMap<>();
        this.transitions = new HashMap<>();
        this.states.put(idleStateName, idleState);
    }

    private void checkConditionalTransitions() {
        List<Transition> transitions = this.transitions.get(currentState);
        for (Transition transition : transitions) {
            if (transition.isConditionMet(getCurrentState())) {
                if (doTransition(transition.getToState()))
                    return;
            }
        }
    }

    public String getCurrentStateName() {
        return currentState;
    }

    public State getCurrentState() {
        return states.get(currentState);
    }

    public boolean hasTransition(String fromState, String toState) {
        return this.transitions.containsKey(fromState)
                && this.transitions.get(fromState).stream()
                .anyMatch(transition -> transition.getToState().equals(toState));
    }

    public void addTransition(String fromState, String toState, Function<State, Boolean> condition) {
        Transition transition = new Transition(toState, condition);
        if (this.transitions.containsKey(fromState)) {
            this.transitions.get(fromState).add(transition);
        } else {
            ArrayList<Transition> transitions = new ArrayList<>();
            transitions.add(transition);
            this.transitions.put(fromState, transitions);
        }
    }

    public boolean doTransition(String toState) {
        if (!states.containsKey(toState)) return false;
        getCurrentState().exitState();
        this.currentState = toState;
        getCurrentState().enterState();
        return true;
    }

    public void update(float dt) {
        checkConditionalTransitions();
        getCurrentState().updateState(dt);
    }

}