package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

import java.awt.*;

public class Main extends SimpleApplication {

    public static AppSettings mySettings() {
        AppSettings settings = new AppSettings(true);

        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        int high = 0;
        for (int i = 1; i < modes.length; i++) {
            if (modes[i].getWidth() * modes[i].getHeight() > modes[high].getWidth() * modes[high].getHeight() ||
                    (modes[i].getWidth() * modes[i].getHeight() == modes[high].getWidth() * modes[high].getHeight() &&
                            modes[i].getRefreshRate() >= modes[high].getRefreshRate() &&
                            modes[i].getBitDepth() >= modes[high].getBitDepth()))
                high = i;
//            System.out.println(i+"  "+modes[i].getWidth()+' '+modes[i].getHeight()+' '+
//                    modes[i].getRefreshRate()+ ' ' +modes[i].getBitDepth());

        }
        settings.setTitle("Our Tower Defense Demo");
        //settings.setSettingsDialogImage("Interface/splashscreen.png");
        settings.setResolution(modes[high].getWidth(), modes[high].getHeight());
        settings.setFrequency(modes[high].getRefreshRate());
//        settings.setFullscreen(device.isFullScreenSupported());
        settings.setSamples(16);
        settings.setBitsPerPixel(modes[high].getBitDepth());
        //   settings.setDisplayFps(false);
        //     settings.setDisplayStatView(false);
        return settings;
    }

    public static void main(String[] args) {
        Main app = new Main();

        app.setSettings(mySettings());
        app.setShowSettings(false);
        app.start();
    }

    protected void initStartSettings() {
        //flyCam.setEnabled(false);
        flyCam.setMoveSpeed(100);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));
    }

    @Override
    public void simpleInitApp() {
        initStartSettings();
        Configuration.setSettings(settings);
        Configuration.setApp(this);
        Configuration.setAppState(stateManager);

        GamePlayAppState state = new GamePlayAppState();
        stateManager.attach(state);

    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void update() {
        super.update();
    }
}