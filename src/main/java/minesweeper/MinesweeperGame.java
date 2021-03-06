package minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anna S. Almielka
 */

public class MinesweeperGame extends Game {

    private static final int SIDE = 12;
    private static final int SCORE_POINT = 5;
    private static final int MINE_POSITION = 0;
    private static final int MINE_COUNT_POSITION = 1;
    private static final int FLAG_POSITION = 4;
    private static final int FLAG_COUNT_POSITION = 5;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";

    private final GameObject[][] gameField = new GameObject[SIDE + 1][SIDE];
    private int countClosedTiles = SIDE * SIDE;
    private int countMinesOnField;
    private int countFlags;
    private int score;
    private boolean isGameStarted;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE + 1);
        createGame();
        createBottomBar();
    }

    private void createGame() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                gameField[y][x] = new GameObject(x, y, false);
                setCellValue(x, y, "");
                setCellColor(x, y, Color.GREY);
            }
        }
    }

    private void createBottomBar() {
        int y = SIDE;
        for (int x = 0; x < SIDE; x++) {
            gameField[y][x] = new GameObject(x, y);
            setCellColor(x, y, Color.WHITE);
        }
        setCellValueEx(MINE_POSITION, y, Color.ORANGE, MINE);
        setCellValueEx(FLAG_POSITION, y, Color.ORANGE, FLAG);
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped) {
            openTile(x, y);
        } else {
            restart();
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void openTile(int x, int y) {
        if (isInGameField(x, y)) {
            return;
        }
        GameObject gameObject = gameField[y][x];
        if (isGameStopped || gameObject.isOpen || gameObject.isFlag) {
            return;
        }
        if (!isGameStarted) {
            createMines(x, y);
            isGameStarted = true;
        }
        if (gameObject.isMine) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            gameObject.isOpen = true;
            countClosedTiles--;
            setCellColor(x, y, Color.MEDIUMSEAGREEN);
            score += SCORE_POINT;
            setScore(score);
            checkMineNeighbors(x, y);
            if (countClosedTiles == countMinesOnField || countClosedTiles == 0) {
                win();
            }
        }
    }

    private boolean isInGameField(int x, int y) {
        return x < 0 || x >= SIDE || y < 0 || y >= SIDE;
    }

    private void createMines(int currentX, int currentY) {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (getRandomNumber(10) < 1 && !gameField[y][x].equals(gameField[currentY][currentX])) {
                    gameField[y][x].isMine = true;
                    countMinesOnField++;
                }
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
        setCellValue(MINE_COUNT_POSITION, SIDE, "" + countMinesOnField);
        refreshCountFlags();
    }

    private void refreshCountFlags() {
        setCellValue(FLAG_COUNT_POSITION, SIDE, "" + countFlags);
    }

    private void countMineNeighbors() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                GameObject currentGameObject = gameField[y][x];
                if (!currentGameObject.isMine) {
                    for (GameObject neighbor : getNeighbors(currentGameObject)) {
                        if (neighbor.isMine) {
                            currentGameObject.countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private void checkMineNeighbors(int x, int y) {
        if (gameField[y][x].countMineNeighbors == 0) {
            setCellValue(x, y, "");
            for (GameObject neighbor : getNeighbors(gameField[y][x])) {
                if (!neighbor.isOpen) {
                    openTile(neighbor.x, neighbor.y);
                }
            }
        } else if (gameField[y][x].countMineNeighbors != 0) {
            setCellNumber(x, y, gameField[y][x].countMineNeighbors);
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> listNeighbors = new ArrayList<>();
        for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
            for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
                if (isInGameField(x, y)) continue;
                if (!gameField[y][x].equals(gameObject)) {
                    listNeighbors.add(gameField[y][x]);
                }
            }
        }
        return listNeighbors;
    }

    private void markTile(int x, int y) {
        if (isInGameField(x, y)) {
            return;
        }
        GameObject gameObject = gameField[y][x];
        if (isGameStopped || gameObject.isOpen) {
            return;
        }
        if (!gameObject.isFlag && countFlags != 0) {
            gameObject.isFlag = true;
            countFlags--;
            refreshCountFlags();
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.ORANGE);
        } else if (gameObject.isFlag) {
            gameObject.isFlag = false;
            countFlags++;
            refreshCountFlags();
            setCellValue(x, y, "");
            setCellColor(x, y, Color.GREY);
        }

    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.LIGHTGREY, "GAME OVER", Color.RED, 44);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.LIGHTGREY, "Awesome. You win!", Color.DARKGREEN, 44);
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

}