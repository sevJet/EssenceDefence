package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.Nifty;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.InventoryCell;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.listener.ListenerManager;
import io.github.sevjet.essensedefence.niftyGui.StartScreen;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.GeometryManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static io.github.sevjet.essensedefence.GamePlayAppState.field;
import static io.github.sevjet.essensedefence.util.Creator.*;

public class Main extends SimpleApplication {

    // TODO: 21/05/2016 fix it
//    public static InfoScreen info;
    public static NiftyJmeDisplay niftyDisplay;
    public static StartScreen start;
    public static Nifty nifty;
    public static GamePlayAppState state;

    public static AppSettings mySettings() {
        AppSettings settings = new AppSettings(true);

        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = device.getDisplayMode();
//        DisplayMode[] modes = device.getDisplayModes();
//        int high = 0;
//        for (int i = 1; i < modes.length; i++) {
//            if (modes[i].getWidth() * modes[i].getHeight() > modes[high].getWidth() * modes[high].getHeight() ||
//                    (modes[i].getWidth() * modes[i].getHeight() == modes[high].getWidth() * modes[high].getHeight() &&
//                            modes[i].getRefreshRate() >= modes[high].getRefreshRate() &&
//                            modes[i].getBitDepth() >= modes[high].getBitDepth()))
//                high = i;
//            System.out.println(i+"  "+modes[i].getWidth()+' '+modes[i].getHeight()+' '+
//                    modes[i].getRefreshRate()+ ' ' +modes[i].getBitDepth());
//        }
        settings.setTitle("Our Tower Defense Demo");
//        settings.setSettingsDialogImage("Interface/splashscreen.png");
        settings.setResolution(mode.getWidth(), mode.getHeight());
        settings.setFrequency(mode.getRefreshRate());
//        settings.setFullscreen(device.isFullScreenSupported());
        settings.setSamples(2);
//        settings.setSamples(16);
        settings.setBitsPerPixel(mode.getBitDepth());

        return settings;
    }

    public static void main(String[] args) {
        Main app = new Main();

        app.setSettings(mySettings());
        app.setShowSettings(false);
        app.start();

    }

    //     TODO: 21/05/2016 delete
    public static void detachAllControl(Spatial spatial) {
        Node rootNode;
        if (spatial == null)
            return;
        if (spatial instanceof Node) {
            rootNode = (Node) spatial;
            for (Spatial sp : rootNode.getChildren()) {
                for (int i = 0; i < sp.getNumControls(); i++) {
                    sp.removeControl(AbstractControl.class);
                }
                detachAllControl(sp);
            }
        } else for (int i = 0; i < spatial.getNumControls(); i++) {
            spatial.removeControl(AbstractControl.class);
        }
    }

    private void initStartData() {
        //        GeometryManager.setDefault(Cell.class, myBox(1 / 2f, 1 / 2f, "cell", ColorRGBA.Black));
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
        sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        Configuration.getRootNode().addLight(sun);
    }

    protected void initStartSettings() {
//        setDisplayFps(false);
//        setDisplayStatView(false);

        Configuration.setSettings(settings);
        Configuration.setApp(this);
        Configuration.setAppState(stateManager);
//        jme3tools.optimize.GeometryBatchFactory.optimize(rootNode);

//        flyCam.setEnabled(false);

        flyCam.setMoveSpeed(100);
//        cam.setLocation(new Vector3f(0f, 32f, 100f));
//        cam.setRotation(new Quaternion(0.0f, 1.0f, 0.0f, 0.0f));
        cam.setLocation(new Vector3f(33.08874f, 48.561615f, 9.273602f));
        cam.setRotation(new Quaternion(0.008674252f, 0.86961013f, -0.49342605f, 0.015287385f));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));
        inputManager.clearMappings();
        ListenerManager.registerListener();
    }

    @Override
    public void simpleInitApp() {
        initStartSettings();
        initStartData();
        flyCam.setDragToRotate(true);


        niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

        ViewPort guiViewPort = Configuration.getApp().getGuiViewPort();
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);

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

    // TODO: 21/05/2016 delete
    private void save_model() {
        field.getGrid().removeFromParent();
//        Configuration.getGamer().getInventory().removeFromParent();
        detachAllControl(rootNode);
        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome + "/Models" + "RootNode.j3o");
        try {
            exporter.save(rootNode, file);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}