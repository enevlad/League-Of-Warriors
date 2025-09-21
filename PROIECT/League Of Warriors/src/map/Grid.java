package map;

import entities.Character;
import exceptions.*;
import java.util.*;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private int length, width;
    private Character character;
    private Cell current;
    private CellEntityType copy;

    private Grid(int length, int width) {
        this.length = length;
        this.width = width;
        character = null;
        current = null;
        copy = CellEntityType.VOID;
    }

    // generates the randomized map
    public static Grid generateMap(int length, int width) {
        Grid map = new Grid(length, width);
        int sanctuaries = 0;
        int enemies = 0;
        for (int i = 0; i < length; i++) {
            ArrayList<Cell> cells = new ArrayList<Cell>();
            for (int j = 0; j < width; j++) {
                Cell cell = new Cell(i, j);
                cells.add(cell);
            }
            map.add(cells);
        }

        Random rand = new Random();
        while (sanctuaries < 2 || enemies < 4) {
            int x = rand.nextInt(length);
            int y = rand.nextInt(width);
            int choice = rand.nextInt(2);
            Cell cell = map.get(x).get(y);

            if (cell.getType() == CellEntityType.VOID) {
                if (choice == 0) {
                    cell.setType(CellEntityType.SANCTUARY);
                    sanctuaries++;
                }
                else if (choice == 1) {
                    cell.setType(CellEntityType.ENEMY);
                    enemies++;
                }
            }
        }

        boolean no_portal = true;
        while (no_portal) {
            int portalX = rand.nextInt(length);
            int portalY = rand.nextInt(width);
            if (map.get(portalX).get(portalY).getType() == CellEntityType.VOID) {
                map.get(portalX).get(portalY).setType(CellEntityType.PORTAL);
                no_portal = false;
            }
        }

        int startX = rand.nextInt(length);
        int startY = rand.nextInt(width);
        map.current = map.get(startX).get(startY);
        map.current.setType(CellEntityType.PLAYER);
        map.current.setX(startX);
        map.current.setY(startY);
        map.current.setVisited(true);
        return map;
    }

    // generates the hardcoded map for testing
    public static Grid generateTestMap() {
        Grid map = new Grid(5, 5);
        for (int i = 0; i < 5; i++) {
            ArrayList<Cell> cells = new ArrayList<Cell>();
            for (int j = 0; j < 5; j++) {
                Cell cell = new Cell(i, j);
                cells.add(cell);
            }
            map.add(cells);
        }
        map.current = map.get(0).get(0);
        map.current.setType(CellEntityType.PLAYER);
        map.current.setX(0);
        map.current.setY(0);
        map.current.setVisited(true);
        map.get(0).get(3).setType(CellEntityType.SANCTUARY);
        map.get(1).get(3).setType(CellEntityType.SANCTUARY);
        map.get(2).get(0).setType(CellEntityType.SANCTUARY);
        map.get(3).get(4).setType(CellEntityType.ENEMY);
        map.get(4).get(3).setType(CellEntityType.SANCTUARY);
        map.get(4).get(4).setType(CellEntityType.PORTAL);
        return map;
    }

    public void showMap() {
        for (int i = 0 ; i < this.length; i++) {
            for (int j = 0 ; j < this.width; j++) {
                Cell cell = this.get(i).get(j);
                if (cell.isVisited()) {
                    switch (cell.getType()) {
                        case SANCTUARY:
                            System.out.print("S ");
                            break;
                        case ENEMY:
                            System.out.print("E ");
                            break;
                        case PORTAL:
                            System.out.print("F ");
                            break;
                        case PLAYER:
                            System.out.print("P ");
                            break;
                        case VOID:
                            System.out.print("V ");
                            break;
                    }
                } else {
                    System.out.print("N ");
                }
            }
            System.out.println();
        }
    }

    public Cell getCurrent() {
        return current;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void goNorth() throws ImpossibleMove {
        if (current.getX() - 1  < 0)
            throw new ImpossibleMove("Out of bounds");
        get(current.getX()).get(current.getY()).setType(copy);
        copy = get(current.getX() - 1).get(current.getY()).getType();
        current.setX(current.getX() - 1);
        get(current.getX()).get(current.getY()).setType(CellEntityType.PLAYER);
    }

    public void goSouth() throws ImpossibleMove {
        if (current.getX() + 1 >= length)
                throw new ImpossibleMove("Out of bounds");
        get(current.getX()).get(current.getY()).setType(copy);
        copy = get(current.getX() + 1).get(current.getY()).getType();
        current.setX(current.getX() + 1);
        get(current.getX()).get(current.getY()).setType(CellEntityType.PLAYER);
    }

    public void goEast() throws ImpossibleMove {
        if (current.getY() + 1 >= width)
            throw new ImpossibleMove("Out of bounds");
        get(current.getX()).get(current.getY()).setType(copy);
        copy = get(current.getX()).get(current.getY() + 1).getType();
        current.setY(current.getY() + 1);
        get(current.getX()).get(current.getY()).setType(CellEntityType.PLAYER);
    }

    public void goWest() throws ImpossibleMove {
        if (current.getY() - 1 < 0)
            throw new ImpossibleMove("Out of bounds");
        get(current.getX()).get(current.getY()).setType(copy);
        copy = get(current.getX()).get(current.getY() - 1).getType();
        current.setY(current.getY() - 1);
        get(current.getX()).get(current.getY()).setType(CellEntityType.PLAYER);

    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public CellEntityType getCopy() {
        return copy;
    }
}
