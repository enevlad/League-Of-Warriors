package entities;

public class CharacterFactory {
    public static Character createCharacter(String characterType, String name, int exp, int level) {
        switch (characterType) {
            case "Warrior":
                return new Warrior(name, exp, level);
            case "Mage":
                return new Mage(name, exp, level);
            case "Rogue":
                return new Rogue(name, exp, level);
            default:
                throw new IllegalArgumentException("Invalid character type: " + characterType);
        }
    }
}
