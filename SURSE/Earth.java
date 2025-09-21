package spells;

import entities.Entity;

public class Earth extends Spell {
    public Earth() {
        super();
    }

    // change it so that it models the effect not just apply it
    @Override
    public void visit(Entity entity) {
        if (entity.isEarthImmune()) {
            System.out.println("Entity is immune to earth");
            entity.reciveDamage(this.getDamage() / 5);
        } else {
            entity.reciveDamage(this.getDamage());
        }
    }

    @Override
    public String toString() {
        return "Type: earth\n" + "Damage: " + getDamage() + "\nMana cost: " + getCost();
    }
}
