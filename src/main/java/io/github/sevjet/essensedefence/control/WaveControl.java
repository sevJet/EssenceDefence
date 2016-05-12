package io.github.sevjet.essensedefence.control;

import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;

import java.util.ArrayList;
import java.util.List;

public class WaveControl extends BasicControl {

    private List<Monster> monsters;
    private int spawnIndex = -1;
    private float curTime = 0f;
    private float spawnTime = 0f;
    private float delay = 0f;
    private float gap = 0f;
    private Field field = null;
    private Portal portal = null;

    public WaveControl() {
        monsters = new ArrayList<>();
    }

    public WaveControl(List<Monster> monsters) {
        this();
        this.monsters.addAll(monsters);
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (entity instanceof Portal) {
            portal = (Portal) entity;
        } else {
            throw new IllegalArgumentException("Not a Portal spatial");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (spawnIndex >= monsters.size()) {
            return;
        }
        curTime += tpf;
        if (spawnIndex == -1) {
            if (curTime >= delay) {
                spawnIndex++;
            } else {
                return;
            }
        }
        if (spawnIndex > 0) {
            if (curTime < spawnTime + gap) {
                return;
            }
        }
        portal.spawn(monsters.get(spawnIndex++));
        spawnTime = curTime;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void addMonster(List<Monster> monsters) {
        this.monsters.addAll(monsters);
    }

}
