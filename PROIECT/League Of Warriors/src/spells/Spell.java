package spells;

import entities.Entity;
import visitor.Visitor;

import java.util.Random;

public abstract class Spell implements Visitor<Entity> {
    private int damage;
    private int cost;
    protected int turnsToApply;
    protected Random rand = new Random();

    public Spell() {
        this.damage = rand.nextInt(50);
        this.cost = rand.nextInt(40);
    }

    public int getDamage() {
        return damage;
    }

    public int getCost() {
        return cost;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public abstract String toString();
}
