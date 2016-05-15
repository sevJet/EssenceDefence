package io.github.sevjet.essensedefence.gui;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.util.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.github.sevjet.essensedefence.util.Creator.gridXY;

public class Inventory extends Node {
    protected ArrayList<Essence> essencesList;
    // TODO: 15/05/2016 change on another cell
    protected Cell[][] cells;
    protected Map<Class<? extends Entity>, Node> objects = new HashMap<>();
    protected Node grid;
    private int rows;
    private int cols;

    private Inventory() {

    }

    public Inventory(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        essencesList = new ArrayList<Essence>(rows * cols);
        for (int i = 0; i < rows * cols; i++)
            essencesList.add(null);

        cells = new Cell[rows][];
        for (int i = 0; i < rows; i++) {
            cells[i] = new Cell[cols];
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
                addObject(cells[i][j]);
            }
        }

        gridOn();
        Configuration.getRootNode().attachChild(this);
        this.setLocalTranslation(65f, 12f, 1f);
        this.rotate(
                FastMath.DEG_TO_RAD,
                180 * FastMath.DEG_TO_RAD,
                90 * FastMath.DEG_TO_RAD);
        this.scale(5);
    }

    public Node getObjects(Class<? extends Entity> clazz) {
        return objects.get(clazz);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    protected boolean gridOn() {
        // FIXME: 09/05/2016 lineWidth don't saved
        grid = gridXY(getRows() + 1, getCols() + 1, 1, ColorRGBA.Gray, 5f);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
        return true;
    }

    public boolean addEssence(Essence essence) {
        return addEssence(essence, firstEmpty());
    }

    public boolean addEssence(Essence essence, Cell cell) {
        return addEssence(essence, cell.getX(), cell.getY());
    }

    public boolean addEssence(Essence essence, int x, int y) {
        return addEssence(essence, num(x, y));
    }

    public boolean addEssence(Essence essence, int num) {
        if (isOccupied(num))
            return false;

        essencesList.set(num, essence);
        addObject(essence);
        placeEssence(num);
        return true;

    }

    public Essence getEssence(Cell cell) {
        return getEssence(cell.getX(), cell.getY());
    }

    public Essence getEssence(int x, int y) {
        return getEssence(num(x, y));
    }

    public Essence getEssence(int num) {
        if (isEmpty(num))
            return null;

        Essence essence = essencesList.get(num);
        return essence;
    }

    public Essence getEssence(Essence essence) {
        if (essencesList.contains(essence)) {
            return essence;
        }
        return null;
    }

    protected void placeEssence(int num) {
        Essence essence = essencesList.get(num);
        attachChild(essence.getGeometry());
        essence.getGeometry().setLocalTranslation(getCell(num).getCenter());
    }

    protected Cell getCell(int num) {
        int x, y;
        x = num / cols;
        y = num % cols;
        return cells[x][y];
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
            System.out.println("   -_="+essencesList.indexOf(object));
            essencesList.set(essencesList.indexOf(object), null);
            return true;
        }
        return false;
    }

//    public boolean filled() {
//        return essencesList.size() == rows * cols;
//    }

    public boolean isEmpty(int num) {
        return essencesList.get(num) == null;
    }

    public boolean isOccupied(Cell cell) {
        return isOccupied(cell.getX(), cell.getY());
    }

    public boolean isOccupied(int x, int y) {
        return isOccupied(num(x, y));
    }

    public boolean isOccupied(int num) {
        return num >= rows * cols || essencesList.get(num) != null;
    }

    private int num(int x, int y) {
        return x * cols + y;
    }

    private int firstEmpty() {
        for (int i = 0; i < rows * cols; i++) {
            if (essencesList.get(i) == null)
                return i;
        }
//        for (Essence essence : essencesList) {
//            if (essence == null) {
//                return essencesList.indexOf(essence);
//            }
//        }
        return -1;
    }
}
