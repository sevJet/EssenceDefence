package io.github.sevjet.essensedefence.util;

import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Ray;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.Gamer;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

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

    // TODO: 09/05/2016 delete this
    private static Integer counter = 0;
    private BitmapText text = Creator.text("Listener");

    {
        text.addControl(new AbstractControl() {
            private boolean flag = true;

            @Override
            protected void controlUpdate(float tpf) {
                if (flag)
                    text.setSize(text.getSize() + counter * tpf);
                else text.setSize(text.getSize() - counter * tpf);
                if (text.getSize() > 100)
                    flag = false;
                if (text.getSize() < 10)
                    flag = true;
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {

            }
        });
    }

    private void update() {
        text.setText((text.getText().split(" "))[0] + " " + counter);
    }
    // TODO: 09/05/2016 delete ene

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
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MAPPING_BUILD) ||
                name.equals(MAPPING_RESET) ||
                name.equals(MAPPING_BUILD_WALL) ||
                name.equals(MAPPING_BUILD_TOWER) ||
                name.equals(MAPPING_BUILD_PORTAL) ||
                name.equals(MAPPING_BUILD_FORTRESS) ||
                name.equals(MAPPING_SPAWN_MONSTER)) {
            // TODO: 09/05/2016 delete this
            counter++;
            update();
            // TODO: 09/05/2016 end

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0) {
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
                                    new Monster(cell.getX(), cell.getY(), 100, 10, 10));
                            break;
                    }
                }
            }
        }
    }
}
