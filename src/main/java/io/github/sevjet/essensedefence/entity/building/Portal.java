package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.util.BoxSize;

public class Portal extends Building {

    private static final BoxSize SIZE = new BoxSize(1, 2, 3);

    public Portal() {
        super(SIZE, -1f);
    }

    public Portal(float health) {
        super(SIZE, health);
    }

    public Portal(int x, int y, float health) {
        super(x, y, SIZE, health);
    }

}
