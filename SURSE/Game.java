package game;

import accounts.Account;
import accounts.JsonInput;
import entities.Character;
import entities.Enemy;
import map.CellEntityType;
import map.Grid;
import spells.Spell;
import ui.GameUI;
import exceptions.*;

import java.util.*;

public class Game {
    private static Game game = null;
    private Random rand;
    private ArrayList<Account> accounts;
    private Grid map;
    private Scanner input;
    private Account account;
    private Enemy enemy;
    private Character choosen;
    private GameUI ui;
    // test = true -> hardcoded map for testing
    private boolean test;
    private boolean over;
    private boolean portal;
    private boolean inBattle;

    private Game() {
        accounts = JsonInput.deserializeAccounts();
        rand = new Random();
        input = new Scanner(System.in);
        account = null;
        over = false;
        portal = false;
        test  = false;
        inBattle = false;
    }

    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return game;
    }

    public String getInput(String prompt) {
        System.out.println(prompt);
        return input.nextLine();
    }

    public void enemyAttack() {
        boolean normal = rand.nextBoolean();
        if (normal) {
            int damage  = enemy.getDamage();
            map.getCharacter().reciveDamage(damage);
            if (enemy.getCritical())
                System.out.println("Enemy landed a critical");
            if (map.getCharacter().getHalf())
                System.out.println("Damage halved " + (damage / 2) + ".");
            else
                System.out.println("Damage dealt " + damage + ".");
        } else {
            int number = rand.nextInt(enemy.getSpells().size());
            enemy.useSpell(enemy.getSpells().get(number), map.getCharacter());
        }
    }

    public void fightEnd() {
        map.getCharacter().regenHealth(2 * map.getCharacter().getHealth());
        System.out.println("You have regenerated you health, now you have " +
                map.getCharacter().getHealth() + "/" + map.getCharacter().getMaxHealth() + ".");
        // between 10 and 30 exp for defeating the enemy
        int exp = rand.nextInt(21) + 10;
        map.getCharacter().setExp(map.getCharacter().getExp() + exp);
        System.out.println("You received " + exp + "xp.");
        System.out.println("Now you have " + map.getCharacter().getExp() + "xp.");
        if (map.getCharacter().getExp() >= 100)
            map.getCharacter().levelUp();
    }

    // code for fighting logic
    // combatants take turns
    public void handleCombat() {
        System.out.println("You have encountered an enemy.");
        enemy = new Enemy();
        enemy.generateSpells();
        map.getCharacter().generateSpells();
        int number;
        while (!over) {
            // PLAYER attacks
            System.out.println("Your health " + map.getCharacter().getHealth() + "/" + map.getCharacter().getMaxHealth() + " and mana "  +
                                map.getCharacter().getMana() + "/" + map.getCharacter().getMaxMana());
            System.out.println("Enemy health " + enemy.getHealth() + "/" + enemy.getMaxHealth() + " and mana " +
                                enemy.getMana() + "/" + enemy.getMaxMana());
            String attack = getInput("Your turn\nWhat will you do?\n1. Normal attack\n2. Spell");
            if (attack.equals("1")) {
                int damage = map.getCharacter().getDamage();
                enemy.reciveDamage(damage);
                if (enemy.getEvade())
                    System.out.println("Enemy evaded the attack.");
                else
                    System.out.println("Damage dealt " + damage + ".");
            } else if (attack.equals("2")) {
                System.out.println("Choose spell");
                int index = 0;
                for (Spell spell : map.getCharacter().getSpells()) {
                    index++;
                    System.out.println(index + ". " + spell.toString());
                    System.out.println();
                }
                String spell = getInput("Which spell will you choose? (Input the number)");
                try {
                    number = Integer.parseInt(spell);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid command. " + spell + " is not a number");
                    continue;
                }
                if (number > map.getCharacter().getSpells().size()) {
                    System.out.println("Invalid command. " + spell + " is too large.");
                    continue;
                }
                map.getCharacter().useSpell(map.getCharacter().getSpells().get(number - 1), enemy);
            } else {
                System.out.println("Invalid command. Try again!");
                continue;
            }

            if (enemy.getHealth()  <= 0) {
                System.out.println("Enemy slain");
                System.out.println();
                break;
            }

            // ENEMY attacks -> (50% normal attack / 50% spell)
            System.out.println();
            System.out.println("Enemy turn");
            enemyAttack();

            if (map.getCharacter().getHealth() <= 0) {
                System.out.println("You died");
                over = true;
            }
        }
        if (!over) {
           fightEnd();
        }
    }

    public void handleSanctuary() {
        System.out.println("You have arrived at a a sanctuary. You will regenerate a part of your health and mana.");
        map.getCharacter().regenHealth(rand.nextInt(50));
        map.getCharacter().regenMana(rand.nextInt(50));
    }

    // xp received = (maps completed on account + 1) * 5
    // character lvl threshold = 100
    public void handlePortal() {
        System.out.println("You have arrived at a portal");
        int exp = (account.getGamesPlayed() + 1) * 5;
        System.out.println("You have earned " + exp + " exp.");
        map.getCharacter().setExp(map.getCharacter().getExp() + exp);
        if (map.getCharacter().getExp() >= 100)
            map.getCharacter().levelUp();
        account.setGamesPlayed(account.getGamesPlayed() + 1);
        System.out.println(map.getCharacter().toString());
    }

    public void movement() throws InvalidCommand {
        String move = getInput("Where would you like to go next?\n1. North(w)\n2. East(d)\n3. South(s)\n4. West(a)");
        switch (move) {
            case "w" -> {
                try {
                    map.goNorth();
                } catch (ImpossibleMove e) {
                    System.out.println(e.getMessage());
                }
            }
            case "d" -> {
                try {
                    map.goEast();
                } catch (ImpossibleMove e) {
                    System.out.println(e.getMessage());
                }
            }
            case "s" -> {
                try {
                    map.goSouth();
                } catch (ImpossibleMove e) {
                    System.out.println(e.getMessage());
                }
            }
            case "a" -> {
                try {
                    map.goWest();
                } catch (ImpossibleMove e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> throw new InvalidCommand("Invalid command. Try again!");
        }
    }

    // function takes the next move of the character
    // checks the cell which the player visited
    // runs the methods handling the cell which the player landed on
    public void handleCell() throws InvalidCommand {
        movement();
        if (map.getCopy() == CellEntityType.SANCTUARY &&
                !map.get(map.getCurrent().getX()).get(map.getCurrent().getY()).isVisited()) {
            handleSanctuary();
        } else if (map.getCopy() == CellEntityType.ENEMY &&
                !map.get(map.getCurrent().getX()).get(map.getCurrent().getY()).isVisited()) {
            handleCombat();
            if (over)
                return;
        } else if (map.getCopy() == CellEntityType.PORTAL &&
                !map.get(map.getCurrent().getX()).get(map.getCurrent().getY()).isVisited()) {
            portal = true;
            handlePortal();
            return;
        }
        map.get(map.getCurrent().getX()).get(map.getCurrent().getY()).setVisited(true);
    }

    public Account checkEmail(String email) {
        for (Account account : accounts) {
            if (account.getInfo().getCredentials().getEmail().equals(email))
                return account;
        }
        return null;
    }

    public boolean checkPassword(String password, Account account) {
        return account.getInfo().getCredentials().getPassword().equals(password);
    }

    public void loginAccount() {
        boolean try_pass = false;
        while (account == null) {
            String email = getInput("Input email:");
            if ((account = checkEmail(email)) == null) {
                System.out.println("Account email not found. Try again!");
                System.out.println();
            }
        }
        while (!try_pass) {
            String password = getInput("Input password:");
            try_pass = checkPassword(password, account);
            if (!try_pass) {
                System.out.println("Invalid password. Try again!");
                System.out.println();
            }
        }
    }

    // runs the whole game
    public void run() {
        System.out.println("Running game in terminal mode...");
        System.out.println();
        System.out.println("------------------------------------");
        System.out.println("Welcome to the League Of Warriors!");
        System.out.println();
        System.out.println("------------------------------------");
        loginAccount();
        while (true) {
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println();
            System.out.println("Choose a character:");
            for (entities.Character character : account.getCharacters()) {
                System.out.println(character.toString() + "\n");
                System.out.println("------------------------------------");
                System.out.println();
            }
            choosen = null;
            while (choosen == null) {
                String characterName = getInput("Input character name to choose:");
                for (entities.Character character : account.getCharacters()) {
                    if (character.getName().equals(characterName)) {
                        choosen = character;
                        break;
                    }
                }
                if (choosen == null) {
                    System.out.println("Invalid character name. Try again!");
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println("------------------------------------");
            System.out.println();
            System.out.println(choosen);
            if (test)
                map = Grid.generateTestMap();
            else
                map = Grid.generateMap(rand.nextInt(6) + 5, rand.nextInt(6) + 5);
            over = false;
            choosen.setHealth(choosen.getMaxHealth());
            choosen.setMana(choosen.getMaxMana());
            map.setCharacter(choosen);
            while (!over) {
                System.out.println();
                System.out.println("------------------------------------");
                System.out.println();
                map.showMap();
                System.out.println();
                while (true) {
                    try {
                        handleCell();
                        break;
                    } catch (InvalidCommand invalidCommand) {
                        System.out.println(invalidCommand.getMessage());
                    }
                }
                if (portal) {
                    map = Grid.generateMap(rand.nextInt(6) + 5, rand.nextInt(6) + 5);
                    map.setCharacter(choosen);
                    portal = false;
                }
            }
        }
    }

    public void runGraphical() {
        ui = new GameUI();
        ui.loginPage();
    }

    public Random getRand() {
        return rand;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public Grid getMap() {
        return map;
    }

    public Character getCharacter() {
        return choosen;
    }

    public Account getAccount() {
        return account;
    }

    public GameUI getUI() {
        return ui;
    }

    public boolean getTest() {
        return test;
    }

    public boolean getOver() {
        return over;
    }

    public boolean getPortal() {
        return portal;
    }

    public boolean getInBattle() {
        return inBattle;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public void setCharacter(Character character) {
        this.choosen = character;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    public void setMap(Grid map) {
        this.map = map;
    }
    
    public void setPortal(boolean portal) {
        this.portal = portal;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public void setTest(boolean test) {
        this.test = test;
    }
}
