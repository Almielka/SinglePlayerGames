package project2048;

import java.awt.*;

/**
 * @author Anna S. Almielka
 */

// Class describing one tile
public class Tile {

    int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
    }

    public boolean isEmpty() {
        return value == 0;
    }

    Color getFontColor() {
        return (value < 16) ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    Color getTileColor() {
        switch (value) {
            case 0:
                return new Color(0xcdc1b4);
            case 2:
                return new Color(0xeee4da);
            case 4:
                return new Color(0xede0c8);
            case 8:
                return new Color(0xf2b179);
            case 16:
                return new Color(0xf59563);
            case 32:
                return new Color(0xf67c5f);
            case 64:
                return new Color(0xf65e3b);
            case 128:
                return new Color(0xF1D98E);
            case 256:
                return new Color(0xedc850);
            case 512:
                return new Color(0xedc22e);
            case 1024:
                return new Color(0xB19122);
            case 2048:
                return new Color(0x748E1B);
            default:
                return new Color(0xff0000);
        }
    }

}
