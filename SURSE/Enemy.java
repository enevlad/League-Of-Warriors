package entities;

import java.util.Random;

public class Enemy extends Entity {
    Random rand = new Random();
    private int damage, min, max;
    protected boolean critical;
    protected boolean evade;

    public Enemy() {
        setMaxHealth(rand.nextInt(101));
        setHealth(getMaxHealth());
        setMaxMana(rand.nextInt(101));
        setMana(getMaxMana());
        setEarthImmune(rand.nextBoolean());
        setFireImmune(rand.nextBoolean());
        setIceImmune(rand.nextBoolean());
        min = 10;
        max = 30;
        damage = rand.nextInt((max - min) + 1) + min;
    }

    @Override
    public void reciveDamage(int damage) {
        evade = rand.nextBoolean();
        if (evade) {
            return;
        } else {
            setHealth(getHealth() - damage);
        }
    }

    @Override
    public int getDamage() {
        critical = rand.nextBoolean();
        if (critical) {
            return 2 * damage;
        }
        return damage;
    }

    public boolean getEvade() {
        return evade;
    }

    public boolean getCritical() {
        return critical;
    }
}
