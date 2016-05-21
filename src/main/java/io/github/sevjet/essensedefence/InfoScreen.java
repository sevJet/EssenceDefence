package io.github.sevjet.essensedefence;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import io.github.sevjet.essensedefence.util.Configuration;

public class InfoScreen implements ScreenController {
    protected Nifty nifty;

    public InfoScreen() {
        AssetManager assetManager = Configuration.getAssetManager();
        InputManager inputManager = Configuration.getApp().getInputManager();
        AudioRenderer audioRenderer = Configuration.getApp().getAudioRenderer();
        ViewPort guiViewPort = Configuration.getApp().getGuiViewPort();

        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

        nifty = niftyDisplay.getNifty();

        try {
            nifty.fromXml("interface/testNifty.xml", "start", this);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
//        flyCam.setEnabled(false);
//        flyCam.setDragToRotate(true);
//        inputManager.setCursorVisible(true);
        nifty.getCurrentScreen().getRootElement().setVisible(false);
    }


    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public Nifty getNifty() {
        return nifty;
    }

    public Element getElement(String name) {
        return nifty.getCurrentScreen().findElementByName(name);
    }

    public boolean setText(Element el, String text) {
        if (el.getRenderer(TextRenderer.class) == null)
            return false;

        el.getRenderer(TextRenderer.class).setText(text);
        return true;
    }

    public boolean moveTo(String name, int x, int y) {
        Element el = nifty.getCurrentScreen().findElementByName(name);
        return moveTo(el, x, y);
    }

    public boolean moveTo(Element el, int x, int y) {
        if (el == null)
            return false;
        el.setConstraintX(new SizeValue(Integer.toString(x)));
        el.setConstraintY(new SizeValue(Integer.toString(y)));
        return true;
    }

    public void updateAll() {
        nifty.getCurrentScreen().layoutLayers();
    }

    public void update(Element el) {
        if (el.getParent() == null) {
            el.layoutElements();
            return;
        }
        el.getParent().layoutElements();
    }

    public void showAll() {
        nifty.getCurrentScreen().getRootElement().setVisible(true);
    }

    public void hideAll() {
        nifty.getCurrentScreen().getRootElement().setVisible(false);
    }
}
