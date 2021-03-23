# Single player computer games

Some single player computer games, Java Projects, learning Java

## Tools and Technologies

- Java8, Swing, Java FX with javarush engine

## Minesweeper

[Minesweeper](src/main/java/minesweeper)

We have a game board divided into squares. Some of them contain "mines", but we don't know how many there are or where they are. Our goal is to reveal all of the unmined squares without getting blown up. The player opens the cells, trying not to open the cell with the mine. Having opened a cell with a mine, he loses.

Added:
- The first move will never hit on a bomb (mine)
- Even if the flags are over, added the ability to remove any flag for its further use.
- Bottom Bar with statistic Mines, Flags and Score
- Button exit

Mouse controls:
- Left mouse click for trying to open the cell
- Right mouse click for using flags
- At the end of the game left mouse click to restart
- At the end of the game left mouse click on the Exit button to exit


## 2048

[2048](src/main/java/project2048)

We have a 4x4 board divided into square tiles. A tile with a value of 2 (probability of 90%) or 4 (probability of 10%) appears in each round. All the tiles can be moved to one of the four sides. If two tiles with the same value "collide", they merge, and the tile value doubles. The objective is to get a tile whose value is 2048. The player loses if another move is no longer possible.

Keyboard Controls:
- Up/Down/Left/Right 
- Z to reverse the last move. We can reverse more than one move
- R to random move
- A to efficiently move
- Space to restart
- ESC to exit


## Hungry Snake

[Snake](src/main/java/snake)

The player controls a long, thin creature resembling a snake, which crawls along a plane bounded by walls, collecting apples, avoiding collisions with its own tail and the edges of the playing field. Each time the snake eats an apple, it gets longer and move faster, which gradually complicates the game. The player controls the direction of movement of the snake's head (4 directions: up, down, left, right), and the snake's tail follows. The player cannot stop the movement of the snake.
