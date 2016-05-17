package io.github.sevjet.essensedefence.control;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class MonsterControl extends BasicControl {

    private Monster monster = null;
    private Fortress fortress = null;
    private MotionPath path = null;
    private MotionEvent event = null;
    private long delayTill = 0L;

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (entity instanceof Monster) {
            monster = (Monster) entity;
        } else {
            throw new IllegalArgumentException("Not a Monster spatial");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (System.currentTimeMillis() <= delayTill) {
            return;
        }
        if (fortress == null || fortress.isEnded()) {
            findFortress();
            if (event != null) {
                event.stop();
                spatial.removeControl(MotionEvent.class);
                event = null;
            }
            path = null;
            entity.setZ(0);
        }
        if (fortress != null) {
            if (path == null || path.getNbWayPoints() == 0) {
                initPath();
                delayTill = System.currentTimeMillis() + 1000L;
            } else {
                delayTill = System.currentTimeMillis() + 1000L;
            }
        } else {
            System.out.println("No fortress found");
            delayTill = System.currentTimeMillis() + 1000L;
        }
    }

    private void findFortress() {
        fortress = null;
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

    private void initPath() {
        buildPath();
        if (path == null) {
            delayTill = System.currentTimeMillis() + 1000L;
            return;
        }

        path.addListener(new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
                if (motionControl.getPath().getNbWayPoints() == wayPointIndex + 1) {
                    fortress.hit(monster.getDamage());
                    monster.die();
                }
                Vector3f curWayPoint = motionControl.getPath().getWayPoint(motionControl.getCurrentWayPoint());
                monster.setX(Math.round(curWayPoint.getX()));
                monster.setY(Math.round(curWayPoint.getY()));
            }
        });
        path.setPathSplineType(Spline.SplineType.Linear);

        event = new MotionEvent(spatial, path);
        event.setInitialDuration(path.getNbWayPoints());
        event.setSpeed(1);
        event.setDirectionType(MotionEvent.Direction.PathAndRotation);

        event.play();
    }

    private void buildPath() {
        final Field field = entity.getField();
        final int rows = field.getRows();
        final int cols = field.getCols();
        final int passable[][] = field.getPassable();
        final Queue<Integer> queue = new LinkedList<>();
        queue.add(Math.round(fortress.getCenter().getX() * cols + fortress.getCenter().getY()));
        passable[Math.round(fortress.getCenter().getX())][Math.round(fortress.getCenter().getY())] = -2;
        queue.element();
        boolean isFound = false;
        int curVer, x = -1, y = -1;
        while (!isFound && queue.size() > 0) {
            curVer = queue.poll();
            x = curVer / cols;
            y = curVer % cols;
            if (x == entity.getX() && y == entity.getY()) {
                isFound = true;
                continue;
            }
            if ((x + 1 < cols) && passable[x + 1][y] == 0) {
                passable[x + 1][y] = curVer + 1;
                queue.add((x + 1) * cols + y);
            }
            if ((x - 1 >= 0) && passable[x - 1][y] == 0) {
                passable[x - 1][y] = curVer + 1;
                queue.add((x - 1) * cols + y);
            }
            if ((y + 1 < rows) && passable[x][y + 1] == 0) {
                passable[x][y + 1] = curVer + 1;
                queue.add(x * cols + (y + 1));
            }
            if ((y - 1 >= 0) && passable[x][y - 1] == 0) {
                passable[x][y - 1] = curVer + 1;
                queue.add(x * cols + (y - 1));
            }
        }
        if (isFound) {
            path = new MotionPath();
            while (x != Math.round(fortress.getCenter().getX()) ||
                    y != Math.round(fortress.getCenter().getY())) {
                path.addWayPoint(new Vector3f(x, y, entity.getCenter().getZ()));
                curVer = passable[x][y] - 1;
                x = curVer / cols;
                y = curVer % cols;
            }
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(monster, "monster", null);
        capsule.write(fortress, "fortress", null);
        capsule.write(path, "path", null);
        capsule.write(event, "event", null);
        capsule.write(delayTill, "delayTill", 0L);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        monster = (Monster) capsule.readSavable("monster", null);
        fortress = (Fortress) capsule.readSavable("fortress", null);
        path = (MotionPath) capsule.readSavable("path", null);
        event = (MotionEvent) capsule.readSavable("event", null);
        delayTill = capsule.readLong("delayTill", 0L);
    }

}
