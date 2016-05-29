package io.github.sevjet.essencedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.jme3.system.Natives;
import com.jme3.util.SkyFactory;

import de.lessvoid.nifty.Nifty;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.entity.building.Wall;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.Cell;
import io.github.sevjet.essencedefence.field.InventoryCell;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.listener.ListenerManager;
import io.github.sevjet.essencedefence.niftyGui.StartScreen;
import io.github.sevjet.essencedefence.util.Configuration;
import io.github.sevjet.essencedefence.util.GeometryManager;

import static com.jme3.system.JmeSystem.getPlatform;
import static io.github.sevjet.essencedefence.util.Creator.myBox;
import static io.github.sevjet.essencedefence.util.Creator.myQuad;
import static io.github.sevjet.essencedefence.util.Creator.myShinySphere;

public class Main extends SimpleApplication {

    // TODO: 21/05/2016 fix it
    public static StartScreen start;
    public static GamePlayAppState state;

    public static AppSettings mySettings(boolean isOSX) throws LWJGLException {
        AppSettings settings = new AppSettings(true);

        settings.setTitle("EssenceDefence 1.0");

        try {
            settings.setAudioRenderer("LWJGL");
            Natives.extractNativeLibs(getPlatform(), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isOSX) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            System.setProperty("apple.awt.UIElement", "true");

            java.awt.DisplayMode mode = device.getDisplayMode();
            settings.setResolution(mode.getWidth(), mode.getHeight());
            settings.setFrequency(mode.getRefreshRate());
            settings.setBitsPerPixel(mode.getBitDepth());
            settings.setFullscreen(true);
        } else {
            org.lwjgl.opengl.DisplayMode mode = Arrays.stream(Display.getAvailableDisplayModes())
                    .reduce(new org.lwjgl.opengl.DisplayMode(0, 0), (prev, el) -> {
                        long prevSize = (long) prev.getHeight() * prev.getWidth();
                        long elSize = (long) el.getHeight() * el.getWidth();
                        if (prevSize < elSize) {
                            return el;
                        }
                        if (prevSize == elSize &&
                                prev.getBitsPerPixel() <= el.getBitsPerPixel() &&
                                prev.getFrequency() <= el.getFrequency()) {
                            return el;
                        }

                        return prev;
                    });
            settings.setResolution(mode.getWidth(), mode.getHeight());
            settings.setFrequency(mode.getFrequency());
            settings.setBitsPerPixel(mode.getBitsPerPixel());
            settings.setFullscreen(device.isFullScreenSupported());
        }

        settings.setSamples(2);

        return settings;
    }

    public static void main(String[] args) throws LWJGLException {
        Main app = new Main();

        app.setSettings(mySettings(System.getProperty("os.name")
                .toLowerCase()
                .contains("mac")));
        app.setShowSettings(false);
        app.start();
        Display.setResizable(true);
        Display.setFullscreen(true);
    }

    private void initStartData() {
        GeometryManager.setDefault(Cell.class, myQuad(1, 1, "cell", ColorRGBA.Black));
        GeometryManager.setDefault(MapCell.class, GeometryManager.getDefault(Cell.class));
        GeometryManager.setDefault(InventoryCell.class, GeometryManager.getDefault(Cell.class));
        GeometryManager.setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, "wall", ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, myBox(1f, 1f, 1.5f, "tower", ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, "fortress", ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, "portal", ColorRGBA.Magenta));
        GeometryManager.setDefault(Monster.class, myBox(1 / 3f, 1 / 3f, 1 / 2f, "monster", ColorRGBA.Yellow));
        GeometryManager.setDefault(Essence.class, myShinySphere(1 / 2f, "essence", ColorRGBA.randomColor()));

        Gamer gamer = new Gamer(100);
        Configuration.setGamer(gamer);
        gamer.setGui();


        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.3f, -0.4f, -1).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        Configuration.getRootNode().addLight(sun);
    }

    protected void initStartSettings() {
        setDisplayFps(false);
        setDisplayStatView(false);

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

        ViewPort guiViewPort = getGuiViewPort();
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);

        Configuration.setSettings(settings);
        Configuration.setApp(this);
        Configuration.setAppState(stateManager);
        Configuration.setNiftyDisplay(niftyDisplay);
        Configuration.setNifty(nifty);


        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(100);
        cam.setLocation(new Vector3f(17f, 14f, 40f));
        cam.setRotation(new Quaternion(0f, 1f, 0f, 0f));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));
        inputManager.clearMappings();
        inputManager.clearRawInputListeners();
        ListenerManager.registerListener();
    }

    @Override
    public void simpleInitApp() {
        initStartSettings();
        initStartData();
        flyCam.setDragToRotate(true);


        start = new StartScreen("interface/mainMenu.xml");
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void stop() {
//        save_model();
//
        super.stop();
    }

}