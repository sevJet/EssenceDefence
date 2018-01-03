package io.github.sevjet.essensedefence;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import io.github.sevjet.essensedefence.control.GameControl;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.field.MapField;
import io.github.sevjet.essensedefence.util.Configuration;
import io.github.sevjet.essensedefence.util.Creator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GamePlayAppState extends AbstractAppState {

    //TODO fix it
    public static MapField field;
    protected Node localGui = new Node("localGui");
    protected Node localRoot = new Node("localRoot");
    private static final String text =
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

        BitmapText btext = Creator.text2D(text, ColorRGBA.Black);
        btext.setLocalTranslation(10f, Configuration.getSettings().getHeight(), 0);
        btext.scale(0.7f);
        localGui.attachChild(btext);

    }

    private void placeGameFields() {
        int n, m;
        n = 26;
        m = n;
//        field = (MapField) load("level1.j3o");
        field = new MapField(n, m);
        field.setLocalTranslation(0f, 0f, 0f);
        field.addControl(new GameControl(6f));
        localRoot.attachChild(field);
        load("cells.txt");
//        field.updateObjectParent();

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
    public void cleanup() {
        super.cleanup();

//        save("cells.txt");
//        if (field != null)
//            save(field, "level1.j3o");
        stateDetached(Configuration.getAppState());
        Configuration.getAppState().detach(this);
//        field.removeAll();
//        state = null;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        super.stateAttached(stateManager);

        Configuration.getRootNode().attachChild(localRoot);
        Configuration.getGuiNode().attachChild(localGui);
    }

    public void save(String name) {
        File file = new File(name);
        PrintWriter out = null;
        file.delete();
        try {
            out = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.println(field.getRows());
        out.println(field.getCols());
        for (int i = 0; i < field.getRows(); i++) {
            for (int j = 0; j < field.getCols(); j++) {
                out.print(field.getCell(i, j).isPassable() + " ");
            }
            out.println();
        }
        out.close();
    }

    public void load(String name) {
        File file = new File(name);
        Scanner in = null;
        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n, m;
        n = in.nextInt();
        m = in.nextInt();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                field.getCell(i, j).setPassably(in.nextBoolean());
            }
        }
        in.close();

        field.build(12, 12, new Fortress(100));
        field.build(n / 2, n - 1, new Portal());
//        field.build(n - 2, n - 1, new Portal());
    }

//    public static void save(MapField field, String name) {
//        BinaryExporter exporter = BinaryExporter.getInstance();
//        File file = new File(name);
//        try {
//            exporter.save(field, file);
//        } catch (IOException ex) {
//            ex.printStackTrace(System.err);
//        }
//    }
//
//    public static MapField load(String name) {
//        Configuration.getAssetManager().registerLocator("./", FileLocator.class);
//        MapField field = (MapField) Configuration.getAssetManager().loadModel(name);
//        return field;
//    }
}