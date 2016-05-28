package io.github.sevjet.essencedefence.entity;

import com.jme3.math.Vector3f;
import io.github.sevjet.essencedefence.util.BoxSize;

public abstract class Entity3D extends Entity {

    private final BoxSize size;

    public Entity3D() {
        this(0, 0);
    }

    public Entity3D(int x, int y) {
        this(x, y, BoxSize.FLAT);
    }

    public Entity3D(BoxSize size) {
        this(0, 0, size);
    }

    public Entity3D(int x, int y, BoxSize size) {
        super(x, y);

        this.size = size;
        update();
    }

    public BoxSize getSize() {
        return size;
    }

    @Override
    public Vector3f getCenter() {
        BoxSize size = this.size == null ? BoxSize.FLAT : this.size;
        return new Vector3f(x + (size.getWidth() - 1) / 2.0F,
                y + (size.getHeight() - 1) / 2.0F,
                z + (size.getDepth()) / 2.0F);
    }

}
