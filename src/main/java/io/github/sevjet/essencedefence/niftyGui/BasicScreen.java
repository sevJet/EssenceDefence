package io.github.sevjet.essencedefence.niftyGui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import io.github.sevjet.essencedefence.util.Configuration;

public abstract class BasicScreen implements ScreenController {

    protected Nifty nifty;

    protected final String xml;
    protected final String screenName;

    public BasicScreen(String xml, String screenName) {
        this.xml = xml;
        this.screenName = screenName;

        nifty = Configuration.getNifty();
        try {
            nifty.registerScreenController(this);
            nifty.fromXmlWithoutStartScreen(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicScreen(String xml) {
        this(xml, "default");
    }

    public String getScreenName() {
        return screenName;
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void show() {
        nifty.gotoScreen(screenName);
    }

    public void hide() {
        nifty.exit();
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
