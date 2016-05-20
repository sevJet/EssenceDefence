package io.github.sevjet.essensedefence.gui;


import com.jme3.export.Savable;

public interface ITextual extends Savable {
    String outputValue();

    boolean isEnded();

}