package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class Tester extends SupportAbstractClass {

    public Tester(Application app, AppStateManager appState, AppSettings settings) {
        super(app, appState, settings);
    }

    public void testTrigger() {
        Field field = new Field(50, 50);
        field.setLocalTranslation(10, 10, 1);

        field.build(1, 1, new Wall());
        field.build(2, 1, new Tower());
        field.build(4, 1, new Portal());
        field.build(6, 1, new Fortress());

        rootNode.attachChild(field);
    }

    public void testWall() {
        Wall wall = new Wall();
        Field field = new Field(5, 5);
//                wall.getGeometry().rotate((float)Math.PI*(-30)/180f, 0, 0);
        field.setLocalTranslation(10, 10, 1);
        field.getCell(2, 3).setBuilding(wall);

        wall = new Wall();
//                 wall.getGeometry().rotate((float)Math.PI*(-30)/180f, 0, 0);
        field.getCell(4, 1).setBuilding(wall);

        rootNode.attachChild(field);
    }

    public void testFieldClass() {
        int rowNum = 5, colNum = 25;
        Node badFace = new Node();

        Field map = new Field(rowNum, colNum);
        badFace.attachChild(map);
        map.setLocalTranslation(4, 0, 1);

        map = new Field(8, 3);
        badFace.attachChild(map);
        map.setLocalTranslation(4, 15, 1);

        map = new Field(8, 3);
        badFace.attachChild(map);
        map.setLocalTranslation(25, 15, 1);

        map = new Field(8, 2);
        badFace.attachChild(map);
        map.setLocalTranslation(8, 21, 1);
        map.rotate(0, 0, (float) Math.PI * 45 / 180f);

        map = new Field(8, 2);
        badFace.attachChild(map);
        map.setLocalTranslation(22, 22, 1);
        map.rotate(0, 0, -(float) Math.PI * 45 / 180f);

        rootNode.attachChild(badFace);
//        badFace.scale(20);
//        badFace.setLocalTranslation(300, 200, 0);
    }
}
