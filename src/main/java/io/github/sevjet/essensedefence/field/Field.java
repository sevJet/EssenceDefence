package io.github.sevjet.essensedefence.field;

import com.jme3.export.*;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.building.Building;
import io.github.sevjet.essensedefence.util.BoxSize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.github.sevjet.essensedefence.util.Creator.gridXY;

public class Field extends Node {

    protected int rows;
    protected int cols;
    protected Cell[][] cells;
    protected Map<Class<? extends Entity>, Node> objects;
    protected Node grid;

    @SuppressWarnings("unused")
    public Field() {
    }

    public Field(int colNum, int rowNum) {
        rows = rowNum;
        cols = colNum;
        this.setName("field");
        objects = new HashMap<>();
        cells = new Cell[rowNum][];
        for (int i = 0; i < rowNum; i++) {
            cells[i] = new Cell[colNum];
            for (int j = 0; j < colNum; j++) {
                cells[i][j] = new Cell(i, j);
                addObject(cells[i][j]);
            }
        }

        grid = gridXY(colNum + 1, rowNum + 1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
    }

    public Field(Cell[][] cells) {
        this.setName("field");
        int rowNum = cells.length, colNum = cells[0].length;
        objects = new HashMap<>();
        this.cells = cells;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.getBuilding() != null) {
                    build(cell.getBuilding());
                }
                cell.updater();
                addObject(cell);
            }
        }

        Node grid = gridXY(colNum + 1, rowNum + 1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
    }

    public Cell getCell(int x, int y) {
        if (x < cells.length) {
            if (y < cells[x].length) {
                return cells[x][y];
            }
        }
        return null;
    }

    public void build(int x, int y, Building building) {
        for (int i = x; i < x + building.getSize().getWidth(); i++) {
            for (int j = y; j < y + building.getSize().getHeight(); j++) {
                cells[i][j].setBuilding(building);
            }
        }
        building.setX(x);
        building.setY(y);

        addObject(building);
    }

    public void build(Building building) {
        build(building.getX(), building.getY(), building);
    }

    public Node getObjects(Class<? extends Entity> clazz) {
        return objects.get(clazz);
    }

    public Cell getCell(Geometry geom) {
        int x, y;
        if (geom != null && geom.getParent().getParent() == this) {
            x = (int) geom.getLocalTranslation().getX();
            y = (int) geom.getLocalTranslation().getY();
            return getCell(x, y);
        }
        return null;
    }

    public boolean addObject(Entity object) {
        Node node = objects.get(object.getClass());
        if (node == null) {
            node = new Node();
            objects.put(object.getClass(), node);
            attachChild(node);
        }
        if (!node.hasChild(object.getGeometry())) {
            node.attachChild(object.getGeometry());
            return true;
        }
        return false;
    }

    public boolean removeObject(Entity object) {
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

    public boolean enoughPlaceFor(Cell cell, Building building) {
        for (int i = cell.getX(); i < cell.getX() + building.getSize().getWidth(); i++) {
            for (int j = cell.getY(); j < cell.getY() + building.getSize().getHeight(); j++) {
                if (i >= cells.length ||
                        j >= cells[i].length ||
                        cells[i][j].getBuilding() != null)
                    return false;
            }
        }
        return true;
    }

    protected void removeBuilding(Building building) {
        for (int i = building.getX(); i < building.getX() + building.getSize().getWidth(); i++) {
            for (int j = building.getY(); j < building.getY() + building.getSize().getHeight(); j++) {
                cells[i][j].free();
            }
        }
        removeObject(building);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        detachChild(grid);
        super.write(ex);
        attachChild(grid);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(rows, "rows", 1);
        capsule.write(cols, "cols", 1);
        capsule.write(cells, "cells", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        rows = capsule.readInt("rows", 1);
        cols = capsule.readInt("cols", 1);

        // @TODO remake
        Savable[][] data = capsule.readSavableArray2D("cells", null);
        cells = new Cell[rows][];
        for (int i = 0; i < rows; i++) {
            cells[i] = new Cell[cols];
            for (int j = 0; j < cols; j++) {
                cells[i][j] = (Cell) data[i][j];
            }
        }

        grid = gridXY(cols + 1, rows + 1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
    }
}
