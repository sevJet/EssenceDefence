package io.github.sevjet.essensedefence.gui;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;
import io.github.sevjet.essensedefence.util.Getter;


// TODO: 17/05/2016 change & delete!!!!
public class Text3dControl extends BillboardControl {

    private BitmapText text;
    private ITextual object;
    private Entity entity;
    private String textName;

    private Node tempNode = new Node();
    private Node nnode = new Node();
    private DataBar bar;

    private float max;

    public Text3dControl(Entity entity, String textName) {
        super();

        if (entity instanceof ITextual) {
            this.object = (ITextual) entity;
        } else {
            throw new ClassCastException(Getter.getEntity(getSpatial()).getClass().toString() +
                    " must implement ITextual interface!!!");
        }


        bar = new DataBar(4f / 5f, 1f / 10f);
        bar.addControl(new BarControl(entity));
//        tempNode.addControl(new BillboardControl());

        this.textName = textName;
        text = Creator.text3D(textName, ColorRGBA.LightGray);
        text.setSize(0.4f);
        text.setLocalTranslation(0, 0, 2f);

        this.entity = entity;

        tempNode.addControl(this);
        tempNode.attachChild(bar);
        tempNode.attachChild(text);
        this.entity.getField().attachChild(tempNode);


        max = Float.parseFloat(object.outputValue());


//        Configuration.getGuiNode().attachChild(tempNode);
//        tempNode.scale(100);
//        tempNode.setLocalTranslation(300, 500, 0);
//        DataBar bar2 = new DataBar(4, 1);
//        Configuration.getGuiNode().attachChild(bar2);
//        bar2.setLocalTranslation(500,500,0);
//        bar2.scale(100);

//        Configuration.getGuiNode().attachChild(bar);
//        bar.scale(2*text.getFont().getCharSet().getRenderedSize());
        nnode.attachChild(bar);

        text = Creator.text2D(textName, ColorRGBA.LightGray);
        text.setSize(0.3f);
        text.setLocalTranslation(-0.3f, 0.4f, 0);
        text.addControl(new TextControl((ITextual) entity, textName));
        nnode.attachChild(text);

        Configuration.getGuiNode().attachChild(nnode);
        nnode.scale(2 * text.getFont().getCharSet().getRenderedSize());

        nnode.addControl(new MovementOnGuiControl(entity));
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (object.isEnded()) {
            tempNode.removeFromParent();
            nnode.removeFromParent();
            return;
        }

        super.controlUpdate(tpf);


    }
}


