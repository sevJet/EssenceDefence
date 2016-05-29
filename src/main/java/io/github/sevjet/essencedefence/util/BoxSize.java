package io.github.sevjet.essencedefence.util;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import java.io.IOException;

public class BoxSize implements Savable, Cloneable {

    public static final BoxSize FLAT = new BoxSize(1, 1, 0);
    public static final BoxSize NONE = new BoxSize(0, 0, 0);

    private int height;
    private int width;
    private int depth;
    private boolean isConstructed = false;

    public BoxSize() {
        height = 0;
        width = 0;
        depth = 0;
    }

    public BoxSize(int height, int width, int depth) {
        if (height < 0 || width < 0 || depth < 0) {
            throw new IllegalArgumentException("None of parameters can be less then zero");
        }
        this.height = height;
        this.width = width;
        this.depth = depth;
        isConstructed = true;
    }

    public int getHeight() {
        return height;
    }

    public BoxSize setHeight(int height) {
        return new BoxSize(height, width, depth);
    }

    public int getWidth() {
        return width;
    }

    public BoxSize setWidth(int width) {
        return new BoxSize(height, width, depth);
    }

    public int getDepth() {
        return depth;
    }

    public BoxSize setDepth(int depth) {
        return new BoxSize(height, width, depth);
    }

    @Override
    public String toString() {
        return String.format("H: %d; W: %d; D: %d;", height, width, depth);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BoxSize) {
            BoxSize size = (BoxSize) obj;
            return height == size.height &&
                    width == size.width &&
                    depth == size.depth;
        }
        return false;
    }

    @Override
    protected BoxSize clone() throws CloneNotSupportedException {
        return (BoxSize) super.clone();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(height, "height", 1);
        capsule.write(width, "width", 1);
        capsule.write(depth, "depth", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        if (isConstructed) {
            throw new IOException("BoxSize already constructed, create new object to read");
        }
        InputCapsule capsule = im.getCapsule(this);
        height = capsule.readInt("height", 1);
        width = capsule.readInt("width", 1);
        depth = capsule.readInt("depth", 0);
        isConstructed = true;
    }
}
