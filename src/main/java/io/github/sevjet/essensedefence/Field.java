package io.github.sevjet.essensedefence;

import com.jme3.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class Field extends Node {

    protected Cell[][] cells;
    protected Map<Class<? extends JME3Object>, Node> objects;

    public Field(int colNum, int rowNum){
        objects = new HashMap<>();
        cells = new Cell[rowNum][];

        for(int i = 0; i < rowNum; i++) {
            cells[i] = new Cell[colNum];
            for(int j = 0; j < colNum; j++) {
                cells[i][j] = new Cell(i, j);
                attachChild(cells[i][j].getGeometry());
            }
        }
    }

    public Cell getCell(int x, int y) {
        if(x < cells.length) {
            if(y < cells[x].length) {
                return cells[x][y];
            }
        }
        return null;
    }

    public boolean addObject(JME3Object object) {
        Node node = objects.get(object.getClass());
        if(node == null) {
            node = new Node();
            objects.put(object.getClass(), node);
            attachChild(node);
        }
        if(!node.hasChild(object.getGeometry())) {
            node.attachChild(object.getGeometry());
            return true;
        }
        return false;
    }

    public boolean removeObject(JME3Object object) {
        Node node = objects.get(object.getClass());
        if(node == null) {
            return false;
        }
        if(node.hasChild(object.getGeometry())) {
            node.detachChild(object.getGeometry());
            return true;
        }
        return false;
    }

}
