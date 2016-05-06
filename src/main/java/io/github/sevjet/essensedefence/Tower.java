package io.github.sevjet.essensedefence;

import java.io.Serializable;

public class Tower extends Building implements Serializable {

    private int price = 0;

    public Tower() {
        this(0);
    }

    public Tower(int price) {
        setHeight(2);
        setWidth(2);

        setPrice(price);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
