package io.github.sevjet.essensedefence;

import io.github.sevjet.essensedefence.control.ITextual;
import io.github.sevjet.essensedefence.entity.Essence;

import java.util.ArrayList;

public class Gamer implements ITextual {

    protected float gold = 0;
    protected ArrayList<Essence> extractedEssences = new ArrayList();

    public Gamer() {
        this(0);
    }

    public Gamer(int gold) {
        this.gold = gold;
    }

    public float getGold() {
        return gold;
    }

    @Deprecated
    public void setGold(float gold) {
        this.gold = gold;
    }

    public boolean decGold(float gold) {
        if (this.gold - gold < 0)
            return false;
        this.gold -= gold;
        return true;
    }

    public ArrayList<Essence> getExtractedEssences() {
        return extractedEssences;
    }

    public float incGold(float gold) {
        return this.gold += gold;
    }


    @Override
    public String outputValue() {
        return Float.toString(gold);
    }

    @Override
    public boolean isEnded() {
        return (false);
    }
}
