package io.github.sevjet.essencedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.util.SkyFactory;

import io.github.sevjet.essencedefence.field.EssenceShop;
import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.game.Player;
import io.github.sevjet.essencedefence.game.Round;
import io.github.sevjet.essencedefence.listener.ListenerManager;
import io.github.sevjet.essencedefence.niftyGui.BasicScreen;
import io.github.sevjet.essencedefence.niftyGui.MainMenu;
import io.github.sevjet.essencedefence.util.Configuration;
import io.github.sevjet.essencedefence.util.GeometryManager;

public class Game extends SimpleApplication {

    private static Game _instance;
    private NiftyJmeDisplay niftyDisplay;
    private BasicScreen screen;
    private AbstractAppState state;
    private Round round;

    private Game() {

    }

    public static Game instance() {
        if (_instance == null) {
            _instance = new Game();
        }
        return _instance;
    }

    public Round getRound() {
        return round;
    }

    public Player player() {
        return round != null ? round.player() : null;
    }

    public MapField field() {
        return round != null ? round.filed() : null;
    }

    public Inventory inventory() {
        return round != null ? round.inventory() : null;
    }

    public EssenceShop shop() {
        return round != null ? round.shop() : null;
    }

    public BasicScreen getScreen() {
        return screen;
    }

    public AbstractAppState getState() {
        return state;
    }

    public void switchScreen(final BasicScreen newScreen) {
        if (screen != null) {
            screen.hide();
        }

        screen = newScreen;
        screen.show();
    }

    public void pauseScreen() {
        if (screen != null) {
            screen.hide();
        }
    }

    public void resumeScreen() {
        if (screen != null) {
            screen.show();
        }
    }

    public boolean isStateSet() {
        return state != null;
    }

    public void switchState(final AbstractAppState newState) {
        if (state != null) {
            stateManager.detach(state);
        }
        state = newState;

        stateManager.attach(state);
    }

    public void pauseState() {
        if (state != null) {
            stateManager.detach(state);
        }
    }

    public void resumeState() {
        if (state != null) {
            stateManager.attach(state);
        }
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);

        niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

        guiViewPort.addProcessor(niftyDisplay);

        Configuration.setSettings(settings);
        Configuration.setApp(this);
        Configuration.setAppState(stateManager);
        Configuration.setNiftyDisplay(niftyDisplay);
        Configuration.setNifty(niftyDisplay.getNifty());

        flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(17f, 14f, 40f));
        cam.setRotation(new Quaternion(0f, 1f, 0f, 0f));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));
        inputManager.clearMappings();
        inputManager.clearRawInputListeners();

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.3f, -0.4f, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        ListenerManager.registerListener();
        GeometryManager.init();

        switchScreen(new MainMenu("interface/mainMenu.xml"));

        resetRound();
    }

    public void resetRound() {
        round = new Round(new Player(100f), new MapField(26, 26));
    }

    @Override
    public void stop() {
        pauseState();

        super.stop();
    }

}