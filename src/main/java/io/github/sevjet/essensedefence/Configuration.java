package io.github.sevjet.essensedefence;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class Configuration {

    private static Configuration instance;

    private SimpleApplication app;
    private AppStateManager appState;
    private Camera cam;
    private FlyByCamera flyCam;
    private Node rootNode;
    private AssetManager assetManager;
    private Node guiNode;
    private AppSettings settings;
    private InputManager inputManager;
    private Creator creator;
    private Tester tester;

    public Configuration(AppStateManager stateManager, Application app,AppSettings settings) {
        instance = this;
        setApp((SimpleApplication)app);
        setAppState(stateManager);
        setCam(this.app.getCamera());
        setRootNode(this.app.getRootNode());
        setAssetManager(this.app.getAssetManager());
        setFlyCam(this.app.getFlyByCamera());
        setGuiNode(this.app.getGuiNode());
        setSettings(settings);
        setInputManager(this.app.getInputManager());
        setCreator(new Creator());
        setTester(new Tester());
    }

    public static Configuration getInstance() {
        return instance;
    }

    public SimpleApplication getApp() {
        return app;
    }

    public void setApp(SimpleApplication app) {
        if(this.app == null) {
            this.app = app;
        }
        return;
    }

    public AppStateManager getAppState() {
        return appState;
    }

    public void setAppState(AppStateManager appState) {
        if(this.appState == null) {
            this.appState = appState;
        }
        return;
    }

    public Camera getCam() {
        return cam;
    }

    public void setCam(Camera cam) {
        if(this.cam == null) {
            this.cam = cam;
        }
        return;
    }

    public FlyByCamera getFlyCam() {
        return flyCam;
    }

    public void setFlyCam(FlyByCamera flyCam) {
        if(this.flyCam == null) {
            this.flyCam = flyCam;
        }
        return;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        if(this.rootNode == null) {
            this.rootNode = rootNode;
        }
        return;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager) {
        if(this.assetManager == null) {
            this.assetManager = assetManager;
        }
        return;
    }

    public Node getGuiNode() {
        return guiNode;
    }

    public void setGuiNode(Node guiNode) {
        if(this.guiNode == null) {
            this.guiNode = guiNode;
        }
        return;
    }

    public AppSettings getSettings() {
        return settings;
    }

    public void setSettings(AppSettings settings) {
        if(this.settings == null) {
            this.settings = settings;
        }
        return;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setInputManager(InputManager inputManager) {
        if(this.inputManager == null) {
            this.inputManager = inputManager;
        }
        return;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        if(this.creator == null) {
            this.creator = creator;
        }
        return;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        if(this.tester == null) {
            this.tester = tester;
        }
        return;
    }
}
