package io.github.sevjet.essensedefence.util;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public final class Creator {

    private Creator() {
    }

    public static Geometry myQuad(float x, float y, String name, ColorRGBA color) {
        Quad mesh = new Quad(x, y);
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
//        geom.setLocalTranslation(loc);
        return geom;
    }

    public static Geometry myQuad(float x, float y, ColorRGBA color) {
        return myQuad(x, y, "guard", color);
    }

    //Fix for create savable grid
    private static Node tempGridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr, float lineWidth) {
        Node axis = new Node();
        Geometry geom;
        for (int i = 0; i < rowLen; i++) {
            geom = myLine(
                    new Vector3f(i * lineDist, 0, 0),
                    new Vector3f(i * lineDist, colLen - 1, 0),
                    clr,
                    lineWidth
            );

            axis.attachChild(geom);
        }
        for (int i = 0; i < colLen; i++) {
            geom = myLine(
                    new Vector3f(0, i * lineDist, 0),
                    new Vector3f(rowLen - 1, i * lineDist, 0),
                    clr,
                    lineWidth
            );
            axis.attachChild(geom);
        }
        return axis;
    }

    public static Node gridXY(int rowLen, int colLen, float lineDist, ColorRGBA clr, float lineWidth) {
        Node axis = new Node();
        Geometry geom;

        Grid grid = new Grid(colLen, rowLen, lineDist);
        grid.setLineWidth(lineWidth);
        geom = new Geometry("gridXY", grid);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        geom.rotate(-90f * (float) Math.PI / 180f, 0, 0);
        axis.attachChild(geom);

        return axis;
//        return tempGridXY(rowLen, colLen, lineDist, clr, lineWidth);
    }

    public static Node gridXY(int length, ColorRGBA clr) {
        return gridXY(length, length, 1, clr, 1f);
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

    public static Geometry myLine(Vector3f start, Vector3f end, ColorRGBA clr, float lineWidth) {
        Line line = new Line(start, end);
        line.setLineWidth(lineWidth);
        Geometry geom = new Geometry("line", line);
        Material mat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", clr);
        geom.setMaterial(mat);
        return geom;
    }

    public static Geometry myLine(Vector3f start, Vector3f end, ColorRGBA clr) {
        return myLine(start, end, clr, 1f);
    }

    public static Geometry myLine(Vector3f start, Vector3f end) {
        return myLine(start, end, ColorRGBA.White, 1f);
    }

    public static Geometry myWireframe(BoxSize size, String name) {
        return myWireframe(size, name, ColorRGBA.Gray);
    }

    public static Geometry myWireframe(BoxSize size, String name, ColorRGBA color) {
        Geometry geometry = myBox(size, name, color);

        geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
        geometry.getMesh().setLineWidth(5f);
        return geometry;
    }

    public static Geometry myBox(BoxSize size, String name) {
        return Creator.myBox(size, name, ColorRGBA.Yellow);
    }

    public static Geometry myBox(BoxSize size, String name, ColorRGBA color) {
        return Creator.myBox(size.getWidth() / 2f, size.getHeight() / 2f, size.getDepth() / 2f, name, color);
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

    public static Geometry myShinySphere(float radius, String name, ColorRGBA color) {
        Sphere sphereMesh = new Sphere(32, 32, radius, true, false);
        Geometry geom = new Geometry(name, sphereMesh);

        sphereMesh.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphereMesh);

        Material sphereMat = new Material(Configuration.getAssetManager(),
                "Common/MatDefs/Light/Lighting.j3md");
        sphereMat.setBoolean("UseMaterialColors", true);
        sphereMat.setColor("Diffuse", color);
        sphereMat.setColor("Specular", ColorRGBA.White);
        sphereMat.setFloat("Shininess", 64f);   // [0,128]

        geom.setMaterial(sphereMat);
//        geom.rotate(1.6f, 0, 0);

        return geom;
    }

    public static Geometry mySphere(float radius, String name, /*Vector3f loc,*/ ColorRGBA color) {
        Vector3f loc = Vector3f.ZERO;
        Sphere mesh = new Sphere(32, 32, radius, true, false);
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

    public static Node debugSet() {
        Node debugNode = new Node();
        debugNode.attachChild(Creator.coorAxises(111f));
//        debugNode.attachChild(gridXY(100));
        debugNode.attachChild(myBox("box", Vector3f.ZERO, ColorRGBA.Blue));

        return debugNode;
    }

    public static BitmapText text(String text) {
        return text(text, ColorRGBA.White);
    }

    public static BitmapText text(String text, int rowNum) {
        BitmapFont guiFont;
//        Configuration.getAssetManager().registerLocator("./", FileLocator.class);
//        guiFont = Configuration.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        guiFont = Configuration.getAssetManager().loadFont("interface/fonts/arial.fnt");
        return text(text, 0, Configuration.getSettings().getHeight() - rowNum * guiFont.getCharSet().getRenderedSize());
    }

    public static BitmapText text(String text, float x, float y) {
        BitmapText helloText = Creator.text(text, x, y, ColorRGBA.White);
        Configuration.getGuiNode().attachChild(helloText);
        return helloText;
    }

    public static BitmapText text(String text, ColorRGBA clr) {
        return text(text, 0, Configuration.getSettings().getHeight(), clr);
    }

    public static BitmapText text(String text, float x, float y, ColorRGBA clr) {
        BitmapText helloText = Creator.text(text, x, y, clr, true);
//        helloText.setAlpha(0.5f);
        Configuration.getGuiNode().attachChild(helloText);
        return helloText;
    }

    public static BitmapText text(String text, float x, float y, ColorRGBA clr, boolean attached) {
        BitmapFont guiFont;
//        Configuration.getAssetManager().registerLocator("./", FileLocator.class);
//        guiFont = Configuration.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        guiFont = Configuration.getAssetManager().loadFont("interface/fonts/arial.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
//        helloText.setName(name);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(text);
//        helloText.setQueueBucket(RenderQueue.Bucket.Transparent);
//        helloText.setQueueBucket(RenderQueue.Bucket.Gui);

        helloText.setColor(clr);
        helloText.setLocalTranslation(x, y, 0);

        if (attached)
            Configuration.getGuiNode().attachChild(helloText);
        return helloText;
    }


    public static BitmapText text3D(String text) {
        return text3D(text, ColorRGBA.White);
    }

    public static BitmapText text3D(String text, ColorRGBA clr) {
        BitmapFont guiFont;
        guiFont = Configuration.getAssetManager().loadFont("interface/fonts/arial.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setText(text);
        helloText.setSize(1.0f);
        helloText.setQueueBucket(RenderQueue.Bucket.Transparent);

        helloText.setColor(clr);

        return helloText;
    }

    public static BitmapText text2D(String text, ColorRGBA clr) {
        BitmapFont guiFont;
        guiFont = Configuration.getAssetManager().loadFont("interface/fonts/arial.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(text);
        helloText.setQueueBucket(RenderQueue.Bucket.Gui);
        helloText.setColor(clr);

        return helloText;
    }
}