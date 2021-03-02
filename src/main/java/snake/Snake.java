package snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Anna S. Almielka
 */

public class Snake {

    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    public void draw(Game game) {
        IntStream.range(0, snakeParts.size())
                .forEach(i -> game.setCellValueEx(
                        snakeParts.get(i).x,
                        snakeParts.get(i).y,
                        Color.NONE,
                        (i == 0) ? HEAD_SIGN : BODY_SIGN,
                        isAlive ? Color.BLACK : Color.RED,
                        75));
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (newHead.x < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y < 0 || newHead.y >= SnakeGame.HEIGHT) {
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        if (newHead.x == apple.x && newHead.y == apple.y) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    public GameObject createNewHead() {
        GameObject head = snakeParts.get(0);
        if (direction.equals(Direction.UP)) return new GameObject(head.x, head.y - 1);
        else if (direction.equals(Direction.DOWN)) return new GameObject(head.x, head.y + 1);
        else if (direction.equals(Direction.LEFT)) return new GameObject(head.x - 1, head.y);
        else return new GameObject(head.x + 1, head.y);
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        return snakeParts.stream().anyMatch(part -> part.x == gameObject.x && part.y == gameObject.y);
    }

    public int getLength() {
        return snakeParts.size();
    }

    public void setDirection(Direction direction) {
        if (((this.direction == Direction.UP || this.direction == Direction.DOWN) && snakeParts.get(0).y == snakeParts.get(1).y)
                || ((this.direction == Direction.LEFT || this.direction == Direction.RIGHT) && snakeParts.get(0).x == snakeParts.get(1).x))
            return;
        if ((this.direction == Direction.UP && direction != Direction.DOWN)
                || (this.direction == Direction.DOWN && direction != Direction.UP)
                || (this.direction == Direction.LEFT && direction != Direction.RIGHT)
                || (this.direction == Direction.RIGHT && direction != Direction.LEFT)) {
            this.direction = direction;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

}