package io.github.sevjet.essensedefence;

public class Wall extends Building {

    protected int price = 0;

    public Wall() {
        this(0);
    }

    public Wall(int price) {
        setHeight(1);
        setWidth(1);

        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
