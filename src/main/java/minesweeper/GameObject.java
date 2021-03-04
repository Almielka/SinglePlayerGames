package minesweeper;

/**
 * @author Anna S. Almielka
 */

public class GameObject {

    public int x;
    public int y;
    public boolean isMine;
    public int countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;

    public GameObject(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

}