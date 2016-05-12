package io.github.sevjet.essensedefence.entity.monster;

import io.github.sevjet.essensedefence.control.BasicControl;
import io.github.sevjet.essensedefence.control.MonsterControl;
import io.github.sevjet.essensedefence.entity.Entity3D;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;

public class Monster extends Entity3D {

    private static final BoxSize SIZE = new BoxSize(1, 1, 1);
    private final BasicControl control = new MonsterControl();
    //    private final MotionPath path;
//    private final MotionEvent motionControl;
    private float health = 0f;
    private float speed = 0f;
    private float damage = 0f;
    private float exp = 1f;
    private float money = 10f;

    public Monster() {
        super(SIZE);
        geometry.addControl(control);
    }

    public Monster(float health, float speed, float damage) {
        super(SIZE);

        this.health = health;
        this.speed = speed;
        this.damage = damage;
        geometry.addControl(control);
    }

    public Monster(int x, int y, float health, float speed, float damage) {
        super(x, y, SIZE);

        this.health = health;
        this.speed = speed;
        this.damage = damage;
        geometry.addControl(control);

//        path = new MotionPath();
//        path.addWayPoint(new Vector3f(x, y, z));
//        path.setPathSplineType(Spline.SplineType.Linear);
//        path.addListener(new MotionPathListener() {
//            @Override
//            public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
//                x = Math.round(path.getWayPoint(wayPointIndex).getX());
//                y = Math.round(path.getWayPoint(wayPointIndex).getY());
//                if (wayPointIndex + 1 == path.getNbWayPoints()) {
//                    motionControl.stop();
//                    path.clearWayPoints();
//                    path.addWayPoint(new Vector3f(x, y, z));
//                }
//            }
//        });
//        motionControl = new MotionEvent(getGeometry(), path);
//        motionControl.setSpeed(speed);
//        motionControl.setDirectionType(MotionEvent.Direction.Path);
//        motionControl.setDirection(motionControl.getDirection(), Vector3f.UNIT_Z);
//        motionControl.setInitialDuration(10f);
    }

    public float getHealth() {
        return health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getExp() {
        return exp;
    }

    public void setExp(float exp) {
        this.exp = exp;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public double hit(float damage) {
        this.health -= damage;

        if (this.health <= 0f) {
            giveReward();
            die();
        }
        return this.health;
    }

    private void giveReward() {
        Configuration.getGamer().incGold(money);
    }

    // TODO: 12/05/2016 change to protected
    @Deprecated
    public void die() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this);
        }
        System.out.println("Monster.die()");
    }
}
