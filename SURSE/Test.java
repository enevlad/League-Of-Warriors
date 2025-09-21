import game.Game;

import java.util.Scanner;

// Test class that runs the game with the first map hardcoded
public class Test {
    public static void main(String[] args) {
        Game.getInstance().setTest(true);
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
        //1Game.getInstance().run();
    }
}
