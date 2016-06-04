package io.github.sevjet.essencedefence.field;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.util.Configuration;

import static io.github.sevjet.essencedefence.util.Creator.gridXY;

public abstract class Field<T extends Cell> extends Node {

    protected ArrayList<T> cells;
    protected Map<String, Node> objects;
    protected Node grid;
    private int rows;
    private int cols;

    @SuppressWarnings("unused")
    public Field() {
    }

    public Field(final int cols, final int rows) {
        this.rows = rows;
        this.cols = cols;
        this.setName("field");
        this.objects = new HashMap<>();
        final int len = cols * rows;
        cells = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final int x = i % cols;
            final int y = i / cols;
            final T cell = newCell(x, y);
            cells.add(cell);
            addObject(cell);
        }

        gridOn();
    }

    public static boolean save(Field field, File file) {
        if (field != null) {
            BinaryExporter exporter = BinaryExporter.getInstance();
            try {
                return exporter.save(field, file);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return false;
    }

    public static Field load(File file) {
        if (file != null && file.exists()) {
            Configuration.getAssetManager().registerLocator(file.getParent(), FileLocator.class);
            Spatial spatial = Configuration.getAssetManager().loadModel(file.getAbsolutePath());
            Configuration.getAssetManager().unregisterLocator(file.getParent(), FileLocator.class);
            return (Field) spatial;
        }
        return null;
    }

    protected abstract T newCell(final int x, final int y);

    public T getCell(final int x, final int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows) {
            return null;
        }
        final int index = y * cols + x;
        return cells.get(index);
    }

    @Deprecated
    public T getCell(final Geometry geom) {
        if (geom != null &&
                geom.getParent() != null &&
                geom.getParent().getParent() == this) {
            final int x = (int) geom.getLocalTranslation().getX();
            final int y = (int) geom.getLocalTranslation().getY();
            return getCell(x, y);
        }
        return null;
    }

    public Node getObjects(final Class<? extends Entity> clazz) {
        return objects.get(clazz.getName());
    }

    public boolean addObject(final Entity object) {
        Node node = objects.get(object.getClass().getName());
        if (node == null) {
            node = new Node();
            objects.put(object.getClass().getName(), node);
            attachChild(node);
        }
        if (!node.hasChild(object.getGeometry())) {
            node.attachChild(object.getGeometry());
            guiFor(object);
            return true;
        }
        return false;
    }

    protected void guiFor(Entity object) {

    }

    @Deprecated
    public abstract boolean canGet(T cell, Class<? extends Entity> contentClass);

    @Deprecated
    public abstract boolean canSet(T cell, Class<? extends Entity> contentClass);

    @Deprecated
    public abstract Entity getContent(T cell, Class<? extends Entity> contentClass);

    @Deprecated
    public Entity getContent(final int x, final int y, Class<? extends Entity> contentClass) {
        return getContent(getCell(x, y), contentClass);
    }

    public abstract boolean setContent(T cell, Entity content);

    public boolean setContent(final int x, final int y, Entity content) {
        return setContent(getCell(x, y), content);
    }

    public boolean removeObject(final Entity object) {
        Node node = objects.get(object.getClass().getName());
        if (node == null) {
            return false;
        }
        if (node.hasChild(object.getGeometry())) {
            node.detachChild(object.getGeometry());
            return true;
        }
        return false;
    }

    public boolean removeAll(final Class<? extends Entity> clazz) {
        final Node node = objects.get(clazz.getName());
        if (node != null) {
            node.detachAllChildren();
            return true;
        }
        return false;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    protected boolean gridOn() {
        // FIXME: 09/05/2016 lineWidth don't save
        // FIXME: 20/05/2016 old grid, don't saved
        grid = gridXY(getCols() + 1, getRows() + 1, 1, ColorRGBA.Gray, 3f);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
        return true;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        // TODO: 20/05/2016 detach grid
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(rows, "rows", 1);
        capsule.write(cols, "cols", 1);
        capsule.writeSavableArrayList(cells, "cells", new ArrayList());
        capsule.writeStringSavableMap(objects, "objects", new HashMap<>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        rows = capsule.readInt("rows", 1);
        cols = capsule.readInt("cols", 1);

        final int len = cols * rows;
        cells = new ArrayList<>(len);
        capsule.readSavableArrayList("cells", new ArrayList()).stream()
                .limit(len)
                .forEach(el -> cells.add((T) el));

        objects = (Map<String, Node>) capsule.readStringSavableMap("objects", new HashMap<>());
    }
}