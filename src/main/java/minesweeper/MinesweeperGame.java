package minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;

/**
 * @author Anna S. Almielka
 */

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private final GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStarted;
    private boolean isGameStopped;
    private int countClosedTiles = SIDE * SIDE;
    private int score;

    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField[x].length; y++) {
                gameField[y][x] = new GameObject(x, y, false);
                setCellValue(x, y, "");
                setCellColor(x, y, Color.GREY);
            }
        }
    }

    private void createMines(int currentX, int currentY) {
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField[x].length; y++) {
                if (getRandomNumber(10) < 1 && !gameField[y][x].equals(gameField[currentY][currentX])) {
                    gameField[y][x].isMine = true;
                    countMinesOnField++;
                }
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void countMineNeighbors() {
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField[x].length; y++) {
                GameObject currentGameObject = gameField[y][x];
                if (!currentGameObject.isMine) {
                    ArrayList<GameObject> listNeighbors = getNeighbors(currentGameObject);
                    for (GameObject neighbor : listNeighbors) {
                        if (neighbor.isMine) currentGameObject.countMineNeighbors++;
                    }
                }
            }
        }
    }

    private ArrayList<GameObject> getNeighbors(GameObject gameObject) {
        ArrayList<GameObject> listNeighbors = new ArrayList<>();
        for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
            for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
                try {
                    if (!gameField[y][x].equals(gameObject)) {
                        listNeighbors.add(gameField[y][x]);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        return listNeighbors;
    }


    private void openTile(int x, int y) {
        if (!isGameStopped && !gameField[y][x].isOpen && !gameField[y][x].isFlag) {
            if (!isGameStarted) {
                createMines(x, y);
                isGameStarted = true;
            }
            if (gameField[y][x].isMine) {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else {
                gameField[y][x].isOpen = true;
                countClosedTiles--;
                setCellColor(x, y, Color.MEDIUMSEAGREEN);
                score += 5;
                setScore(score);
                if (gameField[y][x].countMineNeighbors == 0) {
                    setCellValue(x, y, "");
                    ArrayList<GameObject> listNeighbors = getNeighbors(gameField[y][x]);
                    for (GameObject neighbor : listNeighbors) {
                        if (!neighbor.isOpen) openTile(neighbor.x, neighbor.y);
                    }
                } else if (gameField[y][x].countMineNeighbors != 0) {
                    setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                }
                if (countClosedTiles == countMinesOnField || countClosedTiles == 0) win();
            }
        }
    }

    private void markTile(int x, int y) {
        if (!isGameStopped && !gameField[y][x].isOpen) {
            if (!gameField[y][x].isFlag && countFlags != 0) {
                gameField[y][x].isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.ORANGE);
            } else if (gameField[y][x].isFlag) {
                gameField[y][x].isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.GREY);
            }
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.LIGHTGREY, "GAME OVER", Color.RED, 44);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.LIGHTGREY, "Awesome. You win!", Color.GREEN, 44);
    }

    private void restart() {
        isGameStarted = false;
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        setScore(score);
        countMinesOnField = 0;
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped) openTile(x, y);
        else restart();
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

}