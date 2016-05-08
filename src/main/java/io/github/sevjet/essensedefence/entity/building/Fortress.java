package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.util.BoxSize;

public class Fortress extends Building {

    private static final BoxSize SIZE = new BoxSize(3, 3, 4);

    public Fortress() {
        super(SIZE, -1f);
    }

    public Fortress(float health) {
        super(SIZE, health);
    }

    public Fortress(int x, int y, float health) {
        super(x, y, SIZE, health);
    }

}
