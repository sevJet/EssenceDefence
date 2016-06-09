package io.github.sevjet.essencedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import io.github.sevjet.essencedefence.control.GameControl;
import io.github.sevjet.essencedefence.game.Round;
import io.github.sevjet.essencedefence.util.Configuration;
import io.github.sevjet.essencedefence.util.Creator;

public class GamePlayAppState extends AbstractAppState {

    protected Node localGui = new Node("localGui");
    protected Node localRoot = new Node("localRoot");

    public GamePlayAppState() {

    }

    public Node getRoot() {
        return localRoot;
    }

    public Node getGui() {
        return localGui;
    }

    public void initRound() {
        final Round round = Game.instance().getRound();

        getGui().attachChild(round.player().getGui());

        final int cols = round.filed().getCols();

        round.filed().setLocalTranslation(0f, 0f, 0f);
        round.filed().addControl(new GameControl(10f));
        getRoot().attachChild(round.filed());

        round.shop().setLocalTranslation(cols + 2, 20f + 0.5f, 0f);
        round.shop().setLocalScale(2f);
        getRoot().attachChild(round.shop());

        round.inventory().setLocalTranslation(cols + 2, 0.5f, 0f);
        round.inventory().setLocalScale(2f);
        getRoot().attachChild(round.inventory());
    }

    protected void initStartData() {
        Configuration.getGuiNode().attachChild(getGui());
        Configuration.getRootNode().attachChild(getRoot());

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
        BitmapText bText = Creator.text2D(text, ColorRGBA.Black);
        bText.setLocalTranslation(10f, Configuration.getSettings().getHeight(), 0);
        bText.scale(0.7f);
        getGui().attachChild(bText);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        initStartData();
        initRound();

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