/**
 * Created by williamjohnston on 3/2/17.
 */
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;



public class SudokuCreator {
    SudokuBoard board;

    public SudokuCreator() {
        this.board = new SudokuBoard();
    }
    public void createRandomBoard() {
       for (int i = 0; i < 9; i++) {
           for (int j = 0; j < 9; j++) {
               SudokuSolver solver;
               do {
                   int random = ThreadLocalRandom.current().nextInt(1, 10);
                   board.updateCell(i, j, random);
                   solver = new SudokuSolver(board);

               } while (!solver.hasSolution());
           }
       }
    }
    public void minimizeToUnique() {
        LinkedList<Integer> cells = new LinkedList<>();
        for (int i = 0; i < 81; i++) {
            cells.add(i);
        }
        Collections.shuffle(cells);
      // int numberToRemove = ThreadLocalRandom.current().nextInt()

    }


    public boolean isValid(int value, int x_coord, int y_coord) {
        int boxCol = (x_coord/3);
        int boxRow = (y_coord/3);

        //check cols
        for (int x = 0; x < 9; x++) {
            if (x == x_coord) {
                continue;
            } else {
                if (value == board.getCell(x, y_coord)) {
                    return false;
                }
            }
        }

        //check rows
        for (int y = 0; y < 9; y++) {
            if (y == y_coord) {
                continue;
            } else {
                if (value == board.getCell(x_coord,y)) {
                    return false;
                }
            }
        }

        //check remaining neighbors in box
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!(i == x_coord%3 || j == y_coord%3)) {
                    if (value == board.getCell(3 * boxCol + i, 3 * boxRow + j)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SudokuCreator creator = new SudokuCreator();
        creator.createRandomBoard();
        creator.board.printBoard();
    }
}
