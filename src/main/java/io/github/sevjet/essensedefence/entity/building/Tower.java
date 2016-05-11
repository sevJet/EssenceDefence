package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;

public class Tower extends BuyableBuilding {

    private static final BoxSize SIZE = new BoxSize(2, 2, 3);

    private Essence core;

    public Tower() {
        super(SIZE, -1f, 0f);
    }

    public Tower(float health, float price) {
        super(SIZE, health, price);
    }

    public Tower(int x, int y, float health, float price) {
        super(x, y, SIZE, health, price);
    }

    public Essence getCore() {
        return core;
    }

    public void putCore(Essence core) {
        if (this.core == null) {
            this.core = core;
            placeCore();
        }
    }

    private void placeCore() {
        core.setX(x);
        core.setY(y);
        core.setZ(getSize().getDepth());

        Field field = getField();
        if (field != null) {
            field.addObject(core);
            System.out.println("Core placed");
        }
    }

}
