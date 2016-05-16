package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import io.github.sevjet.essensedefence.control.WaveControl;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;

import java.util.ArrayList;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class MonsterListener implements ActionListener {
    @Override

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_SPAWN_MONSTER) ||
                name.equals(MAPPING_SPAWN_WAVE)) {
            Monster monster;
            CollisionResults results;
            results = rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell cell = getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case MAPPING_SPAWN_MONSTER:
                            monster = new Monster(cell.getX(), cell.getY(), 30, 10, 10);
                            field.addObject(monster);
                            break;
                        case MAPPING_SPAWN_WAVE:
                            if (cell.isOccupied() && cell.getBuilding() instanceof Portal) {
                                Portal portal = (Portal) cell.getBuilding();
                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    monster = new Monster(10f, 2f, 0f);
                                    monsters.add(monster);
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
