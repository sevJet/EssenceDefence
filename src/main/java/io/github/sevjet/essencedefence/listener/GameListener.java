package io.github.sevjet.essencedefence.listener;

import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essencedefence.GamePlayAppState;
import io.github.sevjet.essencedefence.niftyGui.StartScreen;
import io.github.sevjet.essencedefence.util.Configuration;

import static io.github.sevjet.essencedefence.listener.EssenceListener.info;

public class GameListener implements ActionListener {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_EXIT)) {
            if (!isPressed) {
                Configuration.getAppState().getState(GamePlayAppState.class).stateDetached(
                        Configuration.getAppState()
                );
                info = null;
                StartScreen screen = new StartScreen("interface/mainMenu.xml");
            }
        }
    }
}
