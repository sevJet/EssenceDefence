package io.github.sevjet.essencedefence.field;

import io.github.sevjet.essencedefence.GamePlayAppState;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.entity.building.Building;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Tower;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.gui.GuiControl;
import io.github.sevjet.essencedefence.util.Configuration;

public class MapField extends Field<MapCell> {

    private long lastUpdate = System.currentTimeMillis();

    @SuppressWarnings("unused")
    public MapField() {
        super();
    }

    public MapField(final int cols, final int rows) {
        super(cols, rows);
    }

    public void build(final int x, final int y, final Building building) {
        for (int i = x; i < x + building.getSize().getWidth(); i++) {
            for (int j = y; j < y + building.getSize().getHeight(); j++) {
                final MapCell cell = getCell(i, j);
                cell.setContent(building);
            }
        }
        building.move(x, y);

        addObject(building);

        building.build();

        lastUpdate = System.currentTimeMillis();
    }

    public void build(final Building building) {
        build(building.getX(), building.getY(), building);
    }

    @Override
    protected MapCell newCell(final int x, final int y) {
        return new MapCell(x, y);
    }

    @Override
    public boolean removeObject(final Entity entity) {
        lastUpdate = System.currentTimeMillis();

        if (entity instanceof Building) {
            freeCells((Building) entity);
        }
        return super.removeObject(entity);
    }

    public boolean enoughPlaceFor(final MapCell cell, final Building building) {
        for (int i = cell.getX(); i < cell.getX() + building.getSize().getWidth(); i++) {
            for (int j = cell.getY(); j < cell.getY() + building.getSize().getHeight(); j++) {
                final MapCell temp = getCell(i, j);
                if (temp == null || temp.hasContent()) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void freeCells(final Building building) {
        building.destroy();

        for (int i = building.getX(); i < building.getX() + building.getSize().getWidth(); i++) {
            for (int j = building.getY(); j < building.getY() + building.getSize().getHeight(); j++) {
                final MapCell cell = getCell(i, j);
                cell.free();
            }
        }
    }

    @Override
    protected void guiFor(Entity object) {
        GuiControl guiControl = null;
        if (object instanceof Fortress) {
            guiControl = new GuiControl(object, "HP:", 6f, 1f / 3f);
            this.addControl(guiControl);
        }
        if (object instanceof Monster) {
            guiControl = new GuiControl(object, "hp:", 2f, 1f / 5f);
            this.addControl(guiControl);
        }
        if (guiControl != null) {
            Configuration.getAppState().getState(GamePlayAppState.class).
                    getGui().attachChild(guiControl.getControlNode());
        }
    }


    @Override
    public Entity getContent(MapCell cell, Class<? extends Entity> contentClass) {
        Entity contentObject;
        try {
            contentObject = contentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }

        if (contentObject instanceof Essence) {
            Tower tower = (Tower) cell.getContent();
            return tower.extractCore();
        }

        return null;
    }


    @Override
    public boolean setContent(MapCell cell, Entity content) {
        lastUpdate = System.currentTimeMillis();

        if (content instanceof Essence) {
            Tower tower = (Tower) cell.getContent();
            if (tower == null)
                return false;
            tower.putCore((Essence) content);
            return true;
        }
        return false;
    }

    @Override
    public boolean canGet(MapCell cell, Class<? extends Entity> contentClass) {
        Entity contentObject;
        try {
            contentObject = contentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return false;
        }

        if (contentObject instanceof Building) {
            return cell.hasContent();
        }
        if (contentObject instanceof Essence) {
            if (cell.hasContent() &&
                    cell.getContent() instanceof Tower) {
                return ((Tower) cell.getContent()).getCore() != null;
            }
        }

        return false;
    }

    @Override
    public boolean canSet(MapCell cell, Class<? extends Entity> contentClass) {
        Entity contentObject;
        try {
            contentObject = contentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return false;
        }

        if (contentObject instanceof Building) {
            return !cell.hasContent();
        }
        if (contentObject instanceof Essence) {
            if (cell.hasContent() &&
                    cell.getContent() instanceof Tower) {
                return ((Tower) cell.getContent()).getCore() == null;
            }
        }

        return false;
    }

    public boolean wasUpdated(long from) {
        return lastUpdate > from;
    }
}
