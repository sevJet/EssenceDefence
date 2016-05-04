package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;

import static io.github.sevjet.essensedefence.Main.assetManagerStatic;

public class GamePlayAppState extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager appState;

    private Camera cam;
    private FlyByCamera flyCam;
    private Node rootNode;
    private AssetManager assetManager;
    private AppSettings settings;
    private Node guiNode;

    public GamePlayAppState(AppSettings settings) {
        this.settings = settings;
    }

    protected void initStartData() {
        Box box = new Box(1/2f, 1/2f, 0f);
        Geometry geom = new Geometry("box", box);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);

        GeometryManager.setDefault(Cell.class, geom);


        box = new Box(1/4f, 1/4f, 1f);
        geom = new Geometry("box", box);
        mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Cyan);
        geom.setMaterial(mat);

        GeometryManager.setDefault(Wall.class, geom);

    }

    protected void initDebug(){
        Node debugNode = new Node();
        debugNode.attachChild(coorAxises(111f));
        debugNode.attachChild(gridXY(100));

//        Unuseless shit
        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
//        mat.getAdditionalRenderState().setWireframe(true);
        geom.setMaterial(mat);
        debugNode.attachChild(geom);

        rootNode.attachChild(debugNode);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.appState = stateManager;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.flyCam = this.app.getFlyByCamera();
        this.guiNode = this.app.getGuiNode();

        initStartData();
        initDebug();

        testWall();
//        testFieldClass();

        guiNode.attachChild(rootNode);
        rootNode.scale(30);
    }

    public void testWall(){
        Wall wall = new Wall();
        Field field = new Field(5, 5);
                wall.getGeometry().rotate((float)Math.PI*(-30)/180f, 0, 0);
        field.setLocalTranslation(10, 10, 1);
        field.getCell(2, 3).setBuilding(wall);

        wall = new Wall();
                 wall.getGeometry().rotate((float)Math.PI*(-30)/180f, 0, 0);
        field.getCell(4, 1).setBuilding(wall);

        rootNode.attachChild(field);
    }

    // FIX GRID INTO Field CLASS
    public void testFieldClass(){
        int rowNum = 5, colNum = 25;
        Node badFace = new Node();

        Field map = new Field(rowNum, colNum);
        badFace.attachChild(map);
        map.setLocalTranslation(4, 0, 1);

        map = new Field(8, 3);
        badFace.attachChild(map);
        map.setLocalTranslation(4, 15, 1);

        map = new Field(8, 3);
        badFace.attachChild(map);
        map.setLocalTranslation(25, 15, 1);

        map = new Field(8, 2);
        badFace.attachChild(map);
        map.setLocalTranslation(8, 21, 1);
        map.rotate(0, 0,(float)Math.PI*45/180f);

        map = new Field(8, 2);
        badFace.attachChild(map);
        map.setLocalTranslation(22, 22, 1);
        map.rotate(0, 0,-(float)Math.PI*45/180f);

        rootNode.attachChild(badFace);
//        badFace.scale(20);
//        badFace.setLocalTranslation(300, 200, 0);
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void render(RenderManager rm) {
        super.render(rm);
    }



    public Node gridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr){
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(rowLen, colLen, lineDist);
        geom = new Geometry("gridXY", grid);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f*(float)Math.PI/180f, 0, 0);
        axis.attachChild(geom);

        return axis;
    }

    public Node gridXY(int length, ColorRGBA clr){
        return gridXY(length, length, 1, clr);
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
