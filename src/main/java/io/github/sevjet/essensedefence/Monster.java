package io.github.sevjet.essensedefence;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;

public class Monster extends JME3Object {

    private final MotionPath path;
    private final MotionEvent motionControl;
    private float health = 0f;
    private float speed = 0f;
    private float damage = 0f;

    public Monster() {
        this(0, 0);
    }

    public Monster(int x, int y) {
        this(0f, 0f, 0f, x, y);
    }

    public Monster(float health, float speed, float damage, int _x, int _y) {
        super(_x, _y);
        this.health = health;
        this.speed = speed;
        this.damage = damage;

        path = new MotionPath();
        path.addWayPoint(new Vector3f(x, y, z));
//        path.setPathSplineType(Spline.SplineType.Linear);
        path.addListener(new MotionPathListener() {
            @Override
            public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
                x = Math.round(path.getWayPoint(wayPointIndex).getX());
                y = Math.round(path.getWayPoint(wayPointIndex).getY());
                if (wayPointIndex + 1 == path.getNbWayPoints()) {
                    motionControl.stop();
                    path.clearWayPoints();
                    path.addWayPoint(new Vector3f(x, y, z));
                }
            }
        });
        motionControl = new MotionEvent(getGeometry(), path);
        motionControl.setSpeed(speed);
        motionControl.setDirectionType(MotionEvent.Direction.Path);
        motionControl.setDirection(motionControl.getDirection(), Vector3f.UNIT_Z);
        motionControl.setInitialDuration(10f);
    }

    public double getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public double hit(float damage) {
        this.health -= damage;

        if (this.health <= 0f) {
            die();
        }
        return this.health;
    }

    @Override
    public void moveRelative(int x, int y) {
        Vector3f lastPoint = path.getWayPoint(path.getNbWayPoints() - 1);
        x += lastPoint.getX();
        y += lastPoint.getY();
        //moveRaw(x, y);
        path.setCycle(true);
        motionControl.setLoopMode(LoopMode.Loop);
        if (motionControl.getPlayState() != PlayState.Playing) {
            motionControl.play();
        }
    }

    @Override
    public void move(int x, int y) {
        path.addWayPoint(new Vector3f(x, y, z));
        if (motionControl.getPlayState() != PlayState.Playing) {
            motionControl.play();
        }
    }

    public void moveRelativeRaw(int x, int y) {
        Vector3f lastPoint = path.getWayPoint(path.getNbWayPoints() - 1);
        x += lastPoint.getX();
        y += lastPoint.getY();
        System.out.println(String.format("Move relative raw x: %d; y: %d;", x, y));
        moveRaw(x, y);
    }

    public void moveRaw(int x, int y) {
        path.addWayPoint(new Vector3f(x, y, z));
    }

    private void die() {

    }
}
