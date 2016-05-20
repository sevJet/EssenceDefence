package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.JmeDesktopSystem;
import com.jme3.system.JmeSystemDelegate;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.*;
import io.github.sevjet.essensedefence.listener.ListenerManager;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.GeometryManager;

import static io.github.sevjet.essensedefence.util.Creator.*;
import static io.github.sevjet.essensedefence.util.Tester.testText;

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

//        GeometryManager.setDefault(Cell.class, myBox(1 / 2f, 1 / 2f, "cell", ColorRGBA.Black));
        GeometryManager.setDefault(Cell.class, myQuad(1, 1, "cell", ColorRGBA.Black));
        GeometryManager.setDefault(MapCell.class, GeometryManager.getDefault(Cell.class));
        GeometryManager.setDefault(InventoryCell.class, GeometryManager.getDefault(Cell.class));
        GeometryManager.setDefault(Wall.class, myBox(1 / 2f, 1 / 2f, 1f, "wall", ColorRGBA.Cyan));
        GeometryManager.setDefault(Tower.class, myBox(1f, 1f, 1.5f, "tower", ColorRGBA.Green));
        GeometryManager.setDefault(Fortress.class, myBox(3 / 2f, 3 / 2f, 2f, "fortress", ColorRGBA.Gray));
        GeometryManager.setDefault(Portal.class, myBox(1f, 1 / 2f, 1.5f, "portal", ColorRGBA.Magenta));
        GeometryManager.setDefault(Monster.class, myBox(1 / 3f, 1 / 3f, 1 / 2f, "monster", ColorRGBA.Yellow));
        GeometryManager.setDefault(Essence.class, myShinySphere(1 / 2f, "essence", ColorRGBA.randomColor()));

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1, 0, -2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        Configuration.getRootNode().addLight(sun);


        Gamer gamer = new Gamer(100);
        Configuration.setGamer(gamer);
        gamer.setGui();
    }

    private void placeGameFields() {
        Node invent = Configuration.getGamer().getInventory();

        Configuration.getRootNode().attachChild(invent);
        invent.setLocalTranslation(40f, 10f, 1f);
        invent.scale(2.5f);
        invent.rotate(
                -90 * FastMath.DEG_TO_RAD,
                000 * FastMath.DEG_TO_RAD,
                000 * FastMath.DEG_TO_RAD
        );
//        grid.rotate(
//                -90 * FastMath.DEG_TO_RAD,
//                +90 * FastMath.DEG_TO_RAD,
//                000 * FastMath.DEG_TO_RAD
//        );

        field.setLocalTranslation(5, 10, 1);
        field.rotate(
                -90 * FastMath.DEG_TO_RAD,
                000 * FastMath.DEG_TO_RAD,
                000 * FastMath.DEG_TO_RAD
        );
        Configuration.getRootNode().attachChild(field);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        initStartData();
        testText();

//        field = load();
//        field.setLocalTranslation(-55, 10, 0);
//        Configuration.getRootNode().attachChild(field);

        field = new MapField(25, 25);

        placeGameFields();
    }

    @Override
    public void cleanup() {
//        if (field != null)
//            save(field);

        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }


}