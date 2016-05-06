package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import java.util.Random;

import static io.github.sevjet.essensedefence.Field.serialize;
import static io.github.sevjet.essensedefence.GamePlayAppState.field;

public class Tester {

    Configuration config;

    public Tester() {
        config = Configuration.getInstance();
    }

    public void tests() {
//        testTrigger();
//        testWall();
        testAllBuildings();
    }

    Random rnd = new Random();

    private int gen(int to) {
        return Math.abs(rnd.nextInt()) % to;
    }

    public Field testSerialization() {

        field = new Field(50, 50);
        field.setLocalTranslation(10, 10, 1);

        field.build(1, 1, new Wall());
        field.build(gen(20), gen(20), new Tower());
        field.build(gen(20), gen(20), new Portal());
        field.build(gen(20), gen(20), new Fortress());
        config.getRootNode().attachChild(field);
        return field;
    }

    public void testAllBuildings() {
        Field field = new Field(10, 10);
        field.setLocalTranslation(10, 10, 1);

        field.build(1, 1, new Wall());
        field.build(2, 1, new Tower());
        field.build(4, 1, new Portal());
        field.build(6, 1, new Fortress());
    }

    public void testTrigger() {
        field = new Field(50, 50);
        field.setLocalTranslation(10, 10, 1);

        config.getRootNode().attachChild(field);
    }

    public void testWall() {
        Wall wall = new Wall();
        Field field = new Field(5, 5);
        wall.getGeometry().rotate((float) Math.PI * (-30) / 180f, 0, 0);
        field.setLocalTranslation(10, 10, 1);
        field.build(2, 3, wall);

        wall = new Wall();
        wall.getGeometry().rotate((float) Math.PI * (-30) / 180f, 0, 0);
        field.build(4, 1, wall);

        config.getRootNode().attachChild(field);
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

        config.getRootNode().attachChild(badFace);
//        badFace.scale(20);
//        badFace.setLocalTranslation(300, 200, 0);
    }
}
