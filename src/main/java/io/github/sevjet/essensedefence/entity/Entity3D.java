package io.github.sevjet.essensedefence.entity;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import io.github.sevjet.essensedefence.util.GeometryManager;

import java.io.IOException;

public abstract class Entity3D extends Entity {

    private int height;
    private int width;
    private int depth;

    public Entity3D(int height, int width, int depth) {
        this(0, 0, height, width, depth);
    }

    public Entity3D(int x, int y) {
        this(x, y, 1, 1, 0);
    }

    public Entity3D(int x, int y, int height, int width, int depth) {
        super(x, y);

        setSize(height, width, depth);
    }

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

    public int getDepth() {
        return depth;
    }

    protected void setDepth(int depth) {
        this.depth = depth;
    }

    protected void setSize(int height, int width, int depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(height, "height", 1);
        capsule.write(width, "width", 1);
        capsule.write(depth, "depth", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        height = capsule.readInt("height", 1);
        width = capsule.readInt("width", 1);
        depth = capsule.readInt("depth", 0);
    }

    @Override
    protected boolean updater() {
        if (geometry == null) {
            geometry = GeometryManager.getDefault(this.getClass());
            if (geometry == null) {
                geometry = GeometryManager.getDefault(Entity.class);
            }
        }
        if (geometry != null) {
            geometry.setLocalTranslation(x + (width - 1) / 2.0F, y + (height - 1) / 2.0F, z + (depth) / 2.0F);
            return true;
        }
        return false;
    }
}
