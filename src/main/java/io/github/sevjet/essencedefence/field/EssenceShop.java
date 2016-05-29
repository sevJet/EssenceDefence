package io.github.sevjet.essencedefence.field;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.Essence;

public class EssenceShop extends Inventory {

    public EssenceShop() {
    }

    public EssenceShop(int cols, int rows) {
        super(cols, rows);
    }

    @Override
    protected InventoryCell newCell(int x, int y) {
        return new InventoryCell(x, y);
    }

    @Override
    public boolean setContent(InventoryCell cell, Entity content) {
        if (content instanceof Essence) {
            if (cell.hasContent()) {
                ((Essence) content).sell();
                return true;
            }
            addEssence((Essence) content, cell);
            return true;
        }
        return false;
    }

    @Override
    public boolean canGet(InventoryCell cell, Class<? extends Entity> contentClass) {
        return true;
    }

    @Override
    public boolean canSet(InventoryCell cell, Class<? extends Entity> contentClass) {
        return true;
    }

    @Override
    public Essence takeEssence(final InventoryCell cell) {
        return getEssence(cell).clone();
    }
}
