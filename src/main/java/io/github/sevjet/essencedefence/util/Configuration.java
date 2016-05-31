package io.github.sevjet.essencedefence.util;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

import io.github.sevjet.essencedefence.Gamer;
import io.github.sevjet.essencedefence.field.EssenceShop;
import io.github.sevjet.essencedefence.field.Inventory;

public class Configuration {
    private static Configuration _instance = new Configuration();
    private SimpleApplication app = null;
    private AppStateManager appState = null;
    private AppSettings settings = null;
    private NiftyJmeDisplay niftyDisplay = null;
    private Nifty nifty = null;

    private Gamer gamer = null;

    private Configuration() {

    }

    public static Configuration getInstance() {
        return _instance;
    }

    public static Nifty getNifty() {
        Configuration instance = getInstance();
        return instance.nifty;
    }

    public static void setNifty(Nifty newNifty) {
        Configuration instance = getInstance();
        Nifty oldNifty = instance.nifty;
        instance.nifty = (oldNifty == null ? newNifty : oldNifty);
    }

    public static NiftyJmeDisplay getNiftyDisplay() {
        Configuration instance = getInstance();
        return instance.niftyDisplay;
    }

    public static void setNiftyDisplay(NiftyJmeDisplay newDisplay) {
        Configuration instance = getInstance();
        NiftyJmeDisplay oldDisplay = instance.niftyDisplay;
        instance.niftyDisplay = (oldDisplay == null ? newDisplay : oldDisplay);
    }

    public static Gamer getGamer() {
        Configuration instance = getInstance();
        return instance.gamer;
    }

    public static void setGamer(Gamer newGamer) {
        Configuration instance = getInstance();
        Gamer oldGamer = instance.gamer;
        instance.gamer = (oldGamer == null ? newGamer : oldGamer);
    }

    public static Inventory getInventory() {
        Configuration instance = getInstance();
        return instance.gamer != null ? instance.gamer.getInventory() : null;
    }

    public static EssenceShop getShop() {
        Configuration instance = getInstance();
        return instance.gamer != null ? instance.gamer.getShop() : null;
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
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getAssetManager();
    }

    public static InputManager getInputManager() {
        Configuration instance = getInstance();
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getInputManager();
    }

    public static Camera getCam() {
        Configuration instance = getInstance();
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getCamera();
    }

    public static FlyByCamera getFlyCam() {
        Configuration instance = getInstance();
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getFlyByCamera();
    }

    public static Node getRootNode() {
        Configuration instance = getInstance();
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getRootNode();
    }

    public static Node getGuiNode() {
        Configuration instance = getInstance();
        if (instance.app == null) {
            throw new IllegalStateException("Configuration not set");
        }
        return instance.app.getGuiNode();
    }
}
