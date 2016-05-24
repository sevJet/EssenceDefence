package io.github.sevjet.essensedefence.listener;

import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.GamePlayAppState;
import io.github.sevjet.essensedefence.niftyGui.StartScreen;
import io.github.sevjet.essensedefence.util.Configuration;

import static io.github.sevjet.essensedefence.listener.EssenceListener.info;
import static io.github.sevjet.essensedefence.listener.ListenerManager.MAPPING_EXIT;

public class GameListener implements ActionListener {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_EXIT)) {
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
