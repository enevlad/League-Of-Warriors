import game.Game;

import java.util.Scanner;

// Main function that runs the main game with the first map randomized
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Select mode:");
        System.out.println("1. Terminal mode");
        System.out.println("2. Graphical mode");

        int mode = input.nextInt();
        switch (mode) {
            case 1:
                Game.getInstance().run();
                break;
            case 2:
                Game.getInstance().runGraphical();
                break;
            default:
                System.out.println("Invalid mode. Try again!");
                break;
        }
//        Game.getInstance().run();
    }
}