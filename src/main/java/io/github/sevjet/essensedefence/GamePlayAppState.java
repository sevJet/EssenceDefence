package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.listener.*;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.GeometryManager;

import static io.github.sevjet.essensedefence.util.Creator.*;
import static io.github.sevjet.essensedefence.util.Tester.TestForSerialization.save;
import static io.github.sevjet.essensedefence.util.Tester.TestForSerialization.testSerialization;
import static io.github.sevjet.essensedefence.util.Tester.testGamer;
import static io.github.sevjet.essensedefence.util.Tester.testText;
import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class GamePlayAppState extends AbstractAppState {

    //TODO fix it
    public static Field field;

    public GamePlayAppState() {
    }

    protected void initStartData() {
        Node debugNode = debugSet();
        Configuration.getRootNode().attachChild(debugNode);

        ListenerManager.registerListener();

        Creator.attachCenterMark();

        GeometryManager.setDefault(Cell.class, myBox(1 / 2f, 1 / 2f, "cell", ColorRGBA.Black));
        GeometryManager.setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, "wall", ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, myBox(1f, 1f, 1.5f, "tower", ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, "fortress", ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, "portal", ColorRGBA.Magenta));
        GeometryManager.setDefault(Monster.class, myBox(1 / 3f, 1 / 3f, 1 / 2f, "monster", ColorRGBA.Yellow));
        GeometryManager.setDefault(Essence.class, mySphere(1 / 2f, "essence", ColorRGBA.randomColor()));
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        initStartData();
        testText();
        testGamer();

//        field = load();
//        field.setLocalTranslation(-55, 10, 0);
//        Configuration.getRootNode().attachChild(field);

        field = testSerialization();
//        guiNode.attachChild(Configuration.getRootNode());
//        Configuration.getRootNode().scale(30);
    }

    @Override
    public void cleanup() {
        if (field != null)
            save(field);

        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }


}