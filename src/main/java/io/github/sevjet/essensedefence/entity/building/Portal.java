package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.monster.Wave;
import io.github.sevjet.essensedefence.util.BoxSize;

import java.util.ArrayList;
import java.util.List;

public class Portal extends Building {

    private static final BoxSize SIZE = new BoxSize(1, 2, 3);

    private List<Wave> waves = new ArrayList<>();
    private int pushIndex = 0;

    public Portal() {
        super(SIZE, -1f);
    }

    public Portal(float health) {
        super(SIZE, health);
    }

    public Portal(int x, int y, float health) {
        super(x, y, SIZE, health);
    }

    public void addWave(Wave wave) {
        wave.setPortal(this);
        waves.add(wave);
    }

    public void pushWave() {
        if (waves.size() > pushIndex) {
            waves.get(pushIndex++).start();
        }
    }

}
