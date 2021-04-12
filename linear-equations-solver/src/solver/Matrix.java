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

    /**
     * Sort matrix rows by their leading zeros, descending
     * (rows with the most zeros, go to bottom).
     * In place.
     */
    private void orderByLeadingZeros() {

        // maybe devise a better algorithm for this function

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

    /**
     * Remove rows consisting of only zeros, in place.
     */
    private void removeRowsFullOfZeros() {

        ArrayList<Row> newList = new ArrayList<>();

        for (Row row : matrixRows) {
            if (!row.isZeroed()) {
                newList.add(row);
            }
        }

        matrixRows = newList;
        this.rows = matrixRows.size();
    }

    /**
     * Swap matrix rows based on specified indexes, in place.
     * @param row1Index index of 1st row
     * @param row2Index index of 2nd row
     */
    private void swapRows(int row1Index, int row2Index) {

        Row row1 = matrixRows.get(row1Index);
        Row row2 = matrixRows.get(row2Index);
        matrixRows.remove(row1Index);
        matrixRows.add(row1Index, row2);
        matrixRows.remove(row2Index);
        matrixRows.add(row2Index, row1);
    }

    /**
     * Flip Matrix horizontally, in place.
     */
    private void flipRowsHorizontally() {

        for (int i = 0; i < rows / 2; i++) {
            swapRows(i, rows - 1 - i);
        }
    }

    /**
     * Turn the matrix to row echelon form, in place.
     */
    private void toRowEchelonForm() {

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

    /**
     * Turn the matrix to reduced row echelon form, in place.
     * toRowEchelonForm() must run before this method!
     */
    private void toReducedRowEchelonForm() {

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

    /**
     * Check the number of solutions for this matrix (One / None / Infinite).
     * rowEchelonForm() must run before this method!
     * @return the number of solutions for this matrix
     */
    private SolutionsCount checkNumberOfSolution() {

        int rowCounter = 0;

        for (Row row : matrixRows) {
            // check if a row is full of zeros (except the constant)

            // for the first n rows (where n = variables)
            // if a zero-ed row has been found with a non-zero constant, then infinite solutions
            if (rowCounter < cols - 1 && row.getZeroCount() == row.getSize()) {
                return SolutionsCount.INFINITE;
            }

            // if a fully zero-ed row has been found, then no solutions
            if (row.getZeroCount() == row.getSize() - 1 &&
                        !row.getElement(row.getSize() - 1).isZero()) {
                return SolutionsCount.ZERO;
            }

            rowCounter++;

        }
        return SolutionsCount.ONE;
    }

    /**
     * After reducing to Row Echelon Form,
     * saves each row's constant (variable's solution) to an array.
     * toReducedRowEchelonForm() must run before this method!
     */
    private void setSolvedVariables() {

        for (int r = 0; r < rows; r++) {
            Row currentRow = matrixRows.get(r);
            variables.add(currentRow.getConstant());
        }
    }

    /**
     * Getter of the matrix's solved variables.
     * @param unrounded set to true to round each solution to 1 d.p.
     * @return a string containing the variable solutions, line separated.
     */
    public String getSolvedVariables(boolean unrounded) {

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

    /**
     * Set the specified solution as the final solution of this matrix.
     * @param solution this Matrix's solved variables.
     */
    private void setSolution(String solution) {

        this.solution = solution;
    }

    /**
     * Getter of this matrix's final solution.
     * @return a string of this matrix's solved variables.
     */
    public String getSolution() {

        return solution;
    }

    /***
     * Print the final solution of this matrix
     * (for debugging purposes)
     */
    private void displaySolution() {

        if (solution != null) {
            System.out.println(solution);
        }
    }

    /**
     * Display the matrix's current state.
     * (for debugging purposes)
     * @param unrounded set to true to round each solution to 1 d.p.
     */
    private void displayMatrix(boolean unrounded) {

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

    /**
     * Display the matrix's current state, as is (no rounding).
     */
    private void displayMatrix() {
        displayMatrix(false);
    }

    /**
     * Run this method to solve the matrix.
     */
    public void solve() {

        System.out.println("\nGiven matrix:");
        displayMatrix();
        removeRowsFullOfZeros();
        toRowEchelonForm();

        switch (checkNumberOfSolution()) {
            case ONE:
                break;
            case ZERO:
                setSolution("No solutions");
                displaySolution();
                return;
            case INFINITE:
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
            case ONE:
                setSolvedVariables();
                setSolution(getSolvedVariables(true));
                break;
            case ZERO:
                setSolution("No solutions");
                break;
            case INFINITE:
                setSolution("Infinitely many solutions");
                break;
        }

        displayMatrix();
    }
}


enum SolutionsCount {

    ONE,
    ZERO,
    INFINITE
}
