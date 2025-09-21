package ui;

import entities.Enemy;

import javax.swing.*;
import java.awt.*;
import game.Game;
import map.*;
import exceptions.*;

public class GameUI {
    private JFrame frame;
    private Game game;
    private JPanel mapPanel;
    private JPanel characterInfoPanel;
    private JPanel movementPanel;

    public GameUI() {
        this.frame = new JFrame("League of Warriors");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.game = Game.getInstance();
        if (this.game.getTest())
            game.setMap(Grid.generateTestMap());
        else
            game.setMap(Grid.generateMap(this.game.getRand().nextInt(6) + 5, this.game.getRand().nextInt(6) + 5));
        this.mapPanel = new JPanel(new GridLayout(game.getMap().getLength(), game.getMap().getWidth()));
        this.characterInfoPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        this.movementPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    }

    public void loginPage() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(800, 600));

        // elements in the login page
        JPanel loginPanel = new JPanel(new GridLayout(5, 1));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // adding the elements to the panel
        loginPanel.add(emailLabel);
        loginPanel.add(emailField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        frame.add(loginPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            game.setAccount(game.checkEmail(email));
            if (game.getAccount()!= null) {
                if (game.checkPassword(password, game.getAccount())) {
                    showCharaterSelection();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid password. Try again!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Account email not found. Try again!");
            }
        });
    }

    public void showCharaterSelection() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Select your character:");
        panel.add(label);

        // making a list of chracters available on the account
        DefaultListModel<entities.Character> model = new DefaultListModel<>();
        for (entities.Character character : game.getAccount().getCharacters()) {
            model.addElement(character);
        }
        JList<entities.Character> characterList = new JList<>(model);

        panel.add(new JScrollPane(characterList));
        JButton select = new JButton("Select");
        panel.add(select);
        frame.add(panel, BorderLayout.CENTER);

        select.addActionListener(e -> {
            game.setCharacter(characterList.getSelectedValue());
            if (game.getCharacter() != null) {
                //start game with the choosen character
                game.getCharacter().setHealth(game.getCharacter().getMaxHealth());
                game.getCharacter().setMana(game.getCharacter().getMaxMana());
                game.getMap().setCharacter(game.getCharacter());
                renderGame();
            } else {
                JOptionPane.showMessageDialog(frame, "No character selected. Try again!");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private void updateMovementPanel() {
        movementPanel.removeAll();
        JButton north = new JButton("North");
        JButton east = new JButton("East");
        JButton south = new JButton("South");
        JButton west = new JButton("West");

        movementPanel.add(north);
        movementPanel.add(east);
        movementPanel.add(south);
        movementPanel.add(west);

        north.setBackground(Color.BLACK);
        east.setBackground(Color.BLACK);
        west.setBackground(Color.BLACK);
        south.setBackground(Color.BLACK);

        north.setForeground(Color.WHITE);
        east.setForeground(Color.WHITE);
        west.setForeground(Color.WHITE);
        south.setForeground(Color.WHITE);

        north.addActionListener(e -> move("north"));
        east.addActionListener(e -> move("east"));
        west.addActionListener(e -> move("west"));
        south.addActionListener(e -> move("south"));
    }

    public void move(String direction) {
        switch (direction) {
            case "north":
                try {
                    game.getMap().goNorth();
                } catch (ImpossibleMove e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
                break;
            case "east":
                try {
                    game.getMap().goEast();
                } catch (ImpossibleMove e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
                break;
            case "west":
                try {
                    game.getMap().goWest();
                } catch (ImpossibleMove e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
                break;
            case "south":
                try {
                    game.getMap().goSouth();
                } catch (ImpossibleMove e) {
                    JOptionPane.showMessageDialog(frame, e.getMessage());
                }
                break;
            default:
                break;
        }
        if (game.getMap().getCopy() == CellEntityType.SANCTUARY &&
                !game.getMap().get(game.getMap().getCurrent().getX()).get(game.getMap().getCurrent().getY()).isVisited()) {
            game.handleSanctuary();
        } else if (game.getMap().getCopy() == CellEntityType.PORTAL &&
                !game.getMap().get(game.getMap().getCurrent().getX()).get(game.getMap().getCurrent().getY()).isVisited()) {
            game.setPortal(true);
            game.handlePortal();
        } else if (game.getMap().getCopy() == CellEntityType.ENEMY &&
                !game.getMap().get(game.getMap().getCurrent().getX()).get(game.getMap().getCurrent().getY()).isVisited()) {
            // TODO: something to show the player and enemy and handle combat with ui
            game.setInBattle(true);
        }
        game.getMap().get(game.getMap().getCurrent().getX()).get(game.getMap().getCurrent().getY()).setVisited(true);
        renderGame();
    }

    public void updateCharacterInfo() {
        characterInfoPanel.removeAll();
        characterInfoPanel.add(new JLabel("Name: " + game.getCharacter().getName()));
        characterInfoPanel.add(new JLabel("Experience: " + game.getCharacter().getExp()));
        characterInfoPanel.add(new JLabel("Level: " + game.getCharacter().getLevel()));
        characterInfoPanel.add(new JLabel("Health: " + game.getCharacter() .getHealth()));
        characterInfoPanel.add(new JLabel("Mana: " + game.getCharacter().getMana()));
    }

    public void updateMapPanel(Grid map) {
        mapPanel.removeAll();
        mapPanel.setLayout(new GridLayout(map.getLength(), map.getWidth()));
        for (int i = 0; i < map.getLength(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(50, 50));

                ImageIcon icon;
                Image img;

                if (i == map.getCurrent().getX() && j == map.getCurrent().getY()) {
                    icon = new ImageIcon(getClass().getResource("/images/player.jpg"));
                    img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(img));
                } else if (map.get(i).get(j).getType() == CellEntityType.SANCTUARY && map.get(i).get(j).isVisited()) {
                    icon = new ImageIcon(getClass().getResource("/images/sanctuary.png"));
                    img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(img));
                } else if (map.get(i).get(j).getType() == CellEntityType.ENEMY && map.get(i).get(j).isVisited()) {
                    icon = new ImageIcon(getClass().getResource("/images/enemy.png"));
                    img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(img));
                } else if(map.get(i).get(j).getType() == CellEntityType.VOID && map.get(i).get(j).isVisited()) {
                    icon = new ImageIcon(getClass().getResource("/images/void.png"));
                    img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(img));
                } else {
                    icon = new ImageIcon(getClass().getResource("/images/question.png"));
                    img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(img));
                }
                mapPanel.add(cell);
            }
        }
    }

    public void renderGame() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        if (game.getOver()) {
            frame.add(new FinalPage(game.getCharacter(), false), BorderLayout.CENTER);
        } else if (game.getPortal()) {
            frame.add(new FinalPage(game.getCharacter(), true), BorderLayout.CENTER);
        } else if (game.getInBattle()) {
            game.setEnemy(new Enemy());
            game.getEnemy().generateSpells();
            game.getMap().getCharacter().generateSpells();
            BattlePage battle = new BattlePage(game.getCharacter(), game.getEnemy());
            //System.out.println("Switching to battle page");
            frame.add(battle, BorderLayout.CENTER);
        } else {
            updateMapPanel(game.getMap());
            updateCharacterInfo();
            updateMovementPanel();

            frame.add(movementPanel, BorderLayout.WEST);
            frame.add(characterInfoPanel, BorderLayout.EAST);
            frame.add(mapPanel, BorderLayout.CENTER);
        }
        frame.pack();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
