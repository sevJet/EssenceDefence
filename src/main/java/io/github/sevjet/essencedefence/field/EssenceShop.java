package io.github.sevjet.essencedefence.field;

import com.jme3.math.ColorRGBA;

import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.Essence;

public class EssenceShop extends Inventory {

    @SuppressWarnings("unused")
    public EssenceShop() {
    }

    public EssenceShop(int cols, int rows) {
        super(cols, rows);
    }

    public void setDefault() {
        ColorRGBA temp;
        setContent(0, 0, new Essence(5f, 2f, 1f, 1, 10f, ColorRGBA.Red));
        setContent(1, 0, new Essence(3f, 2f, 5f, 1, 10f, ColorRGBA.Yellow));
        setContent(2, 0, new Essence(3f, 4f, 1f, 1, 10f, ColorRGBA.Blue));

        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Yellow, 0.5f);
        setContent(0, 1, new Essence(5f, 2f, 5f, 1, 20f, temp));

        temp = ColorRGBA.Blue.clone();
        temp.interpolate(ColorRGBA.Red, 0.5f);
        setContent(1, 1, new Essence(5f, 4f, 1f, 1, 20f, temp));

        temp = ColorRGBA.Yellow.clone();
        temp.interpolate(ColorRGBA.Blue, 0.5f);
        setContent(2, 1, new Essence(3f, 4f, 5f, 1, 20f, temp));


        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Yellow, 0.5f);
        temp.interpolate(ColorRGBA.Blue, 1 / 3f);
        setContent(0, 2, new Essence(5f, 4f, 5f, 1, 30f, temp));

        temp = ColorRGBA.Red.clone();
        temp.interpolate(ColorRGBA.Black, 0.25f);
        setContent(1, 2, new Essence(10f, 2f, 0.5f, 1, 50f, temp));
        temp = ColorRGBA.Yellow.clone();
        temp.interpolate(ColorRGBA.Black, 0.25f);
        setContent(2, 2, new Essence(1f, 2f, 10f, 1, 50f, temp));
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
