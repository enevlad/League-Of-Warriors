package ui;

import entities.*;
import entities.Character;
import spells.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import game.Game;

public class BattlePage extends JPanel {
    private JLabel playerLabel, enemyLabel;
    private JLabel playerHealthLabel, playerManaLabel;
    private JLabel enemyHealthLabel, enemyManaLabel;
    private JButton attackButton, spellButton;

    public BattlePage(Entity player, Entity enemy) {
        super(new BorderLayout());

        JPanel playerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JPanel enemyPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        playerLabel = new JLabel();
        if (player instanceof Warrior) {
            playerLabel.setIcon(new ImageIcon(getClass().getResource("/images/warrior.png")));
        } else if (player instanceof Mage) {
            playerLabel.setIcon(new ImageIcon(getClass().getResource("/images/mage.png")));
        } else if (player instanceof Rogue) {
            playerLabel.setIcon(new ImageIcon(getClass().getResource("/images/rogue.png")));
        }
        playerHealthLabel = new JLabel("Health: " + Game.getInstance().getCharacter().getHealth() + "/" + Game.getInstance().getCharacter().getMaxHealth());
        playerManaLabel = new JLabel("Mana: " + Game.getInstance().getCharacter().getMana() + "/" + Game.getInstance().getCharacter().getMaxMana());
        playerPanel.add(playerLabel);
        playerPanel.add(playerHealthLabel);
        playerPanel.add(playerManaLabel);

        Random rand = new Random();
        int random = rand.nextInt(3) + 1;
        enemyLabel = new JLabel();
        enemyLabel.setIcon(new ImageIcon(getClass().getResource("/images/enemy" + random + ".png")));
        enemyHealthLabel = new JLabel("Health: " + enemy.getHealth() + "/" + enemy.getMaxHealth());
        enemyManaLabel = new JLabel("Mana: " + enemy.getMana() + "/" + enemy.getMaxMana());
        enemyPanel.add(enemyLabel);
        enemyPanel.add(enemyHealthLabel);
        enemyPanel.add(enemyManaLabel);

        attackButton = new JButton("Attack");
        spellButton = new JButton("Spells");

        buttonsPanel.add(attackButton);
        buttonsPanel.add(spellButton);

        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int damage = player.getDamage();
                enemy.reciveDamage(damage);
                if (((Enemy) enemy).getEvade())
                    System.out.println("Enemy evaded your attack");
                else if(((Character) player).getCritical())
                    System.out.println("You landed a critical! Damage dealt " + (damage * 2) + ".");
                else
                    System.out.println("Damage dealt " + damage + ".");
                checkStateEnemy(enemy);
                if (Game.getInstance().getInBattle()) {
                    System.out.println("Enemy turn");
                    Game.getInstance().enemyAttack();
                    updateCharacterInfo();
                    updateEnemyInfo();
                    checkStatePlayer(player);
                }
            }
        });

        spellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame spellFrame = new JFrame("Spells");
                spellFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel allSpellsPanel = new JPanel(new GridLayout(1, player.getSpells().size(), 10, 10));
                for (Spell spell : player.getSpells()) {
                    JPanel spellPanel = new JPanel(new GridLayout(4, 1, 10, 10));
                    JLabel spellLabel = new JLabel();
                    if (spell instanceof Fire)
                        spellLabel.setIcon(new ImageIcon(getClass().getResource("/images/fire.png")));
                    else if (spell instanceof Ice)
                        spellLabel.setIcon(new ImageIcon(getClass().getResource("/images/ice.png")));
                    else if (spell instanceof Earth)
                        spellLabel.setIcon(new ImageIcon(getClass().getResource("/images/earth.png")));
                    JLabel spellDamageLabel = new JLabel("Damage: " + spell.getDamage());
                    JLabel spellManaLabel = new JLabel("Mana: " + spell.getCost());
                    JButton useButton = new JButton("Use");
                    useButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            player.useSpell(spell, enemy);
                            checkStateEnemy(enemy);
                            if(Game.getInstance().getInBattle()) {
                                System.out.println("Enemy turn");
                                Game.getInstance().enemyAttack();
                                updateCharacterInfo();
                                updateEnemyInfo();
                                checkStatePlayer(player);
                            }
                            spellFrame.dispose();
                        }
                    });
                    spellPanel.add(spellLabel);
                    spellPanel.add(spellDamageLabel);
                    spellPanel.add(spellManaLabel);
                    spellPanel.add(useButton);
                    allSpellsPanel.add(spellPanel);
                }
                spellFrame.add(allSpellsPanel, BorderLayout.CENTER);
                spellFrame.pack();
                spellFrame.setVisible(true);
            }
        });

        add(playerPanel, BorderLayout.WEST);
        add(buttonsPanel, BorderLayout.CENTER);
        add(enemyPanel, BorderLayout.EAST);
    }

    public void updateCharacterInfo() {
        playerHealthLabel.setText("Health: " + Game.getInstance().getCharacter().getHealth() + "/" + Game.getInstance().getCharacter().getMaxHealth());
        playerManaLabel.setText("Mana: " + Game.getInstance().getCharacter().getMana() + "/" + Game.getInstance().getCharacter().getMaxMana());
    }

    public void updateEnemyInfo() {
        enemyHealthLabel.setText("Health: " + Game.getInstance().getEnemy().getHealth() + "/" + Game.getInstance().getEnemy().getMaxHealth());
        enemyManaLabel.setText("Mana: " + Game.getInstance().getEnemy().getMana() + "/" + Game.getInstance().getEnemy().getMaxMana());
    }

    public void checkStateEnemy(Entity enemy) {
        if (enemy.getHealth() <= 0) {
            System.out.println("You won!");
            Game.getInstance().setInBattle(false);
            Game.getInstance().fightEnd();
            Game.getInstance().getUI().renderGame();
        }
    }

    public void checkStatePlayer(Entity player) {
        if (player.getHealth() <= 0) {
            System.out.println("You died!");
            JOptionPane.showMessageDialog(null, "You died!");
            Game.getInstance().setInBattle(false);
            Game.getInstance().setOver(true);
            Game.getInstance().getUI().renderGame();
        }

    }
}
