package io.github.sevjet.essencedefence.game;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import java.io.IOException;

import io.github.sevjet.essencedefence.field.EssenceShop;
import io.github.sevjet.essencedefence.field.Inventory;
import io.github.sevjet.essencedefence.field.MapField;

public class Round implements Savable {

    private Player player;
    private MapField field;
    private EssenceShop shop;

    @SuppressWarnings("unused")
    public Round() {

    }

    public Round(final Player player, final MapField field, final EssenceShop shop) {
        if (player == null) {
            throw new IllegalArgumentException("Player can not be null");
        }
        if (field == null) {
            throw new IllegalArgumentException("Field can not be null");
        }
        if (shop == null) {
            throw new IllegalArgumentException("Shop can not be null");
        }

        this.player = player;
        this.field = field;
        this.shop = shop;
    }

    public Round(final Player player, final MapField field) {
        if (player == null) {
            throw new IllegalArgumentException("Player can not be null");
        }
        if (field == null) {
            throw new IllegalArgumentException("Field can not be null");
        }

        this.player = player;
        this.field = field;
        this.shop = new EssenceShop(3, 3);

        this.shop.setDefault();
    }

    public Player player() {
        return player;
    }

    public Inventory inventory() {
        return player != null ? player.inventory() : null;
    }

    public EssenceShop shop() {
        return shop;
    }

    public MapField filed() {
        return field;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);

        capsule.write(player, "player", null);
        capsule.write(field, "field", null);
        capsule.write(shop, "shop", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);

        player = (Player) capsule.readSavable("player", new Player());
        field = (MapField) capsule.readSavable("field", null);
        shop = (EssenceShop) capsule.readSavable("shop", null);

        if (shop == null) {
            shop = new EssenceShop(3, 3);
            shop.setDefault();
        }
    }
}
