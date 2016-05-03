package io.github.sevjet.essensedefence;

public class Wall extends Building {

    private int price;

    public Wall() {
        this(0);
    }

    public Wall(int price) {
        setHeight(1);
        setWidth(1);

        this.price = price;
    }

    @Override
    public void build() {

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
