package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class Configuration {
    private static Configuration _instance = new Configuration();
    private SimpleApplication app = null;
    private AppStateManager appState = null;
    private AppSettings settings = null;

    private Configuration() {

    }

    public static Configuration getInstance() {
        return _instance;
    }

    public static SimpleApplication getApp() {
        Configuration instance = getInstance();
        return instance.app;
    }

    public static void setApp(SimpleApplication newApp) {
        Configuration instance = getInstance();
        SimpleApplication oldApp = instance.app;
        instance.app = (oldApp == null ? newApp : oldApp);
    }

    public static AppStateManager getAppState() {
        Configuration instance = getInstance();
        return instance.appState;
    }

    public static void setAppState(AppStateManager newState) {
        Configuration instance = getInstance();
        AppStateManager oldState = instance.appState;
        instance.appState = (oldState == null ? newState : oldState);
    }

    public static AppSettings getSettings() {
        Configuration instance = getInstance();
        return instance.settings;
    }

    public static void setSettings(AppSettings newSettings) {
        Configuration instance = getInstance();
        AppSettings oldSettings = instance.settings;
        instance.settings = (oldSettings == null ? newSettings : oldSettings);
    }

    public static AssetManager getAssetManager() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getAssetManager() : null);
    }

    public static InputManager getInputManager() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getInputManager() : null);
    }

    public static Camera getCam() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getCamera() : null);
    }

    public static FlyByCamera getFlyCam() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getFlyByCamera() : null);
    }

    public static Node getRootNode() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getRootNode() : null);
    }

    public static Node getGuiNode() {
        Configuration instance = getInstance();
        return (instance.app != null ? instance.app.getGuiNode() : null);
    }
}
