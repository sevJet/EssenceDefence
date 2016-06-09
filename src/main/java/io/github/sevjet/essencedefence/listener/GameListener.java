package io.github.sevjet.essencedefence.listener;

import com.jme3.input.controls.ActionListener;

import io.github.sevjet.essencedefence.Game;
import io.github.sevjet.essencedefence.GamePlayAppState;
import io.github.sevjet.essencedefence.niftyGui.MainMenu;
import io.github.sevjet.essencedefence.util.Configuration;

import static io.github.sevjet.essencedefence.listener.EssenceListener.info;

public class GameListener implements ActionListener {

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_EXIT)) {
            if (!isPressed) {
                Game.instance().pauseState();
                Game.instance().resumeScreen();

                // FIXME: 09.06.16 This is hell
                info = null;
            }
        }
    }

}
