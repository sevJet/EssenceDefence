package io.github.sevjet.essensedefence.control;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;

public class MonsterControl extends BasicControl {

    private Monster monster = null;
    private Fortress fortress = null;
    private MotionPath path = new MotionPath();
    private MotionEvent event = new MotionEvent();

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (entity instanceof Monster) {
            monster = (Monster) entity;
        } else {
            throw new IllegalArgumentException("Not a Monster spatial");
        }

        event.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (fortress == null) {
            findFortress();
        }
        if (fortress != null) {
            if (path.getNbWayPoints() == 0) {
                buildPath();
            }
        } else {
            // @TODO turn this Control off or delay
            System.out.println("No fortress found");
        }
    }

    private void findFortress() {
        Field field = entity.getField();
        if (field != null) {
            Node fortressNode = field.getObjects(Fortress.class);
            if (fortressNode != null) {
                Spatial spatial = fortressNode.getChildren().size() > 0 ? fortressNode.getChild(0) : null;
                if (spatial != null) {
                    fortress = spatial.getUserData("entity");
                }
            }
        }
    }

    private void buildPath() {
        // Start point
        path.addWayPoint(new Vector3f(entity.getX(), entity.getY(), entity.getZ()));
        // Finish point
        path.addWayPoint(fortress.getCenter());

        path.addListener(new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
                if (motionControl.getPath().getNbWayPoints() == wayPointIndex + 1) {
                    fortress.hit(monster.getDamage());
                    monster.die();
                }
            }
        });
        path.setPathSplineType(Spline.SplineType.Linear);

        event.setSpatial(spatial);
        event.setPath(path);
        event.setSpeed(monster.getSpeed());
        event.setDirectionType(MotionEvent.Direction.Path);

        spatial.addControl(event);

        event.play();
    }

}
