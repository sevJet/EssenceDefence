package io.github.sevjet.essensedefence.control;

import com.jme3.scene.control.AbstractControl;

public interface ITextual {
    default void newText(String textName, int rowNum) {
        final AbstractControl control = new TextControl(this, textName, rowNum);
    }

    String outputValue();

    boolean isEnded();
}
