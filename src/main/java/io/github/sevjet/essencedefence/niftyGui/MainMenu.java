package io.github.sevjet.essencedefence.niftyGui;

import io.github.sevjet.essencedefence.Game;
import io.github.sevjet.essencedefence.GamePlayAppState;

public class MainMenu extends BasicScreen {

    public MainMenu(String xml) {
        super(xml, "mainMenu");
    }

    @SuppressWarnings("unused")
    public void newGame() {
        Game.instance()
                .resetRound();
        Game.instance()
                .switchState(new GamePlayAppState());

        hide();
    }

    @SuppressWarnings("unused")
    public void continueGame() {
        Game.instance()
                .resumeState();

        hide();
    }

    @SuppressWarnings("unused")
    public void exit() {
        Game.instance()
                .stop();
    }

    @Override
    public void onStartScreen() {
        if (!Game.instance().isStateSet()) {
            nifty.getCurrentScreen()
                    .findElementByName("btnContinue")
                    .disable();
        } else {
            nifty.getCurrentScreen()
                    .findElementByName("btnContinue")
                    .enable();
        }
    }

}
