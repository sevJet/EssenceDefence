package io.github.sevjet.essencedefence.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Spatial;

import java.io.IOException;
import java.util.ArrayList;

import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.entity.monster.Monster;

public class WaveControl extends BasicControl {

    private ArrayList<Monster> monsters;
    private int spawnIndex = -1;
    private float curTime = 0f;
    private float spawnTime = 0f;
    private float delay = 0f;
    private float gap = 0f;
    private Portal portal = null;

    public WaveControl() {
        monsters = new ArrayList<>();
    }

    public WaveControl(ArrayList<Monster> monsters) {
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
            spatial.removeControl(this);
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

    public ArrayList<Monster> getMonsters() {
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

    public void addMonster(ArrayList<Monster> monsters) {
        this.monsters.addAll(monsters);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeSavableArrayList(monsters, "monsters", null);
        capsule.write(spawnIndex, "spawnIndex", -1);
        capsule.write(curTime, "curTime", 0f);
        capsule.write(spawnTime, "spawnTime", 0f);
        capsule.write(delay, "delay", 0f);
        capsule.write(gap, "gap", 0f);
        capsule.write(portal, "portal", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        ArrayList list = capsule.readSavableArrayList("monsters", null);
        if (!list.isEmpty()) {
            monsters = new ArrayList<>(list.size());
            for (Object el : list) {
                if (el instanceof Monster) {
                    monsters.add((Monster) el);
                }
            }
        }
        spawnIndex = capsule.readInt("spawnIndex", -1);
        curTime = capsule.readFloat("curTime", 0f);
        spawnTime = capsule.readFloat("spawnTime", 0f);
        delay = capsule.readFloat("delay", 0f);
        gap = capsule.readFloat("gap", 0f);
        portal = (Portal) capsule.readSavable("portal", null);
    }

}
