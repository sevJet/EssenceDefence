package io.github.sevjet.essensedefence.control;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;

import java.util.LinkedList;
import java.util.Queue;

public class MonsterControl extends BasicControl {

    private Monster monster = null;
    private Fortress fortress = null;
    private MotionPath path = null;
    private MotionEvent event = new MotionEvent();
    private long delayTill = 0L;

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
        if (System.currentTimeMillis() <= delayTill) {
            return;
        }
        if (fortress == null || fortress.isEnded()) {
            findFortress();
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

        event.setSpatial(spatial);
        event.setPath(path);
        event.setInitialDuration(path.getNbWayPoints());
        event.setSpeed(1);
        event.setDirectionType(MotionEvent.Direction.Path);

        spatial.addControl(event);

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
            if (passable[x + 1][y] == 0) {
                passable[x + 1][y] = curVer + 1;
                queue.add((x + 1) * cols + y);
            }
            if (passable[x - 1][y] == 0) {
                passable[x - 1][y] = curVer + 1;
                queue.add((x - 1) * cols + y);
            }
            if (passable[x][y + 1] == 0) {
                passable[x][y + 1] = curVer + 1;
                queue.add(x * cols + (y + 1));
            }
            if (passable[x][y - 1] == 0) {
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

}
