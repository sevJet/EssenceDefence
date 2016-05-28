package io.github.sevjet.essencedefence.listener;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;

import java.util.ArrayList;

import io.github.sevjet.essencedefence.control.WaveControl;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.Cell;
import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.util.Getter;

public class MonsterListener implements ActionListener {

    private int level = 1;

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(ListenerManager.MAPPING_SPAWN_MONSTER) ||
                name.equals(ListenerManager.MAPPING_SPAWN_WAVE) ||
                name.equals(ListenerManager.MAPPING_SPAWN_ALL)) {
            Monster monster;
            CollisionResults results;
            results = ListenerManager.rayCasting();

            if (results.size() > 0 && !isPressed) {
                Cell cell = ListenerManager.getCell(results);
                Field field = cell.getField();
                //TODO change
                if (field != null) {
                    switch (name) {
                        case ListenerManager.MAPPING_SPAWN_MONSTER:
                            if (!cell.hasContent()) {
                                monster = Monster.getDefaultMonster();
                                monster.move(cell.getX(), cell.getY());
                                field.addObject(monster);
                            }
                            break;
                        case ListenerManager.MAPPING_SPAWN_WAVE:
                            if (cell.hasContent() && cell.getContent() instanceof Portal) {
                                Portal portal = (Portal) cell.getContent();
                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 15; i++) {
                                    monster = Monster.getDefaultMonster();
                                    monster.upgrade(level++);
                                    monsters.add(monster);
                                }
                                WaveControl wave = new WaveControl(monsters);
                                wave.setDelay(3f);
                                wave.setGap(2f);
                                portal.addWave(wave);
                            }
                            break;
                        case ListenerManager.MAPPING_SPAWN_ALL:
                            for (Spatial sp : field.getObjects(Portal.class).getChildren()) {
                                Portal p = (Portal) Getter.getEntity(sp);

                                ArrayList<Monster> monsters = new ArrayList<>();
                                for (int i = 0; i < 1; i++) {
                                    monster = Monster.getDefaultMonster();
                                    monster.upgrade(level);
                                    monsters.add(monster);
                                }
                                WaveControl wave = new WaveControl(monsters);
                                wave.setDelay(3f);
                                wave.setGap(2f);

                                p.addWave(wave);
                            }
                            level++;
                            break;
                    }
                }
            }
        }
    }
}
