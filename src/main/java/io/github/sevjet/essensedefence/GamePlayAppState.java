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
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.GeometryManager;
import io.github.sevjet.essensedefence.util.listeners.ListenerForBuilding;
import io.github.sevjet.essensedefence.util.listeners.ListenerForCell;
import io.github.sevjet.essensedefence.util.listeners.ListenerForEssence;
import io.github.sevjet.essensedefence.util.listeners.ListenerForMonsters;

import static io.github.sevjet.essensedefence.util.Creator.*;
import static io.github.sevjet.essensedefence.util.Tester.TestForSerialization.save;
import static io.github.sevjet.essensedefence.util.Tester.TestForSerialization.testSerialization;
import static io.github.sevjet.essensedefence.util.Tester.testGamer;
import static io.github.sevjet.essensedefence.util.Tester.testText;
import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.*;

public class GamePlayAppState extends AbstractAppState {

    //TODO fix it
    public static Field field;

    public GamePlayAppState() {
    }

    protected void initStartData() {
        Node debugNode = debugSet();
        Configuration.getRootNode().attachChild(debugNode);

        InputManager inputManager = Configuration.getInputManager();
        inputManager.addMapping(MAPPING_MAKE_PASSABLE, TRIGGER_MAKE_PASSABLE);
        inputManager.addMapping(MAPPING_RESET, TRIGGER_RESET);
        inputManager.addMapping(MAPPING_BUILD_WALL, TRIGGER_BUILD_WALL);
        inputManager.addMapping(MAPPING_BUILD_TOWER, TRIGGER_BUILD_TOWER);
        inputManager.addMapping(MAPPING_BUILD_PORTAL, TRIGGER_BUILD_PORTAL);
        inputManager.addMapping(MAPPING_BUILD_FORTRESS, TRIGGER_BUILD_FORTRESS);
        inputManager.addMapping(MAPPING_SPAWN_MONSTER, TRIGGER_SPAWN_MONSTER);
        inputManager.addMapping(MAPPING_SPAWN_WAVE, TRIGGER_SPAWN_WAVE);
        inputManager.addMapping(MAPPING_EXTRACTION_ESSENCE, TRIGGER_EXTRACTION_ESSENCE);
        inputManager.addMapping(MAPPING_BUY_ESSENCE, TRIGGER_BUY_ESSENCE);
        inputManager.addMapping(MAPPING_PUT_EXTRACTED_ESSENCE, TRIGGER_PUT_EXTRACTED_ESSENCE);
        inputManager.addMapping(MAPPING_SELL_ESSENCE, TRIGGER_SELL_ESSENCE);

        inputManager.addListener(new ListenerForCell(),
                MAPPING_MAKE_PASSABLE,
                MAPPING_RESET);

        inputManager.addListener(new ListenerForBuilding(),
                MAPPING_BUILD_WALL,
                MAPPING_BUILD_TOWER,
                MAPPING_BUILD_PORTAL,
                MAPPING_BUILD_FORTRESS);

        inputManager.addListener(new ListenerForMonsters(),
                MAPPING_SPAWN_MONSTER,
                MAPPING_SPAWN_WAVE);

        inputManager.addListener(new ListenerForEssence(),
                MAPPING_BUY_ESSENCE,
                MAPPING_SELL_ESSENCE,
                MAPPING_EXTRACTION_ESSENCE,
                MAPPING_PUT_EXTRACTED_ESSENCE);
//        Configuration.getInputManager().addListener(actionListener, new String[]{MAPPING_MAKE_PASSABLE});
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