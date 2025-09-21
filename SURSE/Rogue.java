package entities;

public class Rogue extends Character {
    public Rogue(String name, int exp, int level) {
        super(name, exp, level);
        setFireImmune(false);
        setEarthImmune(true);
        setIceImmune(false);
        strength = level;
        charisma = level;
        dexterity = (int) (level * 1.5);
    }

    @Override
    public void reciveDamage(int damage) {
        half = rand.nextBoolean();
        if (strength > 3 && charisma > 5 && half) {
            System.out.println("Damage halved");
            setHealth(getHealth() - (damage / 2));
        } else {
            setHealth(getHealth() - damage);
        }
    }

    @Override
    public int getDamage() {
        twice = rand.nextBoolean();
        if (dexterity > 5 && twice)
            return 2 * ((int) (dexterity * 1.5 + charisma + strength));
        return (int) (dexterity * 1.5 + charisma + strength);
    }

    @Override
    public void setAtributes(int level) {
        strength = level;
        charisma = level;
        dexterity = (int) (level * 1.5);
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                "\nProfession: Rogue" +
                "\nLevel: " + getLevel() +
                "\nExp: " + getExp() +
                "\nStrength: " + strength +
                "\nCharisma: " + charisma +
                "\nDexterity: " + dexterity + "\n";
    }
}
