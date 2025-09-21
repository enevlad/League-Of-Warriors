package spells;

import entities.Entity;

public class Ice extends Spell {
    public Ice() {
        super();
    }

    // change it so that it models the effect not just apply it
    @Override
    public void visit(Entity entity) {
        if (entity.isIceImmune()) {
            System.out.println("Entity is immune to ice");
            entity.reciveDamage(this.getDamage() / 5);
        } else {
            entity.reciveDamage(this.getDamage());
        }
    }

    @Override
    public String toString() {
        return "Type: ice\n" + "Damage: " + getDamage() + "\nMana cost: " + getCost();
    }
}
