package entities;

public class Warrior extends Character {
    public Warrior(String name, int exp, int level) {
        super(name, exp, level);
        setFireImmune(true);
        setEarthImmune(false);
        setIceImmune(false);
        strength = (int) (level * 1.5);
        charisma = level;
        dexterity = level;
    }

    @Override
    public void reciveDamage(int damage) {
        half = rand.nextBoolean();
        if (charisma > 3 && dexterity > 5 && half) {
            System.out.println("Damage halved");
            setHealth(getHealth() - (damage / 2));
        } else {
            setHealth(getHealth() - damage);
        }
    }

    @Override
    public int getDamage() {
        twice = rand.nextBoolean();
        if (strength > 5 && twice)
            return 2 * ((int) (strength * 1.5 + charisma + dexterity));
        return (int) (strength * 1.5 + charisma + dexterity);
    }

    @Override
    public void setAtributes(int level) {
        strength = (int) (level * 1.5);
        charisma = level;
        dexterity = level;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                "\nProfession: Warrior" +
                "\nLevel: " + getLevel() +
                "\nExp: " + getExp() +
                "\nStrength: " + strength +
                "\nCharisma: " + charisma +
                "\nDexterity: " + dexterity + "\n";
    }
}
