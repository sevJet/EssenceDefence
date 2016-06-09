package io.github.sevjet.essencedefence.niftyGui;

import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;

public class EssenceInfo extends BasicScreen {

    public EssenceInfo(String xml, String screenName) {
        super(xml, screenName);

        nifty.getScreen(screenName)
                .findElementByName("txt")
                .hide();
    }

    public Element getElement(String name) {
        return nifty.getScreen(screenName).findElementByName(name);
    }

    public boolean setText(Element el, String text) {
        if (el == null || el.getRenderer(TextRenderer.class) == null)
            return false;

        el.getRenderer(TextRenderer.class).setText(text);
        return true;
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
        el.startEffect(EffectEventId.onClick);
    }

    public void showAll() {
        nifty.getScreen("info").getRootElement().setVisible(true);
        show();
    }

    public void hideAll() {
        nifty.getCurrentScreen().getRootElement().setVisible(false);
    }
}
