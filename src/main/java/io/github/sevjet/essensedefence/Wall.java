package io.github.sevjet.essensedefence;

public class Wall extends Building {

    private double price;

    public Wall() {
        this(0.0D);
    }

    public Wall(double price) {
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

    public void setPrice(double price) {
        this.price = price;
    }
}
