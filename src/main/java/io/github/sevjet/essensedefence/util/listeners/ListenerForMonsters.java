package io.github.sevjet.essensedefence.util.listeners;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.control.WaveControl;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

import java.util.ArrayList;

import static io.github.sevjet.essensedefence.util.listeners.MappingsAndTriggers.*;

public class ListenerForMonsters implements ActionListener {
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_SPAWN_MONSTER) ||
                name.equals(MAPPING_SPAWN_WAVE)) {

            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_SPAWN_MONSTER:
                            field.addObject(
                                    new Monster(cell.getX(), cell.getY(), 3, 10, 10));
                            break;
                        case MAPPING_SPAWN_WAVE:
                            if (cell.getBuilding() != null && cell.getBuilding() instanceof Portal) {
                                Portal portal = (Portal) cell.getBuilding();
                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    monsters.add(new Monster(10f, 2f, 0f));
                                }
                                WaveControl wave = new WaveControl(monsters);
                                wave.setDelay(3f);
                                wave.setGap(2f);
                                portal.addWave(wave);
                                portal.pushWave();
                            }
                            break;
                    }
                }
            }
        }
    }
}
