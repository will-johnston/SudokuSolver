import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by williamjohnston on 3/2/17.
 * Given a string of 81 numbers, the solver will find a solution to the sudoku puzzle
 */


public class SudokuSolver {
    LinkedList<Integer> cc;  //connected empty cell components in board
    LinkedList<Integer>[] adj;  //adjacency list
    int[][] board;  //board represented as 2d array

    public SudokuSolver(SudokuBoard sb) {
        AdjListConComps al = new AdjListConComps(sb);
        cc = al.cc;
        adj = al.adj;
        board = sb.getBoard();
    }


    //when called, iterates through connected components to find valid sequence for each
    public void solve() {
        for (int c : cc) {
            solve(c);
        }
    }

    //recursive backtracking algorithm to find valid solution
    public boolean solve(int c) {


        //go through possible values for cell c
        for (int i = 1; i < 10; i++) {
            if (isValid(i, c)) {
                board[c/9][c%9] = i;
                boolean flag = true;

                //find solution for each adjacent cell
                for (int a : adj[c]) {
                    if (board[a/9][a%9] != 0) {
                        continue;
                    }
                    flag = solve(a);
                    if (!flag) {
                        board[c/9][c%9] = 0;
                        break;
                    }
                }
                if (flag) {
                    return true;
                }
            }
        }

        //no possible value for cell, set to 0 and backtrack
        board[c/9][c%9] = 0;
        return false;

    }



    public boolean isSolved() {
        //check to make sure each row, col, and square adds to 81.
        for (int i = 0; i < 9; i++) {
            int rowSum = 0;
            int colSum = 0;
            int squareSum = 0;
            for (int j = 0; j < 9; j++) {
                rowSum += board[i][j];
                colSum += board[j][i];
                squareSum += board[3*(i/3) + (j/3)][(3*(i % 3) + (j % 3))];  //square assoc. with i index,
                                                                             //index in square assoc. with j
            }
            if (rowSum != 45 || colSum != 45 || squareSum != 45) {
                return false;
            }
        }
        return true;
    }

    public boolean hasSolution() {
        solve();
        return isSolved();
    }

    //checks to see if value exists in a cell that is in the same box, row, or column
    public boolean isValid(int value, int index) {
        int y_coord = index % 9;
        int x_coord = index / 9;
        int boxCol = (x_coord/3);
        int boxRow = (y_coord/3);

        //check cols
        for (int x = 0; x < 9; x++) {
            if (x == x_coord) {
                continue;
            } else {
                if (value == board[x][y_coord]) {
                    return false;
                }
            }
        }

        //check rows
        for (int y = 0; y < 9; y++) {
            if (y == y_coord) {
                continue;
            } else {
                if (value == board[x_coord][y]) {
                    return false;
                }
            }
        }

        //check remaining neighbors in box
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!(i == x_coord%3 || j == y_coord%3)) {
                    if (value == board[3 * boxCol + i][3 * boxRow + j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    public String getBoard() {
        if (!hasSolution()) {
            return null;
        }
        String solution = "";
        for (int i = 0; i < 81; i++) {
            solution += this.board[i/9][i%9];
        }
        return solution;
    }

    public static void main(String[] args) {

        File input = new File("/Users/williamjohnston/IdeaProjects/SudokuSolver/src/solved1.txt");
        if (input == null) {
            System.out.println("no input");
            return;
        }

        SudokuBoard sb = new SudokuBoard(input);
        SudokuSolver ss = new SudokuSolver(sb);

        sb.printBoard();

        ss.solve();
        if (ss.isSolved()) {
            System.out.println("Solved!");
        } else {
            System.out.println("Not solved!");
        }

        sb.printBoard();
    }
}
