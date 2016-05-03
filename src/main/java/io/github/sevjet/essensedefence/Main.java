package io.github.sevjet.essensedefence;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

import java.awt.*;
import java.util.HashMap;

public class Main extends SimpleApplication {
    private HashMap <Class<? extends jme3Object>, Geometry> mapDefaultGeometry = new HashMap<>();

    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);

        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        int high = 0;
        for (int i = 1; i < modes.length; i++){
            if (modes[i].getWidth()*modes[i].getHeight() > modes[high].getWidth()*modes[high].getHeight() ||
                    (modes[i].getWidth()*modes[i].getHeight() == modes[high].getWidth()*modes[high].getHeight() &&
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
        settings.setFullscreen(device.isFullScreenSupported());
        settings.setSamples(16);
        settings.setBitsPerPixel(modes[high].getBitDepth());
        //   settings.setDisplayFps(false);
        //     settings.setDisplayStatView(false);

        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    protected void initStartData(){
        this.mapDefaultGeometry.clear();

        Box box = new Box(1/2f, 1/2f, 0);
        Geometry geom = new Geometry("box", box);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);

        this.mapDefaultGeometry.put(cell.class, geom);

    }
    protected void initDebug(){
        Node debugNode = new Node();
        debugNode.attachChild(coorAxises(1111f));
        debugNode.attachChild(gridXY(100));
        rootNode.attachChild(debugNode);
    }

    @Override
    public void simpleInitApp() {
        initStartData();
        initDebug();

        testCellClass();

        flyCam.setMoveSpeed(33);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "textures/Skysphere.jpg", true));

        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
//        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }

    public void testCellClass(){
        cell c = new cell(3, 3, this.mapDefaultGeometry.get(cell.class).clone());
        rootNode.attachChild(c.getGeometry());

        c = new cell(5, 2, this.mapDefaultGeometry.get(cell.class).clone(), new building());
        rootNode.attachChild(c.getGeometry());

        c = new cell(2, 5, this.mapDefaultGeometry.get(cell.class).clone(), true);
        rootNode.attachChild(c.getGeometry());

        c = new cell(6, 6, this.mapDefaultGeometry.get(cell.class).clone(), true);
        c.setBuild(new building());
        rootNode.attachChild(c.getGeometry());
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


    public Node gridXY(int length, ColorRGBA clr){
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(length, length, 1);
        geom = new Geometry("gridXY", grid);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f*(float)Math.PI/180f, 0, 0);
        axis.attachChild(geom);

        return axis;
    }

    public Node gridXY(int length){
        return gridXY(length, ColorRGBA.Gray);
    }

    public Node coorAxises(float length) {
        Node axis = new Node();
        Geometry line;

        line = myLine(Vector3f.UNIT_X.mult(0),
                Vector3f.UNIT_X.mult(length),
                ColorRGBA.Red);
        axis.attachChild(line);

        line = myLine(Vector3f.UNIT_Y.mult(0),
                Vector3f.UNIT_Y.mult(length),
                ColorRGBA.Green);
        axis.attachChild(line);

        line = myLine(Vector3f.UNIT_Z.mult(0),
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


