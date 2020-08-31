package solver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class Matrix {

    private ArrayList<Row> matrixRows;
    private int rows;
    private final int cols;
    private final ArrayList<ComplexNum> variables;
    private String solution;


    public Matrix(ArrayList<Row> matrix) {

        this.matrixRows = matrix;
        this.rows = matrix.size();
        this.cols = matrix.get(0).getSize();
        this.variables = new ArrayList<>();
    }

    private void orderByLeadingZeros() {
        /*
        * Sorts matrix rows by their leading zeros
        * descending (rows with the most zeros, go to bottom)
        * in place
        * */

        // maybe write a better algorithm for this function

        boolean sorted = true;
        ArrayList<Integer> zeroCounters = new ArrayList<>();
        for (Row row : matrixRows) {
            int counter = 0;
            for (int i = 0; i < cols - 1; i++) {
                if (row.getElement(i).isZero()) {
                    sorted = false;
                    counter++;
                } else {
                    break;
                }
            }
            zeroCounters.add(counter);
        }

        if (sorted) {
            return;
        }

        while (!sorted) {
            sorted = true;
            for (int i = 1; i < zeroCounters.size(); i++) {
                int currentCounter = zeroCounters.get(i);
                int previousCounter = zeroCounters.get(i - 1);
                if (currentCounter < previousCounter) {
                    zeroCounters.remove(i - 1);
                    zeroCounters.add(i - 1, currentCounter);
                    zeroCounters.remove(i);
                    zeroCounters.add(i, previousCounter);
                    swapRows(i - 1, i);
                    sorted = false;
                }
            }
        }
    }

    private void removeRowsFullOfZeros() {
        /*
         * Removes rows consisting of only zeros
         * in place
         * */

        ArrayList<Row> newList = new ArrayList<>();

        for (Row row : matrixRows) {
            if (!row.isZeroed()) {
                newList.add(row);
            }
        }

        matrixRows = newList;
        this.rows = matrixRows.size();
    }

    private void swapRows(int row1Index, int row2Index) {
        /*
         * Swaps matrix rows based on given indices
         * in place
         * */

        Row row1 = matrixRows.get(row1Index);
        Row row2 = matrixRows.get(row2Index);
        matrixRows.remove(row1Index);
        matrixRows.add(row1Index, row2);
        matrixRows.remove(row2Index);
        matrixRows.add(row2Index, row1);
    }

    private void flipRowsHorizontally() {
        /*
         * Matrix flips horizontally
         * in place
         * */

        for (int i = 0; i < rows / 2; i++) {
            swapRows(i, rows - 1 - i);
        }
    }

    private void toRowEchelonForm() {
        /*
         * Turns the matrix to row echelon form
         * in place
         * */

        // for every row, except the last
        for (int r = 0; r < rows - 1; r++) {
            orderByLeadingZeros();

            Row currentRow = matrixRows.get(r);

            currentRow.setPivot();

            // find the pivot's position
            int pivotIndex = currentRow.getPivotIndex();
            if (pivotIndex == -1) {
                System.out.println("\n\n\n\n\n\n" + Arrays.toString(currentRow.getArray()));
                System.out.println("Bad index #" + r + "\n\n\n\n\n");
                break;
            }

            // for every row underneath current row
            for (int i = r + 1; i < rows; i++) {
                Row belowCurrentRow = matrixRows.get(i);
                ComplexNum element = belowCurrentRow.getElement(pivotIndex);
                Row multipliedRow = Row.multiply(currentRow, element);
                belowCurrentRow.subtract(multipliedRow);
            }
        }

        // set the last row's pivot
        matrixRows.get(rows - 1).setPivot();

        System.out.println("After calculation of row echelon form:");
        displayMatrix();
    }

    private void toReducedRowEchelonForm() {
        /*
         * Turns the matrix to reduced row echelon form
         * in place
         *
         * toRowEchelonForm() must run before this method!
         * */

        flipRowsHorizontally();

        // for every row, except the last
        for (int r = 0; r < rows - 1; r++) {

            Row currentRow = matrixRows.get(r);

            // find the pivot's position
            int pivotIndex = currentRow.getPivotIndex();
            if (pivotIndex == -1) {
                continue;
            }

            // for every row underneath current row
            for (int i = r + 1; i < rows; i++) {
                Row belowCurrentRow = matrixRows.get(i);
                ComplexNum element = belowCurrentRow.getElement(pivotIndex);
                Row multipliedRow = Row.multiply(currentRow, element);
                belowCurrentRow.subtract(multipliedRow);
            }
        }

        flipRowsHorizontally();
        System.out.println("After calculation of reduced row echelon form:");
        displayMatrix();
    }

    private String checkNumberOfSolution() {
        /*
         * Checks the number of solutions for this matrix
         * One / None / Infinite
         *
         * rowEchelonForm() must run before this method!
         * */

        int rowCounter = 0;

        for (Row row : matrixRows) {
            // check if a row is full of zeros (except the constant)

            // for the first n rows (where n = variables)
            // if a zero-ed row has been found with a non-zero constant, then infinite solutions
            if (rowCounter < cols - 1 && row.getZeroCount() == row.getSize()) {
                return "inf";
            }

            // if a fully zero-ed row has been found, then no solutions
            if (row.getZeroCount() == row.getSize() - 1 &&
                        !row.getElement(row.getSize() - 1).isZero()) {
                return "0";
            }

            rowCounter++;

        }
        return "1";
    }

    private void setSolvedVariables() {
        /*
         * After resulting to reduced REF
         * saves each row's constant (variable's solution) to an array
         *
         * toReducedRowEchelonForm() must run before this method!
         * */

        for (int r = 0; r < rows; r++) {
            Row currentRow = matrixRows.get(r);
            variables.add(currentRow.getConstant());
        }
    }

    public String getSolvedVariables(boolean unrounded) {
        /*
         * Returns a string containing the variable solutions
         * each one at its own line
         *
         * Rounds to 1d.p. if specified (unrounded=false)
         * */

        DecimalFormat format = new DecimalFormat("0.0#");
        StringBuilder text = new StringBuilder();
        for (ComplexNum variable : variables) {
            if (unrounded) {
                text.append(variable).append("\n");
            } else {
                text.append(format.format(variable)).append("\n");
            }
        }
        text.deleteCharAt(text.length() - 1);  // delete line-break
        return text.toString();
    }

    public void setSolution(String solution) {
        /*
         * Sets the string to be given as the final solution of this matrix
         * */

        this.solution = solution;
    }

    public String getSolution() {
        /*
         * Returns the final solution of this matrix
         * */

        return solution;
    }

    private void displaySolution() {
        /*
         * Prints the final solution of this matrix
         * (debugging purposes)
         * */

        if (solution != null) {
            System.out.println(solution);
        }
    }

    private void displayMatrix(boolean unrounded) {
        /*
         * Prints the matrix's current state to terminal
         * (debugging purposes)
         *
         * Rounds values (if specified) to 1d.p.
         * */

        System.out.println();
        for (Row row : matrixRows) {

            StringBuilder rowString = new StringBuilder("| ");

            Row currentRow = row;
            if (!unrounded) {
                currentRow = Row.roundAllValues(row, 1);
            }

            for (ComplexNum num : currentRow.getArray()) {
                rowString.append(num.getRealPart());
                if (num.getImaginaryPart() < 0) {
                    rowString.append(" - ");
                } else {
                    rowString.append(" + ");
                }
                rowString.append(Math.abs(num.getImaginaryPart())).append(" |");
            }
            System.out.println(rowString);
        }
        System.out.println();
    }

    private void displayMatrix() {
        displayMatrix(false);
    }

    public void run() {
        /*
         * Full process for solving the matrix
         * */

        System.out.println("\nGiven matrix:");
        displayMatrix();
        removeRowsFullOfZeros();
        toRowEchelonForm();

        switch (checkNumberOfSolution()) {
            case "1":
                break;
            case "0":
                setSolution("No solutions");
                displaySolution();
                return;
            case "inf":
                setSolution("Infinitely many solutions");
                displaySolution();
                return;
        }

        toReducedRowEchelonForm();

        for (Row row : matrixRows) {
            if (row.getZeroCount() < row.getSize() - 2) {
                setSolution("Infinitely many solutions");
                displaySolution();
                return;
            }
        }

        switch (checkNumberOfSolution()) {
            case "1":
                setSolvedVariables();
                setSolution(getSolvedVariables(true));
                break;
            case "0":
                setSolution("No solutions");
                break;
            case "inf":
                setSolution("Infinitely many solutions");
                break;
        }

        displayMatrix();
    }
}
