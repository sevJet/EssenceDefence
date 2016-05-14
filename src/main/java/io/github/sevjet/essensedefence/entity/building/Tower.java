package io.github.sevjet.essensedefence.entity.building;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essensedefence.control.BasicControl;
import io.github.sevjet.essensedefence.control.TowerControl;
import io.github.sevjet.essensedefence.entity.Entity;
import io.github.sevjet.essensedefence.entity.Essence;
import io.github.sevjet.essensedefence.entity.monster.Monster;
import io.github.sevjet.essensedefence.field.Field;
import io.github.sevjet.essensedefence.util.BoxSize;
import io.github.sevjet.essensedefence.util.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tower extends BuyableBuilding {

    public static final BoxSize SIZE = new BoxSize(2, 2, 3);
    private BasicControl control = new TowerControl();

    private Essence core;

    public Tower() {
        super(SIZE, -1f, 0f);

        geometry.addControl(control);
    }

    public Tower(float health, float price) {
        super(SIZE, health, price);

        geometry.addControl(control);
    }

    public Tower(int x, int y, float health, float price) {
        super(x, y, SIZE, health, price);

        geometry.addControl(control);
    }

    public Essence getCore() {
        return core;
    }

    public void putCore(Essence core) {
        if (this.core == null) {
            this.core = core;
            placeCore();
        }
    }

    public void extractionCore() {
        Field field = getField();
        if (field != null) {
            field.removeObject(this.core);
            this.core = null;
        }
        return;
    }

    private void placeCore() {
        core.setX(x);
        core.setY(y);
        core.setZ(getSize().getDepth());

        Field field = getField();
        if (field != null) {
            field.addObject(core);
        }
    }

    // TODO: 12/05/2016 change on BoundSphere
    public List<Monster> getCloseMonsters() {
        if (core == null)
            return Collections.emptyList();
        Field field = getField();
        Node monstersNode = field.getObjects(Monster.class);
        if (monstersNode == null)
            return Collections.emptyList();

        List<Monster> list = new ArrayList<Monster>();
        List<Spatial> monsterSpatials = monstersNode.getChildren();

        Vector3f bottomCenter = geometry.getLocalTranslation();
        bottomCenter.setZ(0);

        for (Spatial spat : monsterSpatials) {
            if (bottomCenter.subtract(spat.getLocalTranslation()).length() <= core.getRange()) {
                Entity entity = Getter.getEntity(spat);
                if (entity instanceof Monster)
                    list.add((Monster) entity);
                else throw new IllegalArgumentException(entity.getClass() + " isn't monster!");
            }
        }

        // TODO: 12/05/2016 delete sout
        System.out.println("Around " + bottomCenter + " are located " + list.size() + " monsters! My range is " + core.getRange());
        return list;
    }

    @Override
    public void destroy() {
        super.destroy();

        Field field = getField();
        if (field != null && core != null) {
            field.removeObject(core);
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(core, "core", null);
        capsule.write(control, "control", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        core = (Essence) capsule.readSavable("core", null);
        control = (BasicControl) capsule.readSavable("control", null);
    }
}
