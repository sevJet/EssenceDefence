package io.github.sevjet.essencedefence.entity.building;

import io.github.sevjet.essencedefence.util.BoxSize;

public class Wall extends BuyableBuilding {

    public static final BoxSize SIZE = new BoxSize(1, 1, 2);

    public Wall() {
        super(SIZE, -1f, 0f);
    }

    public Wall(float health, float price) {
        super(SIZE, health, price);
    }

    public Wall(int x, int y, float health, float price) {
        super(x, y, SIZE, health, price);
    }

}