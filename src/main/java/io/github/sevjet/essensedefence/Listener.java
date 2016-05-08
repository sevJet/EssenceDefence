package io.github.sevjet.essensedefence;

import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;

//TODO change on anonymous class
public class Listener implements AnalogListener {

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
    public final static Trigger TRIGGER_SPAWN_MONSTER =
            new KeyTrigger(KeyInput.KEY_F);

    public final static String MAPPING_BUILD = "Build";
    public final static String MAPPING_RESET = "Reset";
    public final static String MAPPING_BUILD_WALL = "Build wall";
    public final static String MAPPING_BUILD_TOWER = "Build tower";
    public final static String MAPPING_BUILD_PORTAL = "Build portal";
    public final static String MAPPING_BUILD_FORTRESS = "Build fortress";
    public final static String MAPPING_SPAWN_MONSTER = "Spawn monster";

    //TODO change
    public CollisionResults rayCasting() {
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(Configuration.getCam().getLocation(), Configuration.getCam().getDirection());
        //TODO fix static
        GamePlayAppState.field.objects.get(Cell.class).collideWith(ray, results);
//        ((Field)Configuration.getRootNode().getChild("field")).objects.get(Cell.class).collideWith(ray, results);
//        System.out.println(Field.objects.get(Cell.class).collideWith(ray, results));
//        if (results.size() > 0){
//            for (CollisionResult i : results){
//                System.out.println(i.getGeometry().getName());
//            }
//        }
        return results;
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MAPPING_BUILD) ||
                name.equals(MAPPING_RESET) ||
                name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS) ||
                name.equals(MAPPING_SPAWN_MONSTER)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
                Cell temp = new Cell();
                Geometry target = results.getClosestCollision().getGeometry();
                temp.setGeometry(target);
                Field field = temp.getField();
                //TODO change
                if (true) {
                    Cell cell = field.getCell(target);
                    switch (name) {
                        case MAPPING_RESET:
                            //TODO change on casting buildings
                            cell.removeBuilding();
                        case MAPPING_BUILD:
                            cell.setPassably(name.equals(MAPPING_BUILD));
                            break;
                        case MAPPING_BUILD_WALL:
                            cell.build(new Wall());
                            break;
                        case MAPPING_BUILD_TOWER:
                            cell.build(new Tower());
                            break;
                        case MAPPING_BUILD_PORTAL:
                            cell.build(new Portal());
                            break;
                        case MAPPING_BUILD_FORTRESS:
                            cell.build(new Fortress());
                            break;
                        case MAPPING_SPAWN_MONSTER:
                            field.addObject(
                                    new Monster(100, 10, 10, cell.getX(), cell.getY()));
                            break;
                    }
                }
            }
        }
    }
}
