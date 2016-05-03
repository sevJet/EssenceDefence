package io.github.sevjet.essensedefence;

import com.jme3.scene.Node;

import java.util.Arrays;

public class field {
    protected cell[][] allCells;
    protected Node cellNode = new Node();
//    protected list<monster> spawned;

    public field(int colNum, int rowNum){
        allCells = new cell[rowNum][];
        for (int i = 0; i < rowNum; i++) {
            allCells[i] = new cell[colNum];
            for (int j = 0; j < colNum; j++) {
                allCells[i][j] = new cell(i, j, Main.mapDefaultGeometry.get(cell.class).clone());
                cellNode.attachChild(allCells[i][j].getGeometry());
            }
        }
    }

    public Node getCellNode() {
        return cellNode;
    }
}
