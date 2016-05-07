package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.Savable;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.SavableSerializer;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;
import static io.github.sevjet.essensedefence.Creator.myBox;

public class Main extends SimpleApplication {

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
        settings.setSamples(16);
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
        work_test();
        unwork_test();
//        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(100);
        cam.setLocation(new Vector3f(0f, 32f, 100f));
        cam.setRotation(new Quaternion(0.0f, 1.0f, 0.0f, 0.0f));
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));
    }

    @Override
    public void simpleInitApp() {
        Configuration.setSettings(settings);
        Configuration.setApp(this);
        Configuration.setAppState(stateManager);

// https://wiki.jmonkeyengine.org/doku.php/jme3:advanced:save_and_load
        initStartSettings();

//        GamePlayAppState state = new GamePlayAppState();
//        stateManager.attach(state);

    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void update() {
        super.update();
    }




    public void work_test(){
        save();
        load();
    }

    public void unwork_test(){
        save2();
        load2();
    }

    public void save(){
        Geometry geom = Creator.myBox(1, 20, "test1", ColorRGBA.LightGray);

        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome+"/Models/"+"WorkModel.j3o");
        try {
//            exporter.save(rootNode, file);
            exporter.save(geom, file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
        }

    }
    public void load(){

        Geometry geom = Creator.myBox(1, 20, "test", ColorRGBA.LightGray);

        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome+"/Models/"+"TestModel.j3o");
        try {
            exporter.save(geom, file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
        }
    }

    public void save2(){

        TestObject test = new TestObject(2, 2);
        test.setGeometry(Creator.myBox(1, 2, "test", ColorRGBA.LightGray));

        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome+"/Models/"+"TestModel2.j3o");
        try {
//            exporter.save(rootNode, file);
            exporter.save(test, file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
        }

    }
    public void load2(){

        TestObject test=null;
//        String userHome = System.getProperty("user.home");
//        BinaryImporter importer = BinaryImporter.getInstance();
//        File file = new File(userHome+"/Models/TestModel2.j3o");
//        try {
//            test = (TestObject) importer.load(file);
//            if (test == null){
//                System.out.println(":C");
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error: Failed to save game!", ex);
//        }



        String userHome = System.getProperty("user.home");
        assetManager.registerLocator(userHome, FileLocator.class);
//        test = (TestObject) assetManager.loadModel("Models/TestModel2.j3o");
//          test = (TestObject) ((Savable)assetManager.loadModel("Models/TestModel.j3o"));
        Savable temp = assetManager.loadModel("Models/TestModel2.j3o");
        test = (TestObject)temp;
        System.out.println(test.getGeometry().getName());
    }

}
