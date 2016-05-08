package io.github.sevjet.essensedefence.entity;

import io.github.sevjet.essensedefence.util.BoxSize;

public abstract class Entity3D extends Entity {

    private final BoxSize size;

    public Entity3D() {
        this(0, 0);
    }

    public Entity3D(int x, int y) {
        this(x, y, new BoxSize(1, 1, 0));
    }

    public Entity3D(BoxSize size) {
        this(0, 0, size);
    }

    public Entity3D(int x, int y, BoxSize size) {
        super(x, y);

        this.size = size;
        updater();
    }

    public BoxSize getSize() {
        return size;
    }

    @Override
    protected boolean moveToCenter() {
        if (size == null) {
            return false;
        }
        geometry.setLocalTranslation(
                x + (size.getWidth() - 1) / 2.0F,
                y + (size.getHeight() - 1) / 2.0F,
                z + (size.getDepth()) / 2.0F);
        return true;
    }

}