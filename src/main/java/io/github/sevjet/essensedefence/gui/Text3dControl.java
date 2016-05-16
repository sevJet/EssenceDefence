package io.github.sevjet.essensedefence.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.Getter;

public class Text3dControl extends BillboardControl {

    private BitmapText text;
    private ITextual object;
    private Entity entity;
    private String textName;

    private Geometry geom = Creator.myBox(3f / 5, 1f / 5, 0, ColorRGBA.Green);
    private Geometry backGround = Creator.myBox(3f / 5, 1f / 5, 0, ColorRGBA.LightGray);
    private Node tempNode = new Node();

    private float max;

    public Text3dControl(Entity entity, String textName) {
        super();

//        geom.addControl(new BillboardControl());
//        backGround.addControl(new BillboardControl());
        tempNode.addControl(new BillboardControl());

        this.textName = textName;
        text = Creator.text3D(textName, ColorRGBA.LightGray);
//        text.setAlpha(1f);
//        text.mate.
//        getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.FrontAndBack);;
        this.entity = entity;

        this.entity.getField().attachChild(text);
        this.entity.getField().attachChild(tempNode);
        tempNode.attachChild(geom);
        tempNode.attachChild(backGround);
        backGround.setLocalTranslation(0f, 0f, -0.01f);

//        this.entity.getField().attachChild(geom);
//        this.entity.getField().attachChild(backGround);

        text.addControl(this);
        if (entity instanceof ITextual) {
            this.object = (ITextual) entity;
        } else {
            throw new ClassCastException(Getter.getEntity(getSpatial()).getClass().toString() +
                    " must implement ITextual interface!!!");
        }

        max = Float.parseFloat(object.outputValue());
    }

    @Override
    protected void controlUpdate(float tpf) {
        super.controlUpdate(tpf);

        float now = Float.parseFloat(object.outputValue());
        float hp = now / max;

        text.setText(textName + " " + object.outputValue());
        text.setLocalTranslation(entity.getPhysicalCenter().add(0, 0, 1));

        tempNode.setLocalTranslation(entity.getPhysicalCenter().add(0, 0, 1));

        geom.setLocalScale(hp, 1f, 1f);
        geom.setLocalTranslation(-(1 - hp) * ((Box) geom.getMesh()).xExtent, 0f, 0f);
        geom.getMaterial().setColor("Color", new ColorRGBA(
                hp >= 0.5f ? 2 - 2 * hp : max,
                hp >= 0.5f ? max : 2 * hp,
                0f,
                1f
        ));
        if (object.isEnded()) {
            tempNode.removeFromParent();
            text.removeFromParent();
        }
    }
}
