package entities;

public abstract class Character extends Entity {
    private String name;
    private int exp;
    private int level;
    protected int strength, charisma, dexterity;
    protected boolean half;
    protected boolean twice;

    public Character(String name, int exp, int level) {
        super();
        this.name = name;
        this.exp = exp;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // returns boolean telling if damage dealt to character was halved
    public boolean getHalf() {
        return half;
    }

    public void levelUp() {
        while (this.exp >= 100) {
            System.out.println("You have leveled up!!!");
            this.exp -= 100;
            this.level++;
        }
        this.setAtributes(this.level);
    }

    public abstract void setAtributes(int level);

    @Override
    public abstract String toString();

    public boolean getCritical() {
        return twice;
    }
}
