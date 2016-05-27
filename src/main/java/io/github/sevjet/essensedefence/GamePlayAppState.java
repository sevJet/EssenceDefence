package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.control.GameControl;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.field.MapField;
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
        Configuration.getGuiNode().attachChild(localGui);
        Configuration.getRootNode().attachChild(localRoot);

        Gamer gamer = Configuration.getGamer();
        gamer.setGold(100f);
        gamer.setGui();
        localGui.attachChild(gamer.getGui());

        String text =
                "W - Wall\n" +
                        "T - Tower\n" +
                        "P - Portal\n" +
                        "F - Fortress\n" +
                        "\n" +
                        "E - make Cell passable\n" +
                        "R - reset\n" +
                        "M - Monster\n" +
                        "G - Start Wave\n" +
                        "B - Buy Essence\n" +
                        "S - Sell Essence";
        BitmapText btext = Creator.text2D(text, ColorRGBA.Black);
        btext.setLocalTranslation(10f, Configuration.getSettings().getHeight(), 0);
        btext.scale(0.7f);
        localGui.attachChild(btext);

    }

    private void placeGameFields() {
        int n, m;
        n = 26;
        m = n;
        field = new MapField(n, m);
        field.setLocalTranslation(0f, 0f, 0f);
        field.addControl(new GameControl(10f));
        localRoot.attachChild(field);

        Configuration.getGamer().resetShop();
        Node shop = Configuration.getGamer().getShop();

        shop.setLocalTranslation(m + 2, 20f + 0.5f, 0f);
        shop.scale(2f);
        localRoot.attachChild(shop);

        Configuration.getGamer().resetInventory();
        Node invent = Configuration.getGamer().getInventory();
        invent.setLocalTranslation(m + 2, 0.5f, 0f);
        invent.scale(2f);
        localRoot.attachChild(invent);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initStartData();
//        testText();

        placeGameFields();
        setEnabled(false);
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