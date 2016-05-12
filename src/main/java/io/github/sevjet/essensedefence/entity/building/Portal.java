package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.control.WaveControl;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;

import java.util.ArrayList;
import java.util.List;

public class Portal extends Building {

    private static final BoxSize SIZE = new BoxSize(1, 2, 3);

    private List<WaveControl> waves = new ArrayList<>();
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

    public void addWave(WaveControl wave) {
        waves.add(wave);
    }

    public void pushWave() {
        if (waves.size() > pushIndex) {
            geometry.addControl(waves.get(pushIndex++));
        }
    }

    public void spawn(Monster monster) {
        Field field;
        if ((field = getField()) != null) {
            monster.setX(getX());
            monster.setY(getY());
            field.addObject(monster);
        }
    }

}
