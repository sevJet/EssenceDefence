package io.github.sevjet.essensedefence.field;

import com.jme3.math.ColorRGBA;
import io.github.sevjet.essensedefence.entity.Essence;

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

//    @Override
//    public Inventory getField() {
//        Field field = super.getField();
//        if (field instanceof Inventory) {
//            return (Inventory) field;
//        }
//        return null;
//    }

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

}
