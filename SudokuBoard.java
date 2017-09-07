
import java.io.*;
import java.util.LinkedList;

/**
 * Created by williamjohnston on 3/2/17.
 */


public class SudokuBoard {
    private int board[][];
    //private SudokuCell[] cells = new SudokuCell[81];


    public SudokuBoard() {
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
                //cells[i + 3*j] = new SudokuCell(i, j, 0);
            }
        }
    }

    public SudokuBoard(File input) {
        this.board = new int[9][9];
        parseInputFile(input);
    }

    public SudokuBoard(String input) {
        this.board = new int[9][9];
        for (int i = 0; i < 81; i++)  {
           board[i/9][i%9] = input.charAt(i) - 48;
        }
    }

    public int[][] getBoard() {
        return board;
    }

    //create board given input file
    private void parseInputFile(File input) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(input));
            String line = br.readLine();
            for (int i = 0; i < 81; i++) {
                board[i/9][i%9] = Character.getNumericValue(line.charAt(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //change value in cell
    public void updateCell(int x, int y, int value) {
        board[x][y] = value;
    }


    public void printBoard() {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0)
                    System.out.print("| ");
                if (board[i][j] == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print(Integer.toString(board[i][j]));
                }
                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                output += board[i][j];
            }
        }
        return output;
    }

    //get value for the cell
    public int getCell(int x, int y) {
            return board[x][y];
    }

    //add neighbors to an empty cell
    public LinkedList<Integer> addEmptyNeighbors(int x, int y) {

        LinkedList<Integer> emptyNeighs = findEmptyNeighbors(x, y);
        return emptyNeighs;
    }

    private LinkedList<Integer> findEmptyNeighbors(int x, int y) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        //find row empty neighs
        int index = 8;
        while (index > -1) {
            if (index != x && board[index][y] == 0)
                list.addFirst((9*index) + (y));
            index--;
        }
        //find col empty neighs
        index = 8;
        while (index > -1) {
            if (index != y && board[x][index] == 0)
                list.addFirst(9 * x + index);
            index--;
        }
        //find remaining empty neighs in square
        int boxRow = (x/3);
        int boxCol = (y/3);
        for (int i = 2; i > -1; i--) {
            for (int j = 2; j > -1; j--) {
                if (!(i == x%3 || j == y%3) && board[3 * boxRow + i][3 * boxCol + j] == 0) {
                    list.addFirst(9 * (3 * boxRow + i) + 3 * boxCol + j);
                }
            }
        }
        return list;
    }

}
