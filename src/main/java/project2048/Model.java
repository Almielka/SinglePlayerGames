package project2048;

import java.util.*;

/**
 * @author Anna S. Almielka
 */

// The class contains the game logic and store the game board
public class Model {

    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score;
    int maxTile;

    private Stack<Tile[][]> previousStates;
    private Stack<Integer> previousScores;
    private boolean isSaveNeeded;

    public Model() {
        previousStates = new Stack<>();
        previousScores = new Stack<>();
        isSaveNeeded = true;
        resetGameTiles();
    }

    void resetGameTiles() {
        score = 0;
        maxTile = 0;
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (Tile[] row : gameTiles) {
            for (int i = 0; i < row.length; i++) {
                row[i] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    // changes the value of a random empty tile in the gameTiles array by 2 or 4 with probability 0.9 and 0.1, respectively
    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            int index = (int) (Math.random() * emptyTiles.size()) % emptyTiles.size();
            emptyTiles.get(index).value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (Tile[] row : gameTiles) {
            for (Tile tile : row) {
                if (tile.value == 0) {
                    emptyTiles.add(tile);
                }
            }
        }
        return emptyTiles;
    }

    public void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }
        boolean isChanged = false;
        for (Tile[] row : gameTiles) {
            if (compressTiles(row) | mergeTiles(row)) {
                isChanged = true;
            }
        }
        if (isChanged) {
            addTile();
        }
        isSaveNeeded = true;
    }

    private Tile[][] rotateClockwise() {
        Tile[][] result = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                result[i][j] = gameTiles[gameTiles.length - 1 - j][i];
            }
        }
        return result;
    }

    public void right() {
        saveState(gameTiles);
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
        left();
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
    }

    public void up() {
        saveState(gameTiles);
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
        left();
        gameTiles = rotateClockwise();
    }

    public void down() {
        saveState(gameTiles);
        gameTiles = rotateClockwise();
        left();
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
        gameTiles = rotateClockwise();
    }


    // Shrink the tiles so that all empty tiles are to the right,
    // i.e. row {4, 2, 0, 4} becomes row {4, 2, 4, 0}
    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        int insertPosition = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (!tiles[i].isEmpty()) {
                if (i != insertPosition) {
                    tiles[insertPosition] = tiles[i];
                    tiles[i] = new Tile();
                    isChanged = true;
                }
                insertPosition++;
            }
        }
        return isChanged;
    }

    // Merging tiles of the same denomination, i.e. row {4, 4, 2, 0} becomes {8, 2, 0, 0}.
    // Note that the row {4, 4, 4, 4} turns into {8, 8, 0, 0},
    // and {4, 4, 4, 0} turns into {8, 4, 0, 0}.
    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        Deque<Tile> deque = new LinkedList<>();
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].isEmpty()) {
                continue;
            }
            if (i < tiles.length - 1 && tiles[i].value == tiles[i + 1].value) {
                int newValue = tiles[i].value * 2;
                if (newValue > maxTile) {
                    maxTile = newValue;
                }
                score += newValue;
                deque.add(new Tile(newValue));
                tiles[i + 1].value = 0;
                isChanged = true;
            } else {
                deque.add(new Tile(tiles[i].value));
            }
            tiles[i].value = 0;
        }

        int i = 0;
        while (!deque.isEmpty()) {
            tiles[i++] = deque.pollFirst();
        }
        return isChanged;
    }

    boolean canMove() {
        if (!getEmptyTiles().isEmpty()) return true;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Tile tile = gameTiles[i][j];
                if ((i < FIELD_WIDTH - 1 && tile.value == gameTiles[i + 1][j].value)
                        || ((j < FIELD_WIDTH - 1) && tile.value == gameTiles[i][j + 1].value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveState(Tile[][] gameTiles) {
        Tile[][] saveGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                saveGameTiles[i][j] = new Tile(gameTiles[i][j].value);
            }
        }
        previousStates.push(saveGameTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!(previousStates.isEmpty() || previousScores.isEmpty())) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0: {
                left();
                break;
            }
            case 1: {
                right();
                break;
            }
            case 2: {
                up();
                break;
            }
            case 3: {
                down();
                break;
            }
        }

    }

    public void autoMove() {
        // there is always a maximum element at the top of the queue
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));
        priorityQueue.peek().getMove().move();
    }

    private boolean hasBoardChanged() {
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles.length; j++) {
                if (gameTiles[i][j].value != previousStates.peek()[i][j].value) {
                    return true;
                }
            }
        }
        return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        move.move();
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        }
        rollback();
        return moveEfficiency;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

}
