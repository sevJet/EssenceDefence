package io.github.sevjet.essencedefence.entity.building;

import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.gui.ITextual;
import io.github.sevjet.essencedefence.util.BoxSize;


public class Fortress extends Building implements ITextual {

    public static final BoxSize SIZE = new BoxSize(3, 3, 4);

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
        update();
    }

    private void die() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this);
        }

        destroy();
    }

    @Override
    public boolean update() {
        super.update();
        return true;
    }

    @Override
    public String outputValue() {
        return Float.toString(health);
    }

    @Override
    public boolean isEnded() {
        return (health <= 0 || isDestroyed() || geometry.getParent() == null);
    }

}
