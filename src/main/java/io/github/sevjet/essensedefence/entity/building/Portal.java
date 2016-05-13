package io.github.sevjet.essensedefence.entity.building;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import io.github.sevjet.essensedefence.control.WaveControl;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;

import java.io.IOException;
import java.util.ArrayList;

public class Portal extends Building {

    private static final BoxSize SIZE = new BoxSize(1, 2, 3);

    private ArrayList<WaveControl> waves = new ArrayList<>();
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

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeSavableArrayList(waves, "waves", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        ArrayList list = capsule.readSavableArrayList("waves", null);
        if (!list.isEmpty()) {
            waves = new ArrayList<>(list.size());
            for (Object el : list) {
                if (el instanceof WaveControl) {
                    waves.add((WaveControl) el);
                }
            }
        }
    }
}
