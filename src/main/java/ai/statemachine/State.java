package ai.statemachine;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 13.07.2021
 * @since 13.07.2021
 */
public interface State {

    void enterState();

    void updateState(float dt);

    void exitState();

}