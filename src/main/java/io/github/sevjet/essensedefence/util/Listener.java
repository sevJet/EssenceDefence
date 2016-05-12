package io.github.sevjet.essensedefence.util;

import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.entity.monster.Wave;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

import java.util.ArrayList;
import java.util.List;

//TODO change on anonymous class
public class Listener implements ActionListener {

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
    public final static Trigger TRIGGER_SPAWN_WAVE =
            new KeyTrigger(KeyInput.KEY_G);

    public final static String MAPPING_BUILD = "Build";
    public final static String MAPPING_RESET = "Reset";
    public final static String MAPPING_BUILD_WALL = "Build wall";
    public final static String MAPPING_BUILD_TOWER = "Build tower";
    public final static String MAPPING_BUILD_PORTAL = "Build portal";
    public final static String MAPPING_BUILD_FORTRESS = "Build fortress";
    public final static String MAPPING_SPAWN_MONSTER = "Spawn monster";
    public final static String MAPPING_SPAWN_WAVE = "Spawn wave";


    //TODO change
    public CollisionResults rayCasting() {
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

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_BUILD) ||
                name.equals(MAPPING_RESET) ||
                name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS) ||
                name.equals(MAPPING_SPAWN_MONSTER) ||
                name.equals(MAPPING_SPAWN_WAVE)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell temp = new Cell();
                Geometry target = results.getClosestCollision().getGeometry();
                Cell cell = ((Field) target.getParent().getParent()).getCell(target);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
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
                            if (cell.build(new Tower()))
                                ((Tower) cell.getBuilding()).putCore(new Essence(1, 5, 1, 1, 0));
                            break;
                        case MAPPING_BUILD_PORTAL:
                            cell.build(new Portal());
                            break;
                        case MAPPING_BUILD_FORTRESS:
                            cell.build(new Fortress(100f));
                            break;
                        case MAPPING_SPAWN_MONSTER:
                            field.addObject(
                                    new Monster(cell.getX(), cell.getY(), 3, 10, 10));
                            break;
                        case MAPPING_SPAWN_WAVE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Portal) {
                                System.out.println("Adding wave");
                                Portal portal = (Portal) cell.getBuilding();
                                List<Monster> monsters = new ArrayList<Monster>();
                                for(int i=0;i<10;i++) {
                                    monsters.add(new Monster(10f, 2f, 0f));
                                }
                                Wave wave = new Wave(monsters);
                                wave.setDelay(3000);
                                wave.setGap(1000);
                                portal.addWave(wave);
                                portal.pushWave();
                            }
                            break;
                    }
                }
            }
        }
    }
}
