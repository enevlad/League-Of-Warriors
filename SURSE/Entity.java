package entities;

import spells.*;
import visitor.*;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.*;

public abstract class Entity implements Battle, Element<Entity> {
    Random rand = new Random();
    private ArrayList<Spell> spells;
    private int max_health;
    private int health;
    private int max_mana;
    private int mana;
    private boolean fire_immune;
    private boolean ice_immune;
    private boolean earth_immune;


    public Entity() {
        max_health = 100;
        health = max_health;
        max_mana = 100;
        mana = max_mana;
        fire_immune = false;
        ice_immune = false;
        earth_immune = false;
    }

    public void generateSpells() {
        spells = new ArrayList<Spell>();
        // 3 - 6 spelluri random
        spells.add(new Fire());
        spells.add(new Ice());
        spells.add(new Earth());
        int number = rand.nextInt(4);
        for (int i = 0; i < number; i++) {
            int type = rand.nextInt(3);
            switch (type) {
                case 0:
                    spells.add(new Fire());
                    break;
                case 1:
                    spells.add(new Ice());
                    break;
                case 2:
                    spells.add(new Earth());
                    break;
                default:
                    break;
            }
        }
    }

    public void regenHealth(int regen) {
        if (health + regen >= max_health)
            health = max_health;
        else
            health += regen;
    }

    public void regenMana(int regen) {
        if (mana + regen >= max_mana)
            mana = max_mana;
        else
            mana += regen;
    }

    public void setFireImmune(boolean fireImmune) {
        this.fire_immune = fireImmune;
    }

    public void setIceImmune(boolean iceImmune) {
        this.ice_immune = iceImmune;
    }

    public void setEarthImmune(boolean earthImmune) {
        this.earth_immune = earthImmune;
    }

    public void setMaxHealth(int maxHealth) {
        this.max_health = maxHealth;
    }

    public void setMaxMana(int maxMana) {
        this.max_mana = maxMana;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public int getMaxHealth() {
        return max_health;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxMana() {
        return max_mana;
    }

    public int getMana() {
        return mana;
    }

    public boolean isFireImmune() {
        return fire_immune;
    }

    public boolean isIceImmune() {
        return ice_immune;
    }

    public boolean isEarthImmune() {
        return earth_immune;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    // metoda ce primeste spell si inamicul(PLAYER/ENEMY)
    // verifica mana daca e destula foloseste abilitatea
    // se folosesc reciveDamage si getDamage
    public void useSpell(Spell spell, Entity target) {
        int damage;
        if (spell.getCost() > mana) {
            damage = getDamage();
            target.reciveDamage(damage);
        } else {
            System.out.println(spell);
            int baseDamage = getDamage();
            spell.setDamage(spell.getDamage() + baseDamage);
            target.accept(spell);
            damage = spell.getDamage();
            spell.setDamage(spell.getDamage() - baseDamage);
            this.mana -= spell.getCost();
        }
        if ((spell instanceof Fire && target.isFireImmune()) ||
                (spell instanceof Ice && target.isIceImmune())||
                (spell instanceof Earth && target.isEarthImmune()))
            damage /= 5;
        if (target instanceof Character) {
            if (((Enemy)this).critical)
                System.out.println("Enemy landed a critical");
            if (((Character)target).half) {
                if ((target instanceof Mage && ((Mage)target).strength > 5 && ((Mage)target).dexterity > 3) ||
                        (target instanceof Rogue && ((Rogue)target).strength > 3 && ((Rogue)target).charisma > 5) ||
                        (target instanceof Warrior && ((Warrior)target).dexterity > 5 && ((Warrior)target).charisma > 3)) {
                    damage /= 2;
                }
            }
            System.out.println("Damage dealt " + damage + ".");
        } else if (target instanceof Enemy) {
            if (((Enemy)target).evade)
                System.out.println("Enemy evaded your attack");
            else {
                if (((Character)this).twice) {
                    System.out.println("You landed a critical");
                }
                System.out.println("Damage dealt " + damage + ".");
            }
        }
    }
}
