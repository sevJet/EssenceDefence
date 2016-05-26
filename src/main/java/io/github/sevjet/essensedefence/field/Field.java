package io.github.sevjet.essensedefence.field;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.util.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.github.sevjet.essensedefence.util.Creator.gridXY;

public abstract class Field<T extends Cell> extends Node {

    protected ArrayList<T> cells;
    protected Map<Class<? extends Entity>, Node> objects;
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

    protected abstract T newCell(final int x, final int y);

    public T getCell(final int x, final int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows) {
            return null;
        }
        final int index = y * cols + x;
        return cells.get(index);
    }

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
        return objects.get(clazz);
    }

    public boolean addObject(final Entity object) {
        Node node = objects.get(object.getClass());
        if (node == null) {
            node = new Node();
            objects.put(object.getClass(), node);
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
//        if (object instanceof Fortress) {
//            this.addControl(new GuiControl(object, "XP:", 3f, 1f / 5f));
//        }
//        if (object instanceof Monster) {
//            System.out.printf("xp control");
//            this.addControl(new GuiControl(object, "xp:", 1f, 1f / 8f));
//        }
    }

    public abstract boolean canGet(T cell, Class<? extends Entity> contentClass);

    public abstract boolean canSet(T cell, Class<? extends Entity> contentClass);

    public abstract Entity getContent(T cell, Class<? extends Entity> contentClass);

    public Entity getContent(final int x, final int y, Class<? extends Entity> contentClass) {
        return getContent(getCell(x, y), contentClass);
    }

    public abstract boolean setContent(T cell, Entity content);

    public boolean setContent(final int x, final int y, Entity content) {
        return setContent(getCell(x, y), content);
    }

    public boolean removeObject(final Entity object) {
        Node node = objects.get(object.getClass());
        if (node == null) {
            return false;
        }
        if (node.hasChild(object.getGeometry())) {
            node.detachChild(object.getGeometry());
            return true;
        }
        return false;
    }

    public void removeAll() {
        objects.keySet().forEach(this::removeAll);
    }

    public boolean removeAll(final Class<? extends Entity> clazz) {
        final Node node = objects.get(clazz);
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

    public Node getGrid() {
        return grid;
    }

    public static boolean save(Field field, File file) {
        if(field != null) {
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
            Field field = (Field) Configuration.getAssetManager().loadModel(file.getAbsolutePath());
            Configuration.getAssetManager().unregisterLocator(file.getParent(), FileLocator.class);
            return field;
        }
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        // TODO: 20/05/2016 detach grid
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(rows, "rows", 1);
        capsule.write(cols, "cols", 1);
        capsule.writeSavableArrayList(cells, "cells", null);
        // TODO: 17.05.16 this should be saved too
//        capsule.writeSavableMap(objects, "objects", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        rows = capsule.readInt("rows", 1);
        cols = capsule.readInt("cols", 1);

        // @TODO remake
        ArrayList data = capsule.readSavableArrayList("cells", null);
        final int len = cols * rows;
        cells = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Object el = data.get(i);
            // TODO: 17.05.16  fix this unchecked cast
//            if (el instanceof T) {
            cells.add((T) el);
//            }
        }
    }
}