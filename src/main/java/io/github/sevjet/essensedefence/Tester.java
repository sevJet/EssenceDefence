package io.github.sevjet.essensedefence;

import com.jme3.scene.Node;

import java.util.Random;

import static io.github.sevjet.essensedefence.GamePlayAppState.field;

public final class Tester {

    private Tester() {
    }

    public static void tests() {
//        testTrigger();
//        testWall();
        testAllBuildings();
    }

    public static void testAllBuildings() {
        Field field = new Field(10, 10);
        field.setLocalTranslation(10, 10, 1);

        field.build(1, 1, new Wall());
        field.build(2, 1, new Tower());
        field.build(4, 1, new Portal());
        field.build(6, 1, new Fortress());
    }

    public static void testTrigger() {
        field = new Field(50, 50);
        field.setLocalTranslation(10, 10, 1);

        Configuration.getRootNode().attachChild(field);
    }

    public static void testWall() {
        Wall wall = new Wall();
        Field field = new Field(5, 5);
        wall.getGeometry().rotate((float) Math.PI * (-30) / 180f, 0, 0);
        field.setLocalTranslation(10, 10, 1);
        field.build(2, 3, wall);

        wall = new Wall();
        wall.getGeometry().rotate((float) Math.PI * (-30) / 180f, 0, 0);
        field.build(4, 1, wall);

        Configuration.getRootNode().attachChild(field);
    }

    public static void testFieldClass() {
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

        Configuration.getRootNode().attachChild(badFace);
//        badFace.scale(20);
//        badFace.setLocalTranslation(300, 200, 0);
    }

    static class TestForSerialization {

        private static int gen(int to) {
            Random rnd = new Random();
            return Math.abs(rnd.nextInt()) % to;
        }

        public static Field testSerialization() {
            field = new Field(50, 50);
            field.setLocalTranslation(5, 10, 1);

            field.build(1, 1, new Wall());
            field.build(gen(40), gen(40), new Wall());
            field.build(gen(40), gen(40), new Tower());
            field.build(gen(40), gen(40), new Portal());
            field.build(gen(40), gen(40), new Fortress());
            Configuration.getRootNode().attachChild(field);
            return field;
        }

        public static void save(Field field) {
            Field.serialize(field);
        }

        public static Field load() {
            Field field = Field.deserialize();
            if (field != null) {
                Configuration.getRootNode().attachChild(field);
                field.setLocalTranslation(-55, 10, 1);
            }
            return field;
        }
    }
}