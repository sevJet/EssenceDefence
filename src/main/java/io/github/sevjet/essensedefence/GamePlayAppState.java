package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.Inventory;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

public class GamePlayAppState extends AbstractAppState {

    //TODO fix it
    public static Field field;
    protected Node localGui = new Node("localGui");
    protected Node localRoot = new Node("localRoot");

    public GamePlayAppState() {
    }

    public Node getLocalRoot() {
        return localRoot;
    }

    public Node getLocalGui() {
        return localGui;
    }

    protected void initStartData() {
        Gamer gamer = Configuration.getGamer();
        gamer.setGold(100);
        gamer.setGui();
        localGui.attachChild(gamer.getGui());

    }

    private void placeGameFields() {
        field = new MapField(25, 25);
        localRoot.attachChild(field);

        field.setLocalTranslation(5, 10, 1);
        field.rotate(
                -90 * FastMath.DEG_TO_RAD,
                0 * FastMath.DEG_TO_RAD,
                0 * FastMath.DEG_TO_RAD
        );

        Configuration.getGamer().setInventory(new Inventory(3, 10));
        Node invent = Configuration.getGamer().getInventory();

        invent.setLocalTranslation(40f, 10f, 1f);
        invent.scale(2.5f);
        invent.rotate(
                -90 * FastMath.DEG_TO_RAD,
                0 * FastMath.DEG_TO_RAD,
                0 * FastMath.DEG_TO_RAD
        );
        localRoot.attachChild(invent);

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initStartData();
//        testText();

        Configuration.getGuiNode().attachChild(localGui);
        Configuration.getRootNode().attachChild(localRoot);


        placeGameFields();
//        initNewCam(0.7f, 1f, 0.7f, 1f, ColorRGBA.Black, Configuration.getRootNode());
//        initNewCam(0.7f, 1f, 0.4f, 0.69f, ColorRGBA.LightGray, field.getObjects(MapCell.class), field.getGrid());
//        initNewCam(0f, 0.3f, 0.7f, 1f, ColorRGBA.Gray, Configuration.getGuiNode());
        setEnabled(false);
    }

    private void initNewCam(float left, float right, float bottom, float top, ColorRGBA color, Node... nodes) {
        Camera cam2 = Configuration.getCam().clone();
        cam2.setViewPort(left, right, bottom, top);
        ViewPort v = Configuration.getApp().getRenderManager().createMainView("Overhead view for cell", cam2);
        v.setEnabled(true);
        for (Node n : nodes) {
            v.attachScene(n);
        }
        v.setBackgroundColor(color);
        v.setClearColor(true);
        v.setClearDepth(true);
        cam2.setLocation(new Vector3f(25f, 45f, -10f));
        cam2.setRotation(new Quaternion(0f, 0.71243817f, -0.7017349f, 0f));
    }


    @Override
    public void cleanup() {
        super.cleanup();

        Main.detachAllControl(localGui);
        Main.detachAllControl(localRoot);
        localGui.removeFromParent();
        localGui.detachAllChildren();
        localRoot.removeFromParent();
        localRoot.detachAllChildren();

        Main.detachAllControl(localRoot);
        Main.detachAllControl(localGui);
//        field.removeAll();
//        field.detachAllChildren();
//        Configuration.getGamer().getInventory().removeFromParent();
//        Configuration.getGamer().getInventory().removeAll();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        super.stateDetached(stateManager);

        localGui.removeFromParent();
        localRoot.removeFromParent();
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        super.stateAttached(stateManager);

        Configuration.getRootNode().attachChild(localRoot);
        Configuration.getGuiNode().attachChild(localGui);
    }
}