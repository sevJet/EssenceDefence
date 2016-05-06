package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import static io.github.sevjet.essensedefence.Creator.debubSet;
import static io.github.sevjet.essensedefence.Creator.myBox;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.load;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.save;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.testSerialization;

public class GamePlayAppState extends AbstractAppState {

    private final static Trigger TRIGGER_BUILD =
            new KeyTrigger(KeyInput.KEY_E);
    private final static Trigger TRIGGER_ROTATE =
            //           new KeyTrigger(KeyInput.KEY_T);
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String MAPPING_BUILD = "Build";
    //TODO fix it
    static Field field;

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_BUILD)) {

                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(Configuration.getCam().getLocation(), Configuration.getCam().getDirection());
                Configuration.getRootNode().collideWith(ray, results);

                if (results.size() > 0) {
                    Geometry target = results.getClosestCollision().getGeometry();
                    if (target.getParent() instanceof Field) {
                        ((Field) target.getParent()).getCell(target).setPassability(true);
                    }
                } else {
//                    System.out.println("Selection: Nothing");
                }
            }
        }
    };

    public GamePlayAppState() {
    }

    protected void initStartData() {
        Node debugNode = debubSet();
        Configuration.getRootNode().attachChild(debugNode);

        Configuration.getInputManager().addMapping(MAPPING_BUILD, TRIGGER_BUILD);
        Configuration.getInputManager().addListener(analogListener, MAPPING_BUILD);
//        Configuration.getInputManager().addListener(actionListener, new String[]{MAPPING_BUILD});
        Creator.attachCenterMark();

        GeometryManager.setDefault(JME3Object.class, myBox(1 / 4f, 1 / 8f, 1 / 16f, ColorRGBA.Red));
        GeometryManager.setDefault(Cell.class, myBox(1 / 2f, 1 / 2f));
        GeometryManager.setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, myBox(1f, 1f, 1.5f, ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, ColorRGBA.Magenta));

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        initStartData();

        load();
        field = testSerialization();
//        guiNode.attachChild(Configuration.getRootNode());
//        Configuration.getRootNode().scale(30);
    }

    @Override
    public void cleanup() {
        save(field);

        super.cleanup();
    }


    @Override
    public void update(float tpf) {
        super.update(tpf);
    }


}