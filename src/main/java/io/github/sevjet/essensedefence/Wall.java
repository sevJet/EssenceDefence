package io.github.sevjet.essensedefence;

import java.io.Serializable;

public class Wall extends Building implements Serializable {

    protected int price = 0;

    public Wall() {
        this(0);
    }

    public Wall(int price) {
        super(1, 1, 2);

        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
