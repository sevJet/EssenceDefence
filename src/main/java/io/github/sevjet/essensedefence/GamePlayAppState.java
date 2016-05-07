package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import static io.github.sevjet.essensedefence.Creator.debubSet;
import static io.github.sevjet.essensedefence.Creator.myBox;
import static io.github.sevjet.essensedefence.Listener.*;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.load;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.save;
import static io.github.sevjet.essensedefence.Tester.TestForSerialization.testSerialization;

public class GamePlayAppState extends AbstractAppState {


    public final static Trigger TRIGGER_BUILD =
            new KeyTrigger(KeyInput.KEY_E);
    public final static Trigger TRIGGER_RESET =
            new KeyTrigger(KeyInput.KEY_R);
    public final static Trigger TRIGGER_ROTATE =
            //           new KeyTrigger(KeyInput.KEY_T);
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    //TODO change
    public final static Trigger TRIGGER_BUILD_WALL =
            new KeyTrigger(KeyInput.KEY_1);
    public final static Trigger TRIGGER_BUILD_TOWER =
            new KeyTrigger(KeyInput.KEY_2);
    public final static Trigger TRIGGER_BUILD_PORTAL =
            new KeyTrigger(KeyInput.KEY_3);
    public final static Trigger TRIGGER_BUILD_FORTRESS =
            new KeyTrigger(KeyInput.KEY_4);
    //TODO fix it
    static Field field;
    public GamePlayAppState() {
    }

    protected void initStartData() {
        Node debugNode = debubSet();
        Configuration.getRootNode().attachChild(debugNode);

        Configuration.getInputManager().addMapping(MAPPING_BUILD, TRIGGER_BUILD);
        Configuration.getInputManager().addMapping(MAPPING_RESET, TRIGGER_RESET);
        Configuration.getInputManager().addMapping(MAPPING_BUILD_WALL, TRIGGER_BUILD_WALL);
        Configuration.getInputManager().addMapping(MAPPING_BUILD_TOWER, TRIGGER_BUILD_TOWER);
        Configuration.getInputManager().addMapping(MAPPING_BUILD_PORTAL, TRIGGER_BUILD_PORTAL);
        Configuration.getInputManager().addMapping(MAPPING_BUILD_FORTRESS, TRIGGER_BUILD_FORTRESS);
        Configuration.getInputManager().addListener(new Listener(), MAPPING_BUILD, MAPPING_RESET, MAPPING_BUILD_WALL,
                MAPPING_BUILD_TOWER, MAPPING_BUILD_PORTAL, MAPPING_BUILD_FORTRESS);
//        Configuration.getInputManager().addListener(actionListener, new String[]{MAPPING_BUILD});
        Creator.attachCenterMark();

        GeometryManager.setDefault(Cell.class, myBox(1 / 2f, 1 / 2f, "cell", ColorRGBA.Black));
        GeometryManager.setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, "wall", ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, myBox(1f, 1f, 1.5f, "tower", ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, "fortress", ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, "portal", ColorRGBA.Magenta));

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
//        save(field);

        super.cleanup();
    }


    @Override
    public void update(float tpf) {
        super.update(tpf);
    }


}