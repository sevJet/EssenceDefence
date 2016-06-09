package io.github.sevjet.essencedefence;

import com.jme3.system.AppSettings;
import com.jme3.system.Natives;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static com.jme3.system.JmeSystem.getPlatform;

public class Bootstrap {

    public static AppSettings mySettings(boolean isOSX) throws LWJGLException {
        AppSettings settings = new AppSettings(true);

        settings.setTitle("EssenceDefence 1.0");

        try {
            settings.setAudioRenderer("LWJGL");
            Natives.extractNativeLibs(getPlatform(), settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GraphicsDevice device = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isOSX) {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            System.setProperty("apple.awt.UIElement", "true");

            java.awt.DisplayMode mode = device.getDisplayMode();
            settings.setResolution(mode.getWidth(), mode.getHeight());
            settings.setFrequency(mode.getRefreshRate());
            settings.setBitsPerPixel(mode.getBitDepth());
        } else {
            org.lwjgl.opengl.DisplayMode mode = Arrays.stream(Display.getAvailableDisplayModes())
                    .reduce(new org.lwjgl.opengl.DisplayMode(0, 0), (prev, el) -> {
                        long prevSize = (long) prev.getHeight() * prev.getWidth();
                        long elSize = (long) el.getHeight() * el.getWidth();
                        if (prevSize < elSize) {
                            return el;
                        }
                        if (prevSize == elSize &&
                                prev.getBitsPerPixel() <= el.getBitsPerPixel() &&
                                prev.getFrequency() <= el.getFrequency()) {
                            return el;
                        }

                        return prev;
                    });
            settings.setResolution(mode.getWidth(), mode.getHeight());
            settings.setFrequency(mode.getFrequency());
            settings.setBitsPerPixel(mode.getBitsPerPixel());
            settings.setFullscreen(device.isFullScreenSupported());
        }

        settings.setSamples(2);

        return settings;
    }

    public static void main(String[] args) throws LWJGLException {
        final Game app = Game.instance();

        app.setSettings(mySettings(System.getProperty("os.name")
                .toLowerCase()
                .contains("mac")));
        app.setShowSettings(false);
        app.start();
        Display.setResizable(true);
        Display.setFullscreen(true);
    }
}