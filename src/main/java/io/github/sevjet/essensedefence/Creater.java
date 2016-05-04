package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;

public class Creater extends SupportAbstractClass {

    public Creater(Application app, AppStateManager appState, AppSettings settings) {
        super(app, appState, settings);
    }

    public Node gridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr) {
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(rowLen, colLen, lineDist);
        geom = new Geometry("gridXY", grid);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f * (float) Math.PI / 180f, 0, 0);
        axis.attachChild(geom);

        return axis;
    }


    public Node gridXY(int length, ColorRGBA clr) {
        return gridXY(length, length, 1, clr);
    }

    public Node gridXY(int length) {
        return gridXY(length, ColorRGBA.Gray);
    }

    public Node coorAxises(float length) {
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

    public Geometry myLine(Vector3f start, Vector3f end) {
        Line line = new Line(start, end);
        Geometry geom = new Geometry("line", line);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        geom.setMaterial(mat);
        return geom;
    }

    public Geometry myLine(Vector3f start, Vector3f end, ColorRGBA clr) {
        Geometry line;
        line = myLine(start, end);
        line.getMaterial().setColor("Color", clr);
        return line;
    }

    public Geometry myBox(String name, Vector3f loc, ColorRGBA color) {
        Box mesh = new Box(1, 1, 1);
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }

    public void attachCenterMark() {
        Geometry c = myBox("center mark",
                Vector3f.ZERO, ColorRGBA.White);
        c.scale(10);
        c.setLocalTranslation(settings.getWidth() / 2,
                settings.getHeight() / 2, 0);
        guiNode.attachChild(c);
        ;
    }
}
