package snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

/**
 * @author Anna S. Almielka
 */

public class Apple extends GameObject {

    private static final String APPLE_SIGN = "\uD83C\uDF4E";
    public boolean isAlive = true;

    public Apple(int x, int y) {
        super(x, y);
    }

    public void draw(Game game) {
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.OLIVE, 75);
    }

    public boolean isAlive() {
        return isAlive;
    }

}