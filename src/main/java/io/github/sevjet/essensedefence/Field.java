package io.github.sevjet.essensedefence;

import com.jme3.scene.Node;

public class Field extends Node {

    protected Cell[][] allCells;
//    protected list<monster> spawned;

    public Field(int colNum, int rowNum){
        allCells = new Cell[rowNum][];
        for(int i = 0; i < rowNum; i++) {
            allCells[i] = new Cell[colNum];
            for(int j = 0; j < colNum; j++) {
                allCells[i][j] = new Cell(i, j, GeometryManager.getDefault(Cell.class).clone());
                attachChild(allCells[i][j].getGeometry());
            }
        }
    }

}
