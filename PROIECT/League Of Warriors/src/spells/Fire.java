package spells;

import entities.Entity;

public class Fire extends Spell {
    public Fire() {
        super();
    }

    // change it so that it models the effect not just apply it
    @Override
    public void visit(Entity entity) {
        if (entity.isFireImmune()) {
            System.out.println("Entity is immune to fire");
            entity.reciveDamage(this.getDamage() / 5);
        } else {
            entity.reciveDamage(this.getDamage());
        }
    }

    @Override
    public String toString() {
        return "Type: fire\n" + "Damage: " + getDamage() + "\nMana cost: " + getCost();
    }
}
