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
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class GamePlayAppState extends AbstractAppState {

    private final static Trigger TRIGGER_BUILD =
            new KeyTrigger(KeyInput.KEY_E);
    private final static Trigger TRIGGER_ROTATE =
            //           new KeyTrigger(KeyInput.KEY_T);
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String MAPPING_BUILD = "Build";
    private SimpleApplication app;
    private AppStateManager appState;
    private Camera cam;
    private FlyByCamera flyCam;
    private Node rootNode;
    private AssetManager assetManager;
    private AppSettings settings;
    private Node guiNode;
    private InputManager inputManager;
    private Creator creater;
    private Tester tester;
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_BUILD)) {

                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray, results);

                if (results.size() > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();
                    if (target.getParent() instanceof Field) {
                        ((Field) target.getParent()).getCell(target).setPassability(true);
                    }
                } else {
                    System.out.println("Selection: Nothing");
                }
            }
        }
    };

    public GamePlayAppState(AppSettings settings) {
        this.settings = settings;
    }

    protected void initStartData() {
        inputManager.addMapping(MAPPING_BUILD, TRIGGER_BUILD);
        inputManager.addListener(analogListener, MAPPING_BUILD);
//        inputManager.addListener(actionListener, new String[]{MAPPING_BUILD});
        creater.attachCenterMark();

        GeometryManager.setDefault(Cell.class, creater.myBox(1 / 2f, 1 / 2f));
        GeometryManager.setDefault(Wall.class, creater.myBox(1 / 2f, 1 / 2f, 1f, ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, creater.myBox(1f, 1f, 1.5f, ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, creater.myBox(3 / 2f, 3 / 2f, 2f, ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, creater.myBox(1f, 1 / 2f, 1.5f, ColorRGBA.Magenta));

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

        creater = new Creator(app, appState, settings);
        tester = new Tester(app, appState, settings);

        initStartData();
        initDebug();

        tester.tests();
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
