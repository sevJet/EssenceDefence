package io.github.sevjet.essensedefence.niftyGui;

import de.lessvoid.nifty.elements.Element;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.Main.state;

public class StartScreen extends BaseScreen {

    public StartScreen(String xml) {
        super(xml);
    }

    public void start() {
        if (state != null) {
//            state.cleanup();
            Configuration.getAppState().detach(state);
//            state.stateDetached(Configuration.getAppState());
//            state.cleanup();
        }
        state = new GamePlayAppState();

        Configuration.getAppState().attach(state);
        state.stateAttached(Configuration.getAppState());

        this.getNifty().getCurrentScreen().findElementByName("mainLayer").hide();
//        nifty.getCurrentScreen().findElementByName("mainLayer").markForRemoval();
    }

    public void continueGame() {
        if (state == null)
            state = new GamePlayAppState();
        Configuration.getAppState().attach(state);
        state.stateAttached(Configuration.getAppState());

        this.getNifty().getCurrentScreen().findElementByName("mainLayer").hide();
//        nifty.getCurrentScreen().findElementByName("mainLayer").markForRemoval();
    }

    public void exit() {
        Configuration.getApp().stop();
    }

    @Override
    public void onStartScreen() {
        if (state == null) {
            Element el = getNifty().getCurrentScreen().findElementByName("btnContinue");
            el.disable();
        }
    }
}
