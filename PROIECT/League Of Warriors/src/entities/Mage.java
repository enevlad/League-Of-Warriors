package entities;

public class Mage extends Character {
    public Mage(String name, int exp, int level){
        super(name, exp, level);
        setFireImmune(false);
        setIceImmune(true);
        setEarthImmune(false);
        strength = level;
        charisma = (int) (level * 1.5);
        dexterity = level;
    }

    @Override
    public void reciveDamage(int damage) {
        half = rand.nextBoolean();
        if (strength > 5 && dexterity > 3 && half) {
            System.out.println("Damage halved");
            setHealth(getHealth() - (damage / 2));
        } else {
            setHealth(getHealth() - damage);
        }
    }

    @Override
    public int getDamage() {
        twice = rand.nextBoolean();
        if (charisma > 5 && twice)
            return 2 * ((int) (charisma * 1.5 + strength + dexterity));
        return (int) (charisma * 1.5 + strength + dexterity);
    }

    @Override
    public void setAtributes(int level) {
        strength = level;
        charisma = (int) (level * 1.5);
        dexterity = level;
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                "\nProfession: Mage" +
                "\nLevel: " + getLevel() +
                "\nExp: " + getExp() +
                "\nStrength: " + strength +
                "\nCharisma: " + charisma +
                "\nDexterity: " + dexterity + "\n";
    }
}
