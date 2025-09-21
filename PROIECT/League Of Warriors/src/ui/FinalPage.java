package ui;

import javax.swing.*;
import java.awt.*;
import entities.*;
import entities.Character;
import game.Game;
import map.Grid;

public class FinalPage extends JPanel {
    public FinalPage(Character player, boolean portal) {
        super(new GridLayout(2, 1, 10, 10));

        JLabel playerCard = new JLabel();
        JLabel playerProffesion = new JLabel();
        if (player instanceof Warrior) {
            playerCard.setIcon(new ImageIcon(getClass().getResource("/images/warrior.png")));
            playerProffesion.setText("Proffesion: Warrior");
        } else if (player instanceof Mage) {
            playerCard.setIcon(new ImageIcon(getClass().getResource("/images/mage.png")));
            playerProffesion.setText("Proffesion: Mage");
        } else if (player instanceof Rogue) {
            playerCard.setIcon(new ImageIcon(getClass().getResource("/images/rogue.png")));
            playerProffesion.setText("Proffesion: Rogue");
        }
        JLabel playerName = new JLabel("Name: " + player.getName());
        JLabel playerLevel = new JLabel("Level: " + player.getLevel());
        JLabel playerExp = new JLabel("Exp: " + player.getExp());

        add(playerCard);
        JPanel downPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel playerInfo = new JPanel(new GridLayout(4, 1, 10, 10));
        playerInfo.add(playerName);
        playerInfo.add(playerProffesion);
        playerInfo.add(playerLevel);
        playerInfo.add(playerExp);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton exitButton = new JButton("Exit");

        if (portal) {
            JButton nextMapButton = new JButton("Next Map");
            nextMapButton.addActionListener(e -> {
                Game.getInstance().setMap(Grid.generateMap(Game.getInstance().getRand().nextInt(6) + 5, Game.getInstance().getRand().nextInt(6) + 5));
                Game.getInstance().setInBattle(false);
                Game.getInstance().setOver(false);
                Game.getInstance().setPortal(false);
                Game.getInstance().getMap().setCharacter(Game.getInstance().getCharacter());
                Game.getInstance().getUI().getFrame().getContentPane().removeAll();
                Game.getInstance().getUI().renderGame();
            });
            buttonsPanel.add(nextMapButton);
        } else {
            JButton playAgainButton = new JButton("Play Again");
            playAgainButton.addActionListener(e -> {
                if (Game.getInstance().getTest())
                    Game.getInstance().setMap(Grid.generateTestMap());
                else
                    Game.getInstance().setMap(Grid.generateMap(Game.getInstance().getRand().nextInt(6) + 5, Game.getInstance().getRand().nextInt(6) + 5));
                Game.getInstance().setInBattle(false);
                Game.getInstance().setOver(false);
                Game.getInstance().setPortal(false);
                Game.getInstance().getUI().showCharaterSelection();
            });
            buttonsPanel.add(playAgainButton);
        }

        buttonsPanel.add(exitButton);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        add(downPanel);
        downPanel.add(playerInfo);
        downPanel.add(buttonsPanel);
    }
}
