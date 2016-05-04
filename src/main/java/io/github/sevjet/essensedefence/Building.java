package io.github.sevjet.essensedefence;

public abstract class Building extends JME3Object {

    protected int height;
    protected int width;

    public abstract void build();

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

}
