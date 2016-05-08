package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.util.BoxSize;

public class Tower extends BuyableBuilding {

    private static final BoxSize SIZE = new BoxSize(2, 2, 3);

    public Tower() {
        super(SIZE, -1f, 0f);
    }

    public Tower(float health, float price) {
        super(SIZE, health, price);
    }

    public Tower(int x, int y, float health, float price) {
        super(x, y, SIZE, health, price);
    }

}
