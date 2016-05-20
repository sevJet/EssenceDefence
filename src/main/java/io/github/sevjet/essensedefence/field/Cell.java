package io.github.sevjet.essensedefence.field;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import io.github.sevjet.essensedefence.entity.Entity;

import java.io.IOException;

public class Cell<T extends Entity> extends Entity {

    protected T content = null;

    public Cell() {
        super(0, 0);
    }

    public Cell(int x, int y) {
        super(x, y);
    }

    public Cell(int x, int y, T content) {
        super(x, y);
        this.content = content;

        update();
    }

    public boolean hasContent() {
        return content != null;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;

        update();
    }

    protected void free() {
        setContent(null);
    }


    @Override
    protected boolean update() {
        if (super.update()) {
            getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (content != null ? 1 : 0),
                            0,
                            (content == null ? 1 : 0),
                            1));
            return true;
        }
        return false;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(content, "content", null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        content = (T) capsule.readSavable("content", null);
    }
}