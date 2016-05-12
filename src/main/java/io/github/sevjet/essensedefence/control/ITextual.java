package io.github.sevjet.essensedefence.control;

import com.jme3.export.Savable;
import com.jme3.scene.control.AbstractControl;

public interface ITextual extends Savable {
    default void newText(String textName, int rowNum) {
        final AbstractControl control = new TextControl(this, textName, rowNum);
    }

    String outputValue();

    boolean isEnded();
}
