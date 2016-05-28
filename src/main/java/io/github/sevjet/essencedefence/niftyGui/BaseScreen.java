package io.github.sevjet.essencedefence.niftyGui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import io.github.sevjet.essencedefence.util.Configuration;

public abstract class BaseScreen implements ScreenController {
    protected Nifty nifty;

    public BaseScreen(String xml, String screenName) {
        nifty = Configuration.getNifty();
        try {
            nifty.fromXml(xml, screenName, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseScreen(String xml) {
        this(xml, "start");
    }

    public Nifty getNifty() {
        return nifty;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {

    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {
    }
}
