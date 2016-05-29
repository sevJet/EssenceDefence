package io.github.sevjet.essencedefence.control;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.io.IOException;

import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.monster.Monster;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;
import io.github.sevjet.essencedefence.util.PathBuilder;

public class MonsterControl extends BasicControl {

    private Monster monster = null;
    private Fortress fortress = null;
    private MotionPath path = null;
    private MotionEvent event = null;
    private float delay = 0f;
    private long lastUpdated = System.currentTimeMillis();

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (entity instanceof Monster) {
            monster = (Monster) entity;
        } else {
            throw new IllegalArgumentException("Not a Monster spatial");
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        delay -= tpf;
        if (delay > 0f) {
            return;
        }
        delay = 0f;

        if (path == null || fortress == null || fortress.isEnded() || monster.getField().wasUpdated(lastUpdated)) {
            if (event != null) {
                event.stop();
                spatial.removeControl(MotionEvent.class);
                event = null;
            }
            initPath();

            delay += 2f;
            lastUpdated = System.currentTimeMillis();
        }
        if (path == null) {
            delay += 10f;
        }
    }

    private void initPath() {
        buildPath();
        if (path == null) {
            delay += 5f;
            return;
        }

        path.addListener((motionControl, wayPointIndex) -> {
            if (motionControl.getPath().getNbWayPoints() == wayPointIndex + 1) {
                fortress.hit(monster.getDamage());
                monster.die();
                spatial.removeControl(this);
            }
            Vector3f curWayPoint = motionControl.getPath().getWayPoint(motionControl.getCurrentWayPoint());
            monster.move(Math.round(curWayPoint.getX()), Math.round(curWayPoint.getY()), monster.getZ(), false);
        });
        path.setPathSplineType(Spline.SplineType.Linear);

        event = new MotionEvent(spatial, path);
        event.setInitialDuration(path.getNbWayPoints());
        event.setSpeed(monster.getSpeed());
        event.setDirectionType(MotionEvent.Direction.PathAndRotation);

        event.play();
    }

    private void buildPath() {
        MapField field = monster.getField();
        path = PathBuilder.atField(field)
                .from(monster.getX(), monster.getY())
                .floatingAt(monster.getPhysicalCenter())
                .to(Fortress.class)
                .build();
        if (path != null && path.getNbWayPoints() > 0) {
            Vector3f point = path.getWayPoint(path.getNbWayPoints() - 1);
            MapCell cell = field.getCell(Math.round(point.getX()), Math.round(point.getY()));
            if (cell.hasContent() && cell.getContent() instanceof Fortress) {
                fortress = (Fortress) cell.getContent();
            }
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);

        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(monster, "monster", null);
        capsule.write(fortress, "fortress", null);
        capsule.write(path, "path", null);
        capsule.write(event, "event", null);
        capsule.write(delay, "delay", 0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);

        InputCapsule capsule = im.getCapsule(this);
        monster = (Monster) capsule.readSavable("monster", null);
        fortress = (Fortress) capsule.readSavable("fortress", null);
        path = (MotionPath) capsule.readSavable("path", null);
        event = (MotionEvent) capsule.readSavable("event", null);
        delay = capsule.readFloat("delay", 0f);
    }

}
