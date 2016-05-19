package io.github.sevjet.essensedefence.field;

import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.util.Configuration;

public class Inventory extends Field<InventoryCell> {

    @SuppressWarnings("unused")
    public Inventory() {
    }

    public Inventory(final int cols, final int rows) {
        super(cols, rows);

        // TODO: 18.05.16 Move this thing away
        Configuration.getRootNode().attachChild(this);
        setLocalTranslation(70f, 10f, 1f);
        scale(5);
//        rotate(
//                -90 * FastMath.DEG_TO_RAD,
//                +90 * FastMath.DEG_TO_RAD,
//                000 * FastMath.DEG_TO_RAD
//        );
//        grid.rotate(
//                -90 * FastMath.DEG_TO_RAD,
//                +90 * FastMath.DEG_TO_RAD,
//                000 * FastMath.DEG_TO_RAD
//        );
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
                    cell.setContent(essence);
                    addObject(essence);
                    return true;
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
                if (getCell(i, j).getContent().equals(essence)) {
                    return true;
                }
            }
        }
        return false;
    }

}
