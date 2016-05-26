package io.github.sevjet.essensedefence.niftyGui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import static io.github.sevjet.essensedefence.Main.nifty;

public abstract class BaseScreen implements ScreenController {

    public BaseScreen(String xml, String screenName) {
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
