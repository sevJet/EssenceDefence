package io.github.sevjet.essensedefence.niftyGui;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;

import static io.github.sevjet.essensedefence.Main.nifty;

public class InfoScreen extends BaseScreen {

    public InfoScreen(String xml) {
        super(xml);

//        nifty.getCurrentScreen().findElementByName("txt").hide();
        nifty.getCurrentScreen().getRootElement().setVisible(false);
    }


    public Element getElement(String name) {
        return nifty.getCurrentScreen().findElementByName(name);
    }

    public boolean setText(Element el, String text) {
        if (el == null || el.getRenderer(TextRenderer.class) == null)
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
        if (el == null)
            return;
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
