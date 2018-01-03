package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.InventoryCell;
import io.github.sevjet.essensedefence.field.MapCell;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.Getter;


public final class ListenerManager {

    public final static Trigger TRIGGER_MAKE_PASSABLE =
            new KeyTrigger(KeyInput.KEY_E);
    public final static Trigger TRIGGER_RESET =
            new KeyTrigger(KeyInput.KEY_R);
    //  TODO change
    public final static Trigger TRIGGER_BUILD_WALL =
            new KeyTrigger(KeyInput.KEY_W);
    public final static Trigger TRIGGER_BUILD_TOWER =
            new KeyTrigger(KeyInput.KEY_T);
    public final static Trigger TRIGGER_BUILD_PORTAL =
            new KeyTrigger(KeyInput.KEY_P);
    public final static Trigger TRIGGER_BUILD_FORTRESS =
            new KeyTrigger(KeyInput.KEY_F);

    public final static Trigger TRIGGER_SPAWN_MONSTER =
            new KeyTrigger(KeyInput.KEY_M);
    public final static Trigger TRIGGER_SPAWN_WAVE =
            new KeyTrigger(KeyInput.KEY_G);
    public final static Trigger TRIGGER_SPAWN_ALL =
            new KeyTrigger(KeyInput.KEY_L);

    public final static Trigger TRIGGER_BUY_ESSENCE =
            new KeyTrigger(KeyInput.KEY_B);
    public final static Trigger TRIGGER_EXTRACTION_ESSENCE =
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    public final static Trigger TRIGGER_PUT_EXTRACTED_ESSENCE =
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    public final static Trigger TRIGGER_SELL_ESSENCE =
            new KeyTrigger(KeyInput.KEY_S);
    public final static Trigger TRIGGER_UPGRADE_ESSENCE =
            new KeyTrigger(KeyInput.KEY_U);
    public final static Trigger TRIGGER_COMBINE_ESSENCE =
            new KeyTrigger(KeyInput.KEY_C);

    public final static Trigger TRIGGER_INFO =
            new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);

    public final static Trigger TRIGGER_EXIT =
            new KeyTrigger(KeyInput.KEY_ESCAPE);

    public final static String MAPPING_MAKE_PASSABLE = "Make passable";
    public final static String MAPPING_RESET = "Reset";

    public final static String MAPPING_BUILD_WALL = "Build wall";
    public final static String MAPPING_BUILD_TOWER = "Build tower";
    public final static String MAPPING_BUILD_PORTAL = "Build portal";
    public final static String MAPPING_BUILD_FORTRESS = "Build fortress";

    public final static String MAPPING_SPAWN_MONSTER = "Spawn monster";
    public final static String MAPPING_SPAWN_WAVE = "Spawn wave";
    public final static String MAPPING_SPAWN_ALL = "Spawn all";

    public final static String MAPPING_BUY_ESSENCE = "Buy essence";
    public final static String MAPPING_SELL_ESSENCE = "Sell essence";
    public final static String MAPPING_PUT_EXTRACTED_ESSENCE = "Put extracted essence";
    public final static String MAPPING_EXTRACTION_ESSENCE = "Extraction essence";
    public final static String MAPPING_UPGRADE_ESSENCE = "Upgrade essence";
    public final static String MAPPING_COMBINE_ESSENCE = "Combine essence";

    public final static String MAPPING_INFO = "Info";

    public final static String MAPPING_EXIT = "Exit";

    public static void registerListener() {
        InputManager inputManager = Configuration.getInputManager();
        inputManager.addMapping(MAPPING_MAKE_PASSABLE, TRIGGER_MAKE_PASSABLE);
        inputManager.addMapping(MAPPING_RESET, TRIGGER_RESET);

        inputManager.addMapping(MAPPING_BUILD_WALL, TRIGGER_BUILD_WALL);
        inputManager.addMapping(MAPPING_BUILD_TOWER, TRIGGER_BUILD_TOWER);
        inputManager.addMapping(MAPPING_BUILD_PORTAL, TRIGGER_BUILD_PORTAL);
        inputManager.addMapping(MAPPING_BUILD_FORTRESS, TRIGGER_BUILD_FORTRESS);

        inputManager.addMapping(MAPPING_SPAWN_MONSTER, TRIGGER_SPAWN_MONSTER);
        inputManager.addMapping(MAPPING_SPAWN_WAVE, TRIGGER_SPAWN_WAVE);
        inputManager.addMapping(MAPPING_SPAWN_ALL, TRIGGER_SPAWN_ALL);

        inputManager.addMapping(MAPPING_EXTRACTION_ESSENCE, TRIGGER_EXTRACTION_ESSENCE);
        inputManager.addMapping(MAPPING_BUY_ESSENCE, TRIGGER_BUY_ESSENCE);
        inputManager.addMapping(MAPPING_PUT_EXTRACTED_ESSENCE, TRIGGER_PUT_EXTRACTED_ESSENCE);
        inputManager.addMapping(MAPPING_SELL_ESSENCE, TRIGGER_SELL_ESSENCE);
        inputManager.addMapping(MAPPING_UPGRADE_ESSENCE, TRIGGER_UPGRADE_ESSENCE);
        inputManager.addMapping(MAPPING_COMBINE_ESSENCE, TRIGGER_COMBINE_ESSENCE);

        inputManager.addMapping(MAPPING_INFO, TRIGGER_INFO);

        inputManager.addMapping(MAPPING_EXIT, TRIGGER_EXIT);

        inputManager.addListener(new CellListener(),
                MAPPING_MAKE_PASSABLE,
                MAPPING_RESET);

        inputManager.addListener(new BuildingListener(),
                MAPPING_BUILD_WALL,
                MAPPING_BUILD_TOWER,
                MAPPING_BUILD_PORTAL,
                MAPPING_BUILD_FORTRESS);

        inputManager.addListener(new MonsterListener(),
                MAPPING_SPAWN_MONSTER,
                MAPPING_SPAWN_WAVE,
                MAPPING_SPAWN_ALL);

        inputManager.addListener(new EssenceListener(),
                MAPPING_BUY_ESSENCE,
                MAPPING_SELL_ESSENCE,
                MAPPING_EXTRACTION_ESSENCE,
                MAPPING_PUT_EXTRACTED_ESSENCE,
                MAPPING_UPGRADE_ESSENCE,
                MAPPING_COMBINE_ESSENCE,
                MAPPING_INFO);

        inputManager.addListener(new GameListener(),
                MAPPING_EXIT);
    }

    static CollisionResults rayCasting() {
        return rayCasting(getField());
    }

    static CollisionResults rayCasting(Node... objects) {
        CollisionResults results = new CollisionResults();
        Ray ray = fromCursor();
        for (Node with : objects) {
            if (with != null) {
                with.collideWith(ray, results);
                if (results.size() > 0) {
                    break;
                }
            }
        }
        return results;
    }

    static Cell getCell(CollisionResults results) {
        Geometry target = results.getClosestCollision().getGeometry();
        return (Cell) Getter.getEntity(target);
    }

    static Node getField() {
        return GamePlayAppState.field != null ? GamePlayAppState.field.getObjects(MapCell.class) : null;
    }

    static Node getInventory() {
        return Configuration.getGamer().getInventory().getObjects(InventoryCell.class);
    }

    static Node getShop() {
        return Configuration.getGamer().getShop().getObjects(InventoryCell.class);
    }

    private static Ray fromCamera() {
        return new Ray(Configuration.getCam().getLocation(),
                Configuration.getCam().getDirection());
    }

    private static Ray fromCursor() {
        Vector2f click2d = Configuration.getInputManager().getCursorPosition();
        Vector3f click3d = Configuration.getCam().getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 0f);

        Vector3f dir = Configuration.getCam().getWorldCoordinates(
                new Vector2f(click2d.getX(), click2d.getY()), 1f).
                subtract(click3d).normalize();

        Vector3f dir2 = Configuration.getCam().getDirection()
                .subtract(Configuration.getCam().getWorldCoordinates(
                        new Vector2f(
                                Configuration.getSettings().getWidth() / 2f,
                                Configuration.getSettings().getHeight() / 2f),
                        0f))
                .add(click3d).normalize();

        System.out.println(click3d);
//        System.out.println(dir);
//        System.out.println("--");
//        System.out.println(dir2.add(click3d).normalize());
//        System.out.println(dir2.add(click3d.normalize()).normalize());
//        System.out.println("--");
//        dir2 = dir2.negate();
//        System.out.println(dir2.add(click3d).normalize());
//        System.out.println(dir2.add(click3d.normalize()).normalize());
//        System.out.println("--");
//        System.out.println("========");
//        System.out.println(dir2.subtract(click3d).normalize());
//        System.out.println(dir2.subtract(click3d.normalize()).normalize());
//        System.out.println("--");
//        dir2 = dir2.negate();
//        System.out.println(dir2.subtract(click3d).normalize());
//        System.out.println(dir2.subtract(click3d.normalize()).normalize());
//        System.out.println("--");
//        System.out.println(Configuration.getCam().getDirection());
//        System.out.println(Configuration.getCam().getDirection().negate());
        System.out.println();


//        Configuration.getRootNode().attachChild(
//                Creator.myLine(
//                        Vector3f.ZERO,
//                        click3d,
//                        ColorRGBA.Red,
//                        5f
//                ));
//        Configuration.getRootNode().attachChild(
//                Creator.myLine(
//                        Configuration.getCam().getLocation(),
//                        dir2.mult(100).add(Configuration.getCam().getLocation()),
//                        ColorRGBA.Blue,
//                        44f
//                ));
        return new Ray(click3d, dir);
    }
}
