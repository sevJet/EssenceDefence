package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.IBuyable;
import io.github.sevjet.essensedefence.util.BoxSize;

public class BuyableBuilding extends Building implements IBuyable {

    protected float price = 0f;

    public BuyableBuilding() {
        super();
    }

    public BuyableBuilding(BoxSize size, float health, float price) {
        super(size, health);
        setPrice(price);
    }

    public BuyableBuilding(int x, int y, BoxSize size, float health, float price) {
        super(x, y, size, health);
        setPrice(price);
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price < 0f) {
            throw new IllegalArgumentException("Price can not be negative");
        }

        this.price = price;
    }

}
