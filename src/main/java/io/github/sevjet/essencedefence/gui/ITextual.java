package io.github.sevjet.essencedefence.gui;


import com.jme3.export.Savable;

public interface ITextual extends Savable {
    String outputValue();

    boolean isEnded();

}