package io.github.sevjet.essensedefence.util;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import io.github.sevjet.essensedefence.Gamer;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.building.Wall;
import io.github.sevjet.essensedefence.field.Field;

import java.io.File;
import java.io.IOException;
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

    public static void testGamer() {
        Configuration.setGamer(new Gamer(100));
        Configuration.getRootNode().addControl(new AbstractControl() {
            float timer = 0;

            @Override
            protected void controlUpdate(float tpf) {
                timer += tpf;
                if (timer > 1) {
                    Configuration.getGamer().decGold(1);
                    timer = 0;
                }

            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {

            }
        });
    }

    public static void testText() {
        String text =
                "1 - Wall\n" +
                        "2 - Tower\n" +
                        "3 - Portal\n" +
                        "4 - Fortress\n" +
                        "\n" +
                        "E - make Cell passable\n" +
                        "R - reset\n" +
                        "F - Monster";
        BitmapText helloText = Creator.text(text);

//        helloText.setAlpha(0.5f);
//        helloText.setColor(ColorRGBA.Gray);
//        helloText.setLocalTranslation(300, Configuration.getSettings().getHeight() - helloText.getLineHeight(), 0);
//        Configuration.getGuiNode().attachChild(helloText);

        helloText.setLocalTranslation(-50, 100, -100);
        Configuration.getRootNode().attachChild(helloText);
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

    public static class TestForSerialization {

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
            field.build(gen(40), gen(40), new Fortress(100f));
            Configuration.getRootNode().attachChild(field);
            return field;
        }

        public static void save(Field field) {
            BinaryExporter exporter = BinaryExporter.getInstance();
            File file = new File("saved_map.j3o");
            try {
                exporter.save(field, file);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }

        public static Field load() {
            Configuration.getAssetManager().registerLocator("./", FileLocator.class);
            return (Field) Configuration.getAssetManager().loadModel("saved_map.j3o");
        }
    }
}