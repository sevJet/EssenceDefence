package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.collision.CollisionResults;
import com.jme3.export.binary.BinaryExporter;
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.deploy.util.SessionState.save;

public class GamePlayAppState extends AbstractAppState {

    //TODO fix it
    static Field field;

    private final static Trigger TRIGGER_BUILD =
            new KeyTrigger(KeyInput.KEY_E);
    private final static Trigger TRIGGER_ROTATE =
            //           new KeyTrigger(KeyInput.KEY_T);
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);

    private final static String MAPPING_BUILD = "Build";

    private Configuration config = Configuration.getInstance();

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_BUILD)) {

                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(config.getCam().getLocation(), config.getCam().getDirection());
                config.getRootNode().collideWith(ray, results);

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

    public GamePlayAppState() {}


    protected void initStartData() {
        config.getInputManager().addMapping(MAPPING_BUILD, TRIGGER_BUILD);
        config.getInputManager().addListener(analogListener, MAPPING_BUILD);
//        inputManager.addListener(actionListener, new String[]{MAPPING_BUILD});
        config.getCreator().attachCenterMark();

        GeometryManager.setDefault(Cell.class, config.getCreator().myBox(1 / 2f, 1 / 2f));
        GeometryManager.setDefault(Wall.class, config.getCreator().myBox(1 / 2f, 1 / 2f, 1f, ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, config.getCreator().myBox(1f, 1f, 1.5f, ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, config.getCreator().myBox(3 / 2f, 3 / 2f, 2f, ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, config.getCreator().myBox(1f, 1 / 2f, 1.5f, ColorRGBA.Magenta));

    }

    protected void initDebug() {
        Node debugNode = new Node();
        debugNode.attachChild(config.getCreator().coorAxises(111f));
//        debugNode.attachChild(gridXY(100));

        Geometry geom;
        geom = config.getCreator().myBox("box", Vector3f.ZERO, ColorRGBA.Blue);
        debugNode.attachChild(geom);

        config.getRootNode().attachChild(debugNode);
    }
    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        initStartData();

        load();
        field = config.getTester().testSerialization();

//        guiNode.attachChild(rootNode);
//        rootNode.scale(30);


    }

    @Override
    public void cleanup() {
        if (field != null)
            save();
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

    //TODO move to another class, change signature
    public void save(){
        Field.serialize(field);
        System.out.println(field);
    }
    //TODO move to another class, change signature
    public void load(){
        field = Field.deserialize();
        System.out.println(field);
        config.getRootNode().attachChild(field);
        field.setLocalTranslation(-55, 0, -2);
    }
}




