package io.github.sevjet.essencedefence.entity.building;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

import java.io.IOException;

import io.github.sevjet.essencedefence.entity.IBuyable;
import io.github.sevjet.essencedefence.util.BoxSize;

public abstract class BuyableBuilding extends Building implements IBuyable {

    protected float price = 0f;

    public BuyableBuilding() {
        super();
    }

    public BuyableBuilding(BoxSize size, float health, float price) {
        super(size, health);
        setPrice(price);
    }

    public BuyableBuilding(int x, int y, BoxSize size, float health, float price) {
        super(x, y, size, health);
        setPrice(price);
    }

    @Override
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price < 0f) {
            throw new IllegalArgumentException("Price can not be negative");
        }

        this.price = price;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(price, "price", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        price = capsule.readFloat("price", 0f);
    }
}
