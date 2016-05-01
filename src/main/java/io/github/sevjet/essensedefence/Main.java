package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

import java.awt.*;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();

        settings.setTitle("Our Tower Defense Demo");
        //settings.setSettingsDialogImage("Interface/splashscreen.png");
        settings.setResolution(modes[0].getWidth(), modes[0].getHeight());
        settings.setFrequency(modes[0].getRefreshRate());
        settings.setFullscreen(device.isFullScreenSupported());
        settings.setSamples(5);
        //   settings.setDisplayFps(false);
        //     settings.setDisplayStatView(false);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(33);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/skySphere.jpg", true));

        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
//        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        Node axisNode = coorAxises(1111f);
        rootNode.attachChild(axisNode);
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {
        // super.simpleRender(rm);

    }


    @Override
    public void update() {
        super.update();
    }

    public Node coorAxises(float length) {
        Node axis = new Node();
        Geometry line;

        line = myLine(Vector3f.ZERO,
                Vector3f.UNIT_X.mult(length),
                ColorRGBA.Red);
        axis.attachChild(line);

        line = myLine(Vector3f.ZERO,
                Vector3f.UNIT_Y.mult(length),
                ColorRGBA.Green);
        axis.attachChild(line);

        line = myLine(Vector3f.ZERO,
                Vector3f.UNIT_Z.mult(length),
                ColorRGBA.Blue);
        axis.attachChild(line);

        return axis;
    }

    public Geometry myLine(Vector3f start, Vector3f end) {
        Line line = new Line(start, end);
        Geometry geom = new Geometry("line", line);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);
        return geom;
    }

    public Geometry myLine(Vector3f start, Vector3f end, ColorRGBA clr){
        Geometry line;
        line = myLine(start, end);
        line.getMaterial().setColor("Color", clr);
        return line;
    }
}


