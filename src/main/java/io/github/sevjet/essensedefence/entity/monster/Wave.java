package io.github.sevjet.essensedefence.entity.monster;

import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.field.Field;

import java.util.ArrayList;
import java.util.List;

public class Wave {

    private List<Monster> monsters;
    private long startTime = 0L;
    private long delay = 0L;
    private long gap = 0L;
    private Field field = null;
    private Portal portal = null;

    public Wave() {
        monsters = new ArrayList<>();
    }

    public Wave(List<Monster> monsters) {
        this();
        this.monsters.addAll(monsters);
    }

    public Wave(List<Monster> monsters, Portal portal) {
        this(monsters);
        setPortal(portal);
    }

    public void start(Portal portal) {
        setPortal(portal);
        start();
    }

    public void start() {
        if (portal != null) {
            if (field == null) {
                field = portal.getField();
            }
            if (field != null) {
                startTime = System.currentTimeMillis();

                MonsterSpawn thread = new MonsterSpawn(this);
                thread.run();
            }
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getGap() {
        return gap;
    }

    public void setGap(long gap) {
        this.gap = gap;
    }

    public Field getField() {
        return field;
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        if (portal != null) {
            this.portal = portal;
            this.field = portal.getField();
        }
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void addMonster(List<Monster> monsters) {
        this.monsters.addAll(monsters);
    }

    private class MonsterSpawn extends Thread {

        private Wave wave;

        public MonsterSpawn(Wave wave) {
            super("Monster wave spawn");
            this.wave = wave;

            setDaemon(true);
        }

        @Override
        public void run() {
            if(wave.getField() == null || wave.getPortal() == null) {
                return;
            }

            Field field = wave.getField();
            // FIXME point to center
            int x = wave.getPortal().getX();
            int y = wave.getPortal().getY();
            List<Monster> monsters = wave.getMonsters();
            int spawnIndex = 0;

            long curTime = wave.getStartTime();
            long delay = wave.getDelay();
            long gap = wave.getGap();
            if (System.currentTimeMillis() > curTime + delay) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (monsters.size() > spawnIndex) {
                curTime = System.currentTimeMillis();
                Monster monster = monsters.get(spawnIndex++);
                monster.setX(x);
                monster.setY(y);
                field.addObject(monster);
                if (System.currentTimeMillis() > curTime + gap) {
                    try {
                        Thread.sleep(gap);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
