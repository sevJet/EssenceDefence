package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.building.Building;

import java.io.Serializable;

public class Tower extends Building implements Serializable {

    private int price = 0;

    public Tower() {
        this(0);
    }

    public Tower(int price) {
        super(2, 2, 3);

        setPrice(price);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
