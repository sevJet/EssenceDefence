package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class GamePlayAppState extends AbstractAppState {

    private SimpleApplication app;
    private AppStateManager appState;
    private Camera cam;
    private FlyByCamera flyCam;
    private Node rootNode;
    private AssetManager assetManager;
    private AppSettings settings;
    private Node guiNode;
    private InputManager inputManager;

    private Creater creater;
    private Tester tester;


    public GamePlayAppState(AppSettings settings) {
        this.settings = settings;
    }


    private final static Trigger TRIGGER_BUILD =
            new KeyTrigger(KeyInput.KEY_E);
    private final static Trigger TRIGGER_ROTATE =
            //           new KeyTrigger(KeyInput.KEY_T);
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String MAPPING_BUILD = "Build";

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_BUILD)) {

                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);

                if (results.size() > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();
                    if (target.getParent().getClass() == Field.class) {
                        ((Field) target.getParent()).getCell(target).setPassability(true);
                    }
                } else {
                    System.out.println("Selection: Nothing");
                }
            }
        }
    };

    protected void initStartData() {
        inputManager.addMapping(MAPPING_BUILD, TRIGGER_BUILD);
        inputManager.addListener(analogListener, new String[]{MAPPING_BUILD});
//        inputManager.addListener(actionListener, new String[]{MAPPING_BUILD});
        creater.attachCenterMark();

        Box box = new Box(1 / 2f, 1 / 2f, 0f);
        Geometry geom = new Geometry("box", box);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);

        GeometryManager.setDefault(Cell.class, geom);


        box = new Box(1 / 4f, 1 / 4f, 1f);
        geom = new Geometry("box", box);
        mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Cyan);
        geom.setMaterial(mat);

        GeometryManager.setDefault(Wall.class, geom);

    }


    protected void initDebug() {
        Node debugNode = new Node();
        debugNode.attachChild(creater.coorAxises(111f));
//        debugNode.attachChild(gridXY(100));

//        Unuseless shit
        Geometry geom;
        geom = creater.myBox("box", Vector3f.ZERO, ColorRGBA.Blue);
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
        this.inputManager = this.app.getInputManager();

        creater = new Creater(app, appState, settings);
        tester = new Tester(app, appState, settings);

        initStartData();
        initDebug();

        tester.testTrigger();
//        guiNode.attachChild(rootNode);
//        rootNode.scale(30);
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


}
