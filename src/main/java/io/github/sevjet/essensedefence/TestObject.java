package io.github.sevjet.essensedefence;

import com.jme3.export.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;

import java.io.IOException;

public class TestObject extends JME3Object implements Savable {//implements Serializable {


    public TestObject() {
    }

    public TestObject(int x, int y) {
        super(x, y);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(x, "x", 0);
        capsule.write(y, "y", 0);
//        geometry.setName("geometry");
        capsule.write(geometry, "Geometry", null);
//        geometry.write(ex);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        x = capsule.readInt("x", 0);
        y = capsule.readInt("y", 0);
        geometry = (Geometry) capsule.readSavable("Geometry", null);
//        geometry.read(im);
    }

}
