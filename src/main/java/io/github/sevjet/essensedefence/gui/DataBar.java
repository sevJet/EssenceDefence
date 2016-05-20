package io.github.sevjet.essensedefence.gui;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import io.github.sevjet.essensedefence.util.Creator;

public class DataBar extends Node {
    private float width;
    private float height;
    private Geometry bar;
    private Geometry backGround;
    private float ratio = 1f; // [0, 1]

    private DataBar() {

    }

    public DataBar(float width, float height) {
        this.width = width;
        this.height = height;
        float bound = 0.3f * height;

        bar = Creator.myQuad(width, height, ColorRGBA.Green);
        backGround = Creator.myQuad(width + bound, height + bound, ColorRGBA.Black);

        attachChild(bar);
        attachChild(backGround);

        moveToCenter();
        moveBack(backGround);
    }

    public void setBackGroundColor(ColorRGBA color) {
        backGround.getMaterial().setColor("Color", color);
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;

        update();
    }

    private void update() {
        bar.setLocalScale(ratio, 1f, 1f);

        bar.getMaterial().setColor("Color", new ColorRGBA(
                ratio >= 0.5f ? 2 - 2 * ratio : 1f,
                ratio >= 0.5f ? 1f : 2 * ratio,
                0f,
                1f
        ));
    }

    private void moveToCenter() {
        bar.setLocalTranslation(-width / 2f, -height / 2f, 0);
        Quad q = (Quad) backGround.getMesh();
        backGround.setLocalTranslation(-q.getWidth() / 2f, -q.getHeight() / 2f, 0f);
    }


    private void moveBack(Geometry backGround) {
        backGround.move(0f, 0f, -0.01f);
    }
}
