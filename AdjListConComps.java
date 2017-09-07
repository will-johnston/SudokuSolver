import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by williamjohnston on 5/6/17.
 * creates an adjacency list and connected components
 * with empty cells in board to be used for the solver
 */

public class AdjListConComps {

    LinkedList<Integer>[] adj;  //adjacency list
    LinkedList<Integer> cc;  //connected components
    boolean[] marked;  //array for dfs to check if cell has been found already


    public AdjListConComps(SudokuBoard sudokuBoard) {
        adj = (LinkedList<Integer>[]) new LinkedList[81];
        cc = new LinkedList<>();
        marked = new boolean[81];
        Arrays.fill(marked, false);
        createAdjList(sudokuBoard);
        findConnectedComponents();
    }

    //got through each cell in board and if the value is not 1-9, add its neighbors to the adj list
    private void createAdjList(SudokuBoard sudokuBoard) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard.getCell(i, j) == 0) {
                    adj[(i*9)+j] = sudokuBoard.addEmptyNeighbors(i,j);

                }
            }
        }
    }

    //using depth first search, find the connected components of the board
    private void findConnectedComponents() {
        for (int i = 0; i < 81; i++) {
            if (adj[i] == null) {
                continue;
            } else {
                if (marked[i] == false) {
                    cc.add(i);
                    dfs(i);
                }
            }
        }
    }

    //recursive depth first search algorithm
    private void dfs(int v) {
        marked[v] = true;
        for (int w : adj[v]) {
            if (marked[w] == false) {
                dfs(w);
            }
        }
    }

    public void printAdjList() {
        System.out.println();
        for (int i = 0; i < 81; i++) {
            System.out.print(i + ": ");
            if (adj[i] != null) {
                for (int c : adj[i]) {
                    System.out.print("(" + c/9 + ", " + c%9 + ") ");
                }
            }
            System.out.println();
        }
    }

    public void printCC() {
        System.out.println();
        for (int c : cc) {
            System.out.print("(" + c/9 + ", " + c%9 + ") ");
        }
        System.out.println();
    }
}


