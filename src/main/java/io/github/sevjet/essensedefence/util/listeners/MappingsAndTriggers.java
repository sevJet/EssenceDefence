package io.github.sevjet.essensedefence.util.listeners;

import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Getter;

public final class MappingsAndTriggers {

    public final static Trigger TRIGGER_MAKE_PASSABLE =
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
    public final static Trigger TRIGGER_SPAWN_MONSTER =
            new KeyTrigger(KeyInput.KEY_F);
    public final static Trigger TRIGGER_SPAWN_WAVE =
            new KeyTrigger(KeyInput.KEY_G);
    public final static Trigger TRIGGER_BUY_ESSENCE =
            new KeyTrigger(KeyInput.KEY_B);
    public final static Trigger TRIGGER_EXTRACTION_ESSENCE =
            new KeyTrigger(KeyInput.KEY_N);
    public final static Trigger TRIGGER_PUT_EXTRACTED_ESSENCE =
            new KeyTrigger(KeyInput.KEY_P);
    public final static Trigger TRIGGER_SELL_ESSENCE =
            new KeyTrigger(KeyInput.KEY_M);

    public final static String MAPPING_MAKE_PASSABLE = "Make passable";
    public final static String MAPPING_RESET = "Reset";
    public final static String MAPPING_BUILD_WALL = "Build wall";
    public final static String MAPPING_BUILD_TOWER = "Build tower";
    public final static String MAPPING_BUILD_PORTAL = "Build portal";
    public final static String MAPPING_BUILD_FORTRESS = "Build fortress";
    public final static String MAPPING_SPAWN_MONSTER = "Spawn monster";
    public final static String MAPPING_SPAWN_WAVE = "Spawn wave";
    public final static String MAPPING_BUY_ESSENCE = "Buy essence";
    public final static String MAPPING_EXTRACTION_ESSENCE = "Extraction essence";
    public final static String MAPPING_PUT_EXTRACTED_ESSENCE = "Put extracted essence";
    public final static String MAPPING_SELL_ESSENCE = "Sell essence";

    public static CollisionResults rayCasting() {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(Configuration.getCam().getLocation(), Configuration.getCam().getDirection());
        //TODO fix building
        GamePlayAppState.field.getObjects(Cell.class).collideWith(ray, results);
//        ((Field)Configuration.getRootNode().getChild("field")).objects.get(Cell.class).collideWith(ray, results);
//        System.out.println(Field.objects.get(Cell.class).collideWith(ray, results));
//        if (results.size() > 0){
//            for (CollisionResult i : results){
//                System.out.println(i.getGeometry().getName());
//            }
//        }
        return results;
    }

    public static Cell getCell(CollisionResults results) {
        Geometry target = results.getClosestCollision().getGeometry();
        Cell cell = (Cell) Getter.getEntity(target);
        return cell;
    }
}
