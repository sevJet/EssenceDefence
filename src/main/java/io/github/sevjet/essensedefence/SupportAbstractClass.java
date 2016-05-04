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

public abstract class SupportAbstractClass {
    protected SimpleApplication app;
    protected AppStateManager appState;
    protected Camera cam;
    protected FlyByCamera flyCam;
    protected Node rootNode;
    protected AssetManager assetManager;
    protected Node guiNode;
    protected AppSettings settings;
    protected InputManager inputManager;

    public SupportAbstractClass(Application app, AppStateManager appState, AppSettings settings) {
        this.app = (SimpleApplication) app;
        this.appState = appState;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.flyCam = this.app.getFlyByCamera();
        this.guiNode = this.app.getGuiNode();
        this.inputManager = this.app.getInputManager();
        this.settings = settings;
    }
}
