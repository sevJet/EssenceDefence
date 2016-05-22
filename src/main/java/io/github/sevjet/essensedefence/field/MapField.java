package io.github.sevjet.essensedefence.field;

import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.building.Building;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.entity.building.Portal;
import io.github.sevjet.essensedefence.entity.building.Tower;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.gui.GuiControl;

public class MapField extends Field<MapCell> {

    @SuppressWarnings("unused")
    public MapField() {
    }

    public MapField(final int cols, final int rows) {
        super(cols, rows);
    }

    public int[][] getPassable() {
        int passable[][] = new int[getRows()][];
        for (int i = 0; i < getRows(); i++) {
            passable[i] = new int[getCols()];
            for (int j = 0; j < getCols(); j++) {
                final MapCell cell = getCell(i, j);
                passable[i][j] = (cell.isPassable() &&
                        (!cell.hasContent() ||
                                cell.getContent() instanceof Fortress) ||
                        cell.getContent() instanceof Portal) ? 0 : -1;
            }
        }
        return passable;
    }

    public void build(final int x, final int y, final Building building) {
        for (int i = x; i < x + building.getSize().getWidth(); i++) {
            for (int j = y; j < y + building.getSize().getHeight(); j++) {
                getCell(i, j).setContent(building);
            }
        }
        building.setX(x);
        building.setY(y);

        addObject(building);

        building.build();
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
        if (super.removeObject(entity)) {
            if (entity instanceof Building) {
                freeCells((Building) entity);
            }
            return true;
        }
        return false;
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
                getCell(i, j).free();
            }
        }
    }

    @Override
    protected void guiFor(Entity object) {
        if (object instanceof Fortress) {
            this.addControl(new GuiControl(object, "XP:", 3f, 1f / 5f));
        }
        if (object instanceof Monster) {
            this.addControl(new GuiControl(object, "xp:", 1f, 1f / 8f));
        }
    }


    @Override
    public Entity getContent(MapCell cell, Class<? extends Entity> contentClass) {
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
            Tower tower = (Tower) cell.getContent();
            Essence essence = tower.getCore();
            tower.extractCore();
            return essence;
        }
        if (contentObject instanceof Tower) {

        }

        return null;
    }


    @Override
    public boolean setContent(MapCell cell, Entity content) {
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
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
}
