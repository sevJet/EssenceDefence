package io.github.sevjet.essencedefence.niftyGui;

import de.lessvoid.nifty.elements.Element;

import io.github.sevjet.essencedefence.GamePlayAppState;
import io.github.sevjet.essencedefence.Main;
import io.github.sevjet.essencedefence.util.Configuration;

public class StartScreen extends BaseScreen {

    public StartScreen(String xml) {
        super(xml);
    }

    public void start() {
        if (Main.state != null) {
//            state.cleanup();
            Configuration.getAppState().detach(Main.state);
//            state.stateDetached(Configuration.getAppState());
//            state.cleanup();
        }
        Main.state = new GamePlayAppState();

        Configuration.getAppState().attach(Main.state);
        Main.state.stateAttached(Configuration.getAppState());

        this.getNifty().getCurrentScreen().findElementByName("mainLayer").hide();
    }

    public void continueGame() {
        if (Main.state == null)
            Main.state = new GamePlayAppState();
        Configuration.getAppState().attach(Main.state);
        Main.state.stateAttached(Configuration.getAppState());

        this.getNifty().getCurrentScreen().findElementByName("mainLayer").hide();
    }

    public void exit() {
        Configuration.getApp().stop();
    }

    @Override
    public void onStartScreen() {
        if (Main.state == null) {
            Element el = getNifty().getCurrentScreen().findElementByName("btnContinue");
            el.disable();
        }
    }
}
