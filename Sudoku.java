import java.util.ArrayList;

/**
 * Class for recursively finding a solution to a Sudoku problem.
 *
 * @author Biagioni, Edoardo, Cam Moore  October 23, 2013
 * @missing solveSudoku and legalValues, to be implemented by the students in ICS 211
 */
public class Sudoku {
  /**
   * Find an assignment of values to sudoku cells that makes the sudoku valid.
   *
   * @param sudoku is the sudoku to be solved
   * @return whether a solution was found if a solution was found, the sudoku is filled in with the 
   *  solution if no solution was found, restores the sudoku to its original value
   */
  public static boolean solveSudoku(int[][] sudoku) {
    // TODO: Implement this method recursively. You may use a recursive helper method.
    if (checkSudoku(sudoku, false) && sudokuFilled(sudoku)) { //base case 
      return true;
    }
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 9;c++) {
        int test = sudoku[r][c];
        if (test == 0) {
          return solveSudoku(sudoku, r, c); //calling my recursive helper function
        }
      }
    }
    return true; //another base case
  }
  /**
   * Recursive helper method.
   * @param sudoku being solved
   * @param row of cell being tested
   * @param col column of cell being tested
   * @return true if solved, false if not solveable
   */
  
  public static boolean solveSudoku(int[][] sudoku, int row, int col) { //Helped by: Fred Straub
    if (row == 9) { //base case for helper recursion method
      return true;
    }
    int nextrow = row;
    int nextcol = col + 1;
    
    if (nextcol == 9) { //once you reach the end of the row
      nextcol = 0; //set nextcol to 0; col will be 8
      nextrow++; //increment row
    }
    
    if (sudoku[row][col] != 0) { //if not empty
      if (solveSudoku(sudoku, nextrow, nextcol)) { //check the next cell and see if sudoku is solved
        return true; //game done ??
      }
    } else { //when cell is empty
      ArrayList<Integer> legal = legalValues(sudoku, row, col);
      //getting legal values for the current empty cell
      for (int v : legal) {
        sudoku[row][col] = v; //putting first legal value in cell
        boolean check = solveSudoku(sudoku, row, col); //
        if (check) {
          return checkSudoku(sudoku, false); //if ??
        } else {
          sudoku[row][col] = 0;
          //back track and then try the rest of legal values
        }
      }
    }
    return false;
  }
  /**
   * Checks if sudoku is filled.
   * @param sudoku being tested.
   * @return true if sudoku is filled, false if cell is empty/contains 0.
   */
  
  public static boolean sudokuFilled(int[][] sudoku) {
    for (int r = 0; r < 9; r++) {
      for (int c = 0; c < 9;c++) {
        int test = sudoku[r][c];
        if (test == 0) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Find the legal values for the given sudoku and cell.
   *
   * @param sudoku the sudoku being solved.
   * @param row the row of the cell to get values for.
   * @param column the column of the cell.
   * @return an ArrayList of the valid values.
   */
  public static ArrayList<Integer> legalValues(int[][] sudoku, int row, int column) {
    // TODO: Implement this method. You may want to look at the checkSudoku method
    // to see how it finds conflicts.
    ArrayList<Integer> legal = new ArrayList<Integer>();
    for (int i = 1; i < 10; i++) {
      legal.add(i); //adding numbers 1...9 to base arraylist
    }
    int cellNum = sudoku[row][column];
    if (cellNum == 0) {
      for (int i = 0; i < 9; i++) {
        if (sudoku[row][i] != 0) {
          Object var = sudoku[row][i];
          legal.remove(var); //removing value found from arrlist
        }
      }
      for (int i = 0; i < 9; i++) {
        if (sudoku[i][column] != 0) {
          Object var = sudoku[i][column];
          legal.remove(var);
        }
      } //removing the 3 x 3 box 
      //Corben helped with this piece of code
      for (int l = 0; l < 3; l++) {
        for (int w = 0; w < 3; w++) {
          int boxRow = ((row / 3) * 3) + l;
          int boxCol = ((column / 3) * 3) + w;
          Object boxCheck = sudoku[boxRow][boxCol];
          legal.remove(boxCheck);
        }
      }
    } else {
      return null;
    }
    return legal;
  }


  /**
   * checks that the sudoku rules hold in this sudoku puzzle. cells that contain 0 are not checked.
   *
   * @param sudoku is the sudoku to be checked
   * @param printErrors whether to print the error found, if any
   * @return true if this sudoku obeys all of the sudoku rules, otherwise false
   */
  public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
    if (sudoku.length != 9) {
      if (printErrors) {
        System.out.println("sudoku has " + sudoku.length + " rows, should have 9");
      }
      return false;
    }
    for (int i = 0; i < sudoku.length; i++) {
      if (sudoku[i].length != 9) {
        if (printErrors) {
          System.out.println("sudoku row " + i + " has " + sudoku[i].length + " cells, should have 9");
        }
        return false;
      }
    }
    /* check each cell for conflicts */
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku.length; j++) {
        int cell = sudoku[i][j];
        if (cell == 0) {
          continue; /* blanks are always OK */
        }
        if ((cell < 1) || (cell > 9)) {
          if (printErrors) {
            System.out.println("sudoku row " + i + " column " + j + " has illegal value " + cell);
          }
          return false;
        }
        /* does it match any other value in the same row? */
        for (int m = 0; m < sudoku.length; m++) {
          if ((j != m) && (cell == sudoku[i][m])) {
            if (printErrors) {
              System.out.println("sudoku row " + i + " has " + cell + " at both positions " + j + " and " + m);
            }
            return false;
          }
        }
        /* does it match any other value it in the same column? */
        for (int k = 0; k < sudoku.length; k++) {
          if ((i != k) && (cell == sudoku[k][j])) {
            if (printErrors) {
              System.out.println("sudoku column " + j + " has " + cell + " at both positions " + i + " and " + k);
            }
            return false;
          }
        }
        /* does it match any other value in the 3x3? */
        for (int k = 0; k < 3; k++) {
          for (int m = 0; m < 3; m++) {
            int testRow = (i / 3 * 3) + k; /* test this row */
            int testCol = (j / 3 * 3) + m; /* test this col */
            if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
              if (printErrors) {
                System.out.println("sudoku character " + cell + " at row " + i + ", column " + j
                    + " matches character at row " + testRow + ", column " + testCol);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }


  /**
   * Converts the sudoku to a printable string.
   *
   * @param sudoku is the sudoku to be converted
   * @param debug whether to check for errors
   * @return the printable version of the sudoku
   */
  public static String toString(int[][] sudoku, boolean debug) {
    if ((!debug) || (checkSudoku(sudoku, true))) {
      String result = "";
      for (int i = 0; i < sudoku.length; i++) {
        if (i % 3 == 0) {
          result = result + "+-------+-------+-------+\n";
        }
        for (int j = 0; j < sudoku.length; j++) {
          if (j % 3 == 0) {
            result = result + "| ";
          }
          if (sudoku[i][j] == 0) {
            result = result + "  ";
          } else {
            result = result + sudoku[i][j] + " ";
          }
        }
        result = result + "|\n";
      }
      result = result + "+-------+-------+-------+\n";
      return result;
    }
    return "illegal sudoku";
  }
}
