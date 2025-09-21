package map;

public class Cell {
    private int x, y;
    private CellEntityType type;
    private boolean visited;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        visited = false;
        type = CellEntityType.VOID;
    }

    public CellEntityType getType() {
        return type;
    }

    public void setType(CellEntityType type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
