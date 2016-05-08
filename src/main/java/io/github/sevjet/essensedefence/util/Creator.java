package io.github.sevjet.essensedefence.util;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;

public final class Creator {
    private Creator() {
    }

    public static Node gridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr) {
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(rowLen, colLen, lineDist);
        geom = new Geometry("gridXY", grid);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f * (float) Math.PI / 180f, 0, 0);
        axis.attachChild(geom);

        return axis;
    }

    public static Node gridXY(int length, ColorRGBA clr) {
        return gridXY(length, length, 1, clr);
    }

    public static Node gridXY(int length) {
        return gridXY(length, ColorRGBA.Gray);
    }

    public static Node coorAxises(float length) {
        Node axis = new Node();
        Geometry line;

        line = myLine(Vector3f.UNIT_X.mult(0),
                Vector3f.UNIT_X.mult(length),
                ColorRGBA.Red);
        axis.attachChild(line);

        line = myLine(Vector3f.UNIT_Y.mult(0),
                Vector3f.UNIT_Y.mult(length),
                ColorRGBA.Green);
        axis.attachChild(line);

        line = myLine(Vector3f.UNIT_Z.mult(0),
                Vector3f.UNIT_Z.mult(length),
                ColorRGBA.Blue);
        axis.attachChild(line);

        return axis;
    }

    public static Geometry myLine(Vector3f start, Vector3f end) {
        Line line = new Line(start, end);
        Geometry geom = new Geometry("line", line);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);
        return geom;
    }

    public static Geometry myLine(Vector3f start, Vector3f end, ColorRGBA clr) {
        Geometry line;
        line = myLine(start, end);
        line.getMaterial().setColor("Color", clr);
        return line;
    }

    public static Geometry myBox(float x, float y) {
        return Creator.myBox(x, y, 0, "box", Vector3f.ZERO, ColorRGBA.Black);
    }

    public static Geometry myBox(float x, float y, ColorRGBA color) {
        return Creator.myBox(x, y, 1, "box", Vector3f.ZERO, color);
    }

    public static Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        return myBox(1, 1, 1, name, loc, color);
    }

    public static Geometry myBox(float x, float y, String name, ColorRGBA color) {
        return myBox(x, y, 0, name, Vector3f.ZERO, color);
    }

    public static Geometry myBox(float x, float y, float z, ColorRGBA color) {
        return myBox(x, y, z, "box", Vector3f.ZERO, color);
    }

    public static Geometry myBox(float x, float y, float z) {
        return myBox(x, y, z, "box", Vector3f.ZERO, ColorRGBA.Black);
    }

    public static Geometry myBox(float x, float y, float z, String name, ColorRGBA color) {
        return myBox(x, y, z, name, Vector3f.ZERO, color);
    }

    public static Geometry myBox(float x, float y, String name, Vector3f loc, ColorRGBA color) {
        return myBox(x, y, 1, name, loc, color);
    }

    public static Geometry myBox(float x, float y, float z, String name, Vector3f loc, ColorRGBA color) {
        Box mesh = new Box(x, y, z);
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }

    public static void attachCenterMark() {
        Geometry c = Creator.myBox("center mark",
                Vector3f.ZERO, ColorRGBA.White);
        c.scale(5);
        c.setLocalTranslation(Configuration.getSettings().getWidth() / 2,
                Configuration.getSettings().getHeight() / 2, 0);
        Configuration.getGuiNode().attachChild(c);
    }

    public static Node debubSet() {
        Node debugNode = new Node();
        debugNode.attachChild(Creator.coorAxises(111f));
//        debugNode.attachChild(gridXY(100));
        debugNode.attachChild(myBox("box", Vector3f.ZERO, ColorRGBA.Blue));

        return debugNode;
    }
}