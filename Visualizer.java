import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by williamjohnston on 5/8/17.
 */
public class Visualizer extends JFrame implements ActionListener{

    private static final long serialVersionUID = 0;
    private JTextField f[][]= new JTextField[9][9];  //contains values for sudoku board
    private JTextField blankBoard[][] = new JTextField[9][9];  //original, non-edited board
    private JPanel p[][]= new JPanel [3][3];  //used for each 3x3 box in board
    boolean solved = false;  //true if board has just been solved


    //create GUI
    public Visualizer(String input) {
        super("Sudoku");
        initializeTextFields(input);
        initializeBlankBoard();
        initializePanelArray();

        JPanel grid = new JPanel(new GridLayout(3,3,2,2));  //this will house the 3x3 boxes
        updatePanelArray(grid);

        //create solve and check buttons on gui
        JPanel buttonPan = new JPanel();
        JButton solve = new JButton("Solve");
        JButton check = new JButton("Check");
        check.addActionListener(this);
        solve.addActionListener(this);
        check.setActionCommand("check");
        solve.setActionCommand("solve");
        buttonPan.add(solve);
        buttonPan.add(check);

        grid.setPreferredSize(new Dimension(300,300));
        add(grid, BorderLayout.CENTER);
        add(buttonPan, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(true);
        pack();
        setVisible(true);
    }

    //put individual TextField cells in to 3x3 panels. Add panels to grid
    public void updatePanelArray(JPanel grid) {
        for(int a = 0; a <= 2; a++){
            for(int b = 0; b <= 2; b++){
                for(int x = 0; x <= 2; x++ ){
                    for(int y = 0; y <= 2; y++){
                        p[a][b].add(f[x + a*3][y + b*3]);
                    }
                }
                grid.add(p[a][b]);
            }
        }
    }
    //create new panel array in the grid layout
    public void initializePanelArray() {
        for(int i = 0; i <= 2; i++){
            for(int j = 0; j <= 2; j++){
                p[i][j]=new JPanel(new GridLayout(3,3));
            }
        }
    }
    //place original values of board to blankboard (f intialized with original values)
    public void initializeBlankBoard() {
        for(int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                blankBoard[i][j] = new JTextField(1);
                blankBoard[i][j].setText(f[i][j].getText());
            }
        }
    }
    //create text fields, add appropriate values from input
    public void initializeTextFields(String input) {
        int pos = 0;
        for(int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                f[i][j] = new JTextField(1);
                f[i][j].setHorizontalAlignment(JTextField.CENTER);
                f[i][j].setBackground(Color.lightGray);
                //see if boxes have been edited, if so, the puzzle may not be solved.
                f[i][j].getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        solved = false;
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        solved = false;
                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        solved = false;
                    }
                });
                //add value to field
                char curr = input.charAt(pos++);
                if (curr != '0') {
                    f[i][j].setText(String.valueOf(curr));
                    f[i][j].setEditable(false);
                } else {
                    f[i][j].setForeground(Color.RED);
                }

            }
        }
    }

    //see if boxes have been clicked. If so, solve or check puzzle accordingly.
    @Override
    public void actionPerformed(ActionEvent e) {
        String blank = getBoard(blankBoard);  //unedited board in string format
        //find solution to blank board
        SudokuBoard sudokuBoard = new SudokuBoard(blank);
        SudokuSolver solver = new SudokuSolver(sudokuBoard);
        String solution = solver.getBoard();
        if (solution == null) {  //no solution can be found to the blank board-- this shouldn't happen
            System.out.println("No solution");
            popup("No solution! Try another puzzle.", "solve");

        }

        //if check button is pressed...
        if (e.getActionCommand().equals("check")) {
            if (this.solved) {  //true if board has been solved with no edits since
                System.out.println("Solved!");
                popup("Solved!", "check");
                return;
            }
            String currentBoard = getBoard(this.f);  //board after edits
            if (currentBoard == null) {  //message already from getBoard function
                return;
            }
            //check if string formats of edited board and solved blank board match
            else if (solution.equals(currentBoard)) {
                System.out.println("Solved!");
                popup("Solved!", "check");

            } else {
                System.out.println("Not Solved!");
                popup("Not Solved!", "check");

            }
        }

        //if solve button is pressed...
        else if (e.getActionCommand().equals("solve")) {

            int pos = 0;
            for (int a = 0; a < 9; a++) {
                for (int b = 0; b < 9; b++) {
                    f[a][b].setText(String.valueOf(solution.charAt(pos++)));
                }
            }
            this.solved = true;
        }
    }
    //gets the board from the TextFields and puts it in string format. If improper format, return null
    public String getBoard(JTextField array[][]) {
        //convert JTextField array to String array
        String[][] buffer = new String[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buffer[i][j] = array[i][j].getText();
            }
        }
        //check to see if inputs are valid
        String currentBoard = "";
        int illegals = 0;
        boolean legal = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (java.util.regex.Pattern.matches("[1-9]", buffer[i][j])) {
                    currentBoard += buffer[i][j];
                } else if (buffer[i][j].equals("")) {
                    currentBoard += '0';
                } else {
                    System.out.println("There is an illegal textfield at: " + (i + 1) + "," + (j + 1));
                    illegals++;
                    legal = false;
                }
            }
        }
        if (!legal) {
            popup("You have " + illegals + " illegal text field(s)!", "check");
            return null;

        } else {
            return currentBoard;
        }
    }
    public void popup(String message, String title)
    {
        JOptionPane.showMessageDialog(null, message, "Sudoku: " + title, JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        File input = new File("/Users/williamjohnston/IdeaProjects/SudokuSolver/src/solved3.txt");
        if (input == null) {
            System.out.println("Illegal input");
        }
        //parse input file -> string
        String line = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(input));
            line = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Visualizer(line);
    }
}



