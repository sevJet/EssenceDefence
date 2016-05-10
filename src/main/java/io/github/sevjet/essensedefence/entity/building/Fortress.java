package io.github.sevjet.essensedefence.entity.building;

import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.control.ITextual;
import io.github.sevjet.essensedefence.control.TextControl;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;


public class Fortress extends Building implements ITextual {

    private static final BoxSize SIZE = new BoxSize(3, 3, 4);
    private final AbstractControl control = new TextControl(this, "Health of fortress", 0);

    // FIXME: 10/05/2016 make final

    public Fortress() {
        super(SIZE, -1f);
    }

    public Fortress(float health) {
        super(SIZE, health);
    }

    public Fortress(int x, int y, float health) {
        super(x, y, SIZE, health);

    }

    public void hit(float damage) {
        if (health != -1f) {
            health -= damage;
            if (health <= 0f) {
                die();
            }
        }
        updater();
    }

    private void die() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this);
        }
        System.out.println("Fortress.die()");
    }

    @Override
    public boolean updater() {
        super.updater();
        return true;
    }

    @Override
    public String outputValue() {
        return Float.toString(health);
    }

    @Override
    public boolean isEnded() {
        return (health <= 0 || geometry.getParent() == null);
    }
}
