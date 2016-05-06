package io.github.sevjet.essensedefence;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.sevjet.essensedefence.Creator.gridXY;

public class Field extends Node implements Serializable {

    protected Cell[][] cells;
    protected Map<Class<? extends JME3Object>, Node> objects;
    private List<JME3Object> allObjects = new ArrayList<>();

    public Field(int colNum, int rowNum) {

        objects = new HashMap<>();
        cells = new Cell[rowNum][];
        for (int i = 0; i < rowNum; i++) {
            cells[i] = new Cell[colNum];
            for (int j = 0; j < colNum; j++) {
                cells[i][j] = new Cell(i, j);
                attachChild(cells[i][j].getGeometry());
            }
        }

        Node grid = gridXY(colNum + 1, rowNum + 1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
    }

    public Field(Cell[][] cells) {
        int rowNum = cells.length, colNum = cells[0].length;
        objects = new HashMap<>();
        this.cells = cells;
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (cells[i][j].getBuilding() != null) {
                    build(cells[i][j].getBuilding());
                }
                cells[i][j].updater();
                attachChild(cells[i][j].getGeometry());
            }
        }

        Node grid = gridXY(colNum + 1, rowNum + 1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
    }

    //TODO serialize all objects
    public static void serialize(Field field) {
        try {
            FileOutputStream fos = new FileOutputStream("temp_kill_me.out");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(field);
            oos.writeObject(field.cells);
            oos.flush();
            oos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO deserialize all objects
    public static Field deserialize() {
        try {
            FileInputStream fis = new FileInputStream("temp_kill_me.out");
            ObjectInputStream oin = new ObjectInputStream(fis);
            Field field = new Field((Cell[][]) oin.readObject());
            return field;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
        for (int i = x; i < x + building.getWidth(); i++) {
            for (int j = y; j < y + building.getHeight(); j++) {
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

    public Cell getCell(Geometry geom) {
        int x, y;
        if (geom != null && geom.getParent() == this) {
            x = (int) geom.getLocalTranslation().getX();
            y = (int) geom.getLocalTranslation().getY();
            return getCell(x, y);
        }
        return null;
    }

    public boolean addObject(JME3Object object) {
        allObjects.add(object);
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

    public boolean removeObject(JME3Object object) {
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

}
