package io.github.sevjet.essencedefence.field;

import com.jme3.math.ColorRGBA;

import io.github.sevjet.essencedefence.entity.Essence;

public class InventoryCell extends Cell<Essence> {

    public InventoryCell() {
        super();
    }

    public InventoryCell(final int x, final int y) {
        super(x, y);
    }

    public InventoryCell(final int x, final int y, final Essence content) {
        super(x, y, content);
    }

    public Essence extractEssence() {
        if (hasContent()) {
            final Essence buf = content;
            getField().removeObject(buf);
            free();

            return buf;
        }
        return null;
    }

    @Override
    protected boolean update() {
        if (super.update()) {
            getGeometry().getMaterial().setColor("Color",
                    new ColorRGBA(
                            (hasContent() ? 1 : 0),
                            (hasContent() ? 1 : 0),
                            (hasContent() ? 1 : 0),
                            1));
            return true;
        }
        return false;
    }

    @Override
    public Inventory getField() {
        return (Inventory) super.getField();
    }
}
