package io.github.sevjet.essencedefence.entity.building;

import com.jme3.bounding.BoundingSphere;
import com.jme3.effect.Particle;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Node;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.sevjet.essencedefence.control.BasicControl;
import io.github.sevjet.essencedefence.control.TowerControl;
import io.github.sevjet.essencedefence.entity.Essence;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.Field;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.BoxSize;
import io.github.sevjet.essencedefence.util.Creator;
import io.github.sevjet.essencedefence.util.RayHelper;

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
        if (this.core == null && core != null) {
            this.core = core;
            placeCore();
        }
    }

    public Essence extractCore() {
        Essence essence = null;
        Field field = getField();
        if (field != null && core != null) {
            essence = core;
            core.setOffsetX(0f);
            core.setOffsetY(0f);

            field.removeObject(core);
            core = null;
        }
        return essence;
    }

    private void placeCore() {
        core.move(getX(), getY(), getSize().getDepth());

        core.setOffsetX(0.5f);
        core.setOffsetY(0.5f);

        Field field = getField();
        if (field != null) {
            field.addObject(core);
        }
    }

    public List<Monster> getCloseMonsters() {
        if (isEmpty()) {
            return Collections.emptyList();
        }

        final MapField field = getField();
        final Node monstersNode = field.getObjects(Monster.class);
        if (monstersNode == null) {
            return Collections.emptyList();
        }

        BoundingSphere sphere = new BoundingSphere(core.getRange(),
                geometry.getLocalTranslation().setZ(0));

        return RayHelper.collide(sphere, monstersNode.getChildren()).stream()
                .filter(entity -> entity instanceof Monster)
                .map(entity -> (Monster) entity)
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        super.destroy();

        Field field = getField();
        if (field != null && core != null) {
            field.removeObject(core);
        }
    }

    public float getCooldownTime() {
        return (!isEmpty() ? 1f / core.getSpeed() : 0f);
    }

    public boolean isEmpty() {
        return core == null;
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
