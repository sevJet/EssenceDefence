package io.github.sevjet.essensedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.control.WaveControl;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Cell;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.Getter;

import java.util.ArrayList;

import static io.github.sevjet.essensedefence.listener.ListenerManager.*;

public class MonsterListener implements ActionListener {
    @Override

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(MAPPING_SPAWN_MONSTER) ||
                name.equals(MAPPING_SPAWN_WAVE) ||
                name.equals(MAPPING_SPAWN_ALL)) {
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
                            if (!cell.hasContent()) {
                                monster = Monster.getDefaultMonster();
                                monster.setX(cell.getX());
                                monster.setY(cell.getY());
                                field.addObject(monster);
                            }
                            break;
                        case MAPPING_SPAWN_WAVE:
                            if (cell.hasContent() && cell.getContent() instanceof Portal) {
                                Portal portal = (Portal) cell.getContent();
                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 10; i++) {
                                    monster = Monster.getDefaultMonster();
                                    monsters.add(monster);
                                }
                                WaveControl wave = new WaveControl(monsters);
                                wave.setDelay(3f);
                                wave.setGap(2f);
                                portal.addWave(wave);
                                portal.pushWave();
                            }
                            break;
                        case MAPPING_SPAWN_ALL:
                            for (Spatial sp : field.getObjects(Portal.class).getChildren()) {
                                Portal p = (Portal) Getter.getEntity(sp);

                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 15; i++) {
                                    monster = Monster.getDefaultMonster();
                                    monsters.add(monster);
                                }
                                WaveControl wave = new WaveControl(monsters);
                                wave.setDelay(3f);
                                wave.setGap(2f);

                                p.addWave(wave);
                                p.pushWave();
                            }
                            break;
                    }
                }
            }
        }
    }
}
