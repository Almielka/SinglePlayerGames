package snake;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

/**
 * @author Anna S. Almielka
 */

public class SnakeGame extends Game {

    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    private static final int GOAL = 15;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        isGameStopped = false;
        score = 0;
        setScore(score);
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.BISQUE, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        do {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        } while (snake.checkCollision(apple));
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "GAME OVER!", Color.RED, 50);

    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "Awesome. You win!", Color.GREEN, 45);

    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            createNewApple();
            score += 5;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.UP) snake.setDirection(Direction.UP);
        else if (key == Key.DOWN) snake.setDirection(Direction.DOWN);
        else if (key == Key.LEFT) snake.setDirection(Direction.LEFT);
        else if (key == Key.RIGHT) snake.setDirection(Direction.RIGHT);
        if (key == Key.SPACE && isGameStopped) createGame();
    }

}
