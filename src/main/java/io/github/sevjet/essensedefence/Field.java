package io.github.sevjet.essensedefence;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;

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

        //grid field
        setLocalTranslation(4, 0, 1);

        Node grid = gridXY(colNum+1, rowNum+1, 1, ColorRGBA.White);
        grid.setLocalTranslation(-0.5f, -0.5f, 0);
        attachChild(grid);
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

    public Node gridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr){
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(rowLen, colLen, lineDist);
        geom = new Geometry("gridXY", grid);
        //TODO: FIX static field
        Material mat = new Material(Main.assetManagerStatic,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f*(float)Math.PI/180f, 0, 0);
        axis.attachChild(geom);

        return axis;
    }

}
