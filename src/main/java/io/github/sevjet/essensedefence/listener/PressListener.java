package io.github.sevjet.essensedefence.listener;

import com.jme3.input.controls.ActionListener;

public interface PressListener extends ActionListener {
    void onPress(String name, float tpf);

    @Override
    default void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed)
            onPress(name, tpf);
    }
}

interface Press2 extends PressListener{
    @Override
    void onPress(String name, float tpf);
}
