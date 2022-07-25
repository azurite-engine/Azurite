package scene;

import graphics.Texture;
import util.Log;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The SceneManager to handle all scenes of an Engine instance.
 * It contains a scene pool and a currently active scene.
 *
 * @author Juyas
 * @version 16.06.2021
 * @since 16.06.2021
 */
public class SceneManager {

    private Set<Scene> scenePool;
    private Scene currentScene;
    private boolean enabled;

    private float minSceneLight;

    public SceneManager() {
        this.scenePool = new HashSet<>();
        this.currentScene = null;
        this.minSceneLight = 1.0f;
        this.enabled = false;
    }

    /**
     * This method is used to enable the sceneManager and call all initialization methods on a possible currentScene.
     * After this method finished, all newly added scenes will call this methods directly.
     */
    public void enable() {
        this.enabled = true;
        awaken(currentScene);
        Log.info("enabled");
    }

    /**
     * The currently active scene.
     */
    public Scene currentScene() {
        return currentScene;
    }

    /**
     * Adds a scene to the pool of the manager instance.
     * Will call all initialization methods of the scene in preset order
     *
     * @return true, if and only if the scene instance is unique in the pool
     * @see Set#add(Object)
     */
    public boolean addScene(Scene scene) {
        boolean add = scenePool.add(scene);
        if (!add) Log.warn("scene with id " + scene.sceneId() + " could not be added", 1);
        return add;
    }

    /**
     * Switches the current scene to a given one.
     *
     * @param scene the scene to switch to
     * @return true if the given scene is now the new current scene
     */
    public boolean switchScene(Scene scene) {
        boolean newScene = false;

        Optional<Scene> sceneOpt = scenePool.stream()
                .filter(s -> s.sceneId() == scene.sceneId())
                .findFirst();

        if (!sceneOpt.isPresent()) {
            // Scene is not present in the scene pool
            if (!addScene(scene)) return false;
            newScene = true;
        }
        return switchScene(scene, newScene);
    }

    /**
     * Switches the current scene to another scene with the given id, which has to be in the scene pool.
     *
     * @param id the scene id belonging to the target scene to switch to
     * @return true if the given scene is now the new current scene
     */
    public boolean switchScene(int id) {
        Optional<Scene> sceneOpt = scenePool.stream()
                .filter(scene -> scene.sceneId() == id)
                .findFirst();
        return sceneOpt.isPresent() && switchScene(sceneOpt.get(), false);
    }

    /**
     * Creates a new set of all known scene ids.
     */
    public Set<Integer> sceneIds() {
        return scenePool.stream().map(Scene::sceneId).collect(Collectors.toSet());
    }


    //passthrough methods - methods just passing calls to inner objects

    public void update() {
        if (currentScene != null) {
            currentScene.update();
        }
    }

    public void updateGameObjects() {
        if (currentScene != null) {
            currentScene.updateGameObjects();
        }
    }

    public void render() {
        if (currentScene != null) {
            currentScene.render();
        }
    }

    public void updateUI() {
        if (currentScene != null) {
            currentScene.updateUI();
            currentScene.textRender();
        }
    }

    public void postProcess(Texture texture) {
        if (currentScene != null) {
            currentScene.postProcess(texture);
        }
    }

    public void debugRender() {
        if (currentScene != null) {
            currentScene.debugRender();
        }
    }

    //below are internal methods

    private void awaken(Scene scene) {
        if (enabled) {
            scene.initRenderers();
            scene.startUi();
            scene.awake();
        } else Log.warn("scene awaken called without being enabled", 1);
    }

    private boolean switchScene(Scene newCurrent, boolean newScene) {
        if (newCurrent == null) {
            Log.warn("tried to switch to a scene that is null", 1);
            return false;
        }
        //we dont wanna call method if the current scene is already displayed
        //consider adding a debug, because usually the programming is aware of his scenes
        //and won't switch to the current scene
        if (newCurrent == currentScene) {
            Log.warn("tried to switch to current scene id " + currentScene.sceneId(), 1);
            return true;
        }
        //deactivate the previous scene if there is one (just one start, there might be no)
        if (currentScene != null) {
            currentScene.deactivate();
        }

        currentScene = newCurrent;
        currentScene.activate();
        if (newScene && enabled) awaken(currentScene);
        Log.info("switched to new scene (id: " + newCurrent.sceneId() + ")", 1);
        return true;
    }

    public float getMinSceneLight() {
        return minSceneLight;
    }

    public void setMinSceneLight(float minSceneLight) {
        this.minSceneLight = minSceneLight;
    }

}