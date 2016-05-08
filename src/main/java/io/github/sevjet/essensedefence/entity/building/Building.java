package io.github.sevjet.essensedefence.entity.building;

import io.github.sevjet.essensedefence.entity.Entity3D;

public abstract class Building extends Entity3D {
    public Building(int height, int width, int depth) {
        super(height, width, depth);
    }
}
