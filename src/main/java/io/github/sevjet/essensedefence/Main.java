package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import io.github.sevjet.essensedefence.util.Configuration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static io.github.sevjet.essensedefence.GamePlayAppState.field;

public class Main extends SimpleApplication {

    // TODO: 21/05/2016 fix it
    public static InfoScreen start;

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
//        settings.setDisplayFps(false);
//        settings.setDisplayStatView(false);

        return settings;
    }

    public static void main(String[] args) {
        Main app = new Main();

        app.setSettings(mySettings());
        app.setShowSettings(false);
        app.start();
    }

    protected void initStartSettings() {
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
    }

    @Override
    public void simpleInitApp() {
        initStartSettings();

        GamePlayAppState state = new GamePlayAppState();
        stateManager.attach(state);

        start = new InfoScreen();
        flyCam.setDragToRotate(true);

        Integer n = new Integer(5);
        System.out.println(n + "\n" +
                n.getClass() + '\n' +
                n.getClass().getClass() + '\n' +
                n.getClass().getClass().getClass() + '\n' +
                n.getClass().getClass().getClass().getClass() + '\n'
        );
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

    // TODO: 21/05/2016 delete
    private void detachAllControl(Spatial spatial) {
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
}