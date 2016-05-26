package io.github.sevjet.essensedefence.field;

import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;

public class Inventory extends Field<InventoryCell> {

    @SuppressWarnings("unused")
    public Inventory() {
    }

    public Inventory(final int cols, final int rows) {
        super(cols, rows);
    }

    @Override
    protected InventoryCell newCell(final int x, final int y) {
        return new InventoryCell(x, y);
    }

    public boolean addEssence(final Essence essence) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                final InventoryCell cell = getCell(i, j);
                if (!cell.hasContent()) {
                    return addEssence(essence, cell);
                }
            }
        }
        return false;
    }

    public boolean addEssence(final Essence essence, final int x, final int y) {
        return addEssence(essence, getCell(x, y));
    }

    public boolean addEssence(final Essence essence, final InventoryCell cell) {
        if (cell != null) {
            if (!cell.hasContent()) {
                cell.setContent(essence);
                essence.move(cell.getX(), cell.getY(), 0);
                essence.setZ(0);
                addObject(essence);
                return true;
            }
        }
        return false;
    }

    public Essence getEssence(final InventoryCell cell) {
        return cell != null ? cell.getContent() : null;
    }

    public Essence getEssence(final int x, final int y) {
        return getEssence(getCell(x, y));
    }

    public Essence takeEssence(final InventoryCell cell) {
        Essence essence = getEssence(cell);
        if (essence != null) {
            removeObject(essence);
            cell.setContent(null);
        }
        return essence;
    }

    public boolean contains(final Essence essence) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                final InventoryCell cell = getCell(i, j);
                if (cell.getContent().equals(essence)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Entity getContent(InventoryCell cell, Class<? extends Entity> contentClass) {
        Entity contentObject;
        try {
            contentObject = contentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        if (contentObject instanceof Essence) {
            return takeEssence(cell);
        }

        return null;
    }

    @Override
    public boolean setContent(InventoryCell cell, Entity content) {
        if (content instanceof Essence) {
            addEssence((Essence) content, cell);
            return true;
        }
        return false;
    }

    @Override
    public boolean canGet(InventoryCell cell, Class<? extends Entity> contentClass) {
        return cell.hasContent();
    }

    @Override
    public boolean canSet(InventoryCell cell, Class<? extends Entity> contentClass) {
        return !cell.hasContent();
    }
}
