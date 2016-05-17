package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.gui.ITextual;
import io.github.sevjet.essensedefence.util.BoxSize;


public class Fortress extends Building implements ITextual {

    public static final BoxSize SIZE = new BoxSize(3, 3, 4);

    public Fortress() {
        super(SIZE, -1f);

        tempMethod();
    }

    public Fortress(float health) {
        super(SIZE, health);

        tempMethod();
    }

    public Fortress(int x, int y, float health) {
        super(x, y, SIZE, health);

        tempMethod();
    }

    private void tempMethod() {
        newText("Health of fortress:", 0);
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
