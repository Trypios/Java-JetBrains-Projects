package matrixprocessor;

import java.util.ArrayList;

class Matrix {

    private final int ROWS;
    private final int COLS;
    double[][] grid;
    double determinant = Double.NaN;

    public Matrix(double[][] grid) {
        this.ROWS = grid.length;
        this.COLS = grid[0].length;
        this.grid = grid;
    }

    /**
     * Getter
     * @return count of rows of the matrix.
     */
    public int getRows() {

        return ROWS;
    }

    /**
     * Getter
     * @return count of columns of the matrix.
     */
    public int getCols() {

        return COLS;
    }

    /**
     * Getter
     * @return the matrix grid
     */
    public double[][] getGrid() {

        return grid;
    }

    /**
     * Display the grid in a rectangle grid format.
     */
    public void printGrid() {

        for (double[] row : grid) {
            for (double number : row) {
                System.out.printf("%f ", number);
            }
            System.out.println();
        }
    }

    /**
     * Add this matrix with another matrix (this + other), not in place.
     * @param otherMatrix other matrix to be added to.
     * @return a new Matrix object.
     */
    public Matrix add(Matrix otherMatrix) {

        if (ROWS != otherMatrix.getRows() || COLS != otherMatrix.getCols()) {
            System.out.println("The operation cannot be performed.");
            return null;
        }
        double[][] otherGrid = otherMatrix.getGrid();
        double[][] newGrid = new double[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                newGrid[r][c] = grid[r][c] + otherGrid[r][c];
            }
        }
        return new Matrix(newGrid);
    }

    /**
     * Subtract another matrix from this matrix (this - other), not in place.
     * @param otherMatrix other matrix to subtract from.
     * @return a new Matrix object.
     */
    public Matrix subtract(Matrix otherMatrix) {

        return add(otherMatrix.multiply(-1d));
    }

    /**
     * Multiply this matrix by a scalar (this * scalar), not in place.
     * @param scalar a number to multiply each matrix element by.
     * @return a new Matrix object.
     */
    public Matrix multiply(double scalar) {

        double[][] newGrid = new double[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                newGrid[r][c] = grid[r][c] * scalar;
            }
        }
        return new Matrix(newGrid);
    }

    /**
     * Multiply this matrix by another matrix (this * other), not in place.
     * @param otherMatrix other matrix to multiply by.
     * @return a new Matrix object.
     */
    public Matrix multiply(Matrix otherMatrix) {

        if (COLS != otherMatrix.getRows()) {
            System.out.println("Operation cannot be performed.");
            return null;
        }
        double[][] otherGrid = otherMatrix.getGrid();
        double[][] newGrid = new double[ROWS][otherMatrix.getCols()];
        for (int ra = 0; ra < ROWS; ra++) {
            for (int cb = 0; cb < otherMatrix.getCols(); cb++) {
                double sum = 0;
                for (int rb = 0; rb < otherMatrix.getRows(); rb++) {
                    sum += grid[ra][rb] * otherGrid[rb][cb];
                }
                newGrid[ra][cb] = sum;
            }
        }
        return new Matrix(newGrid);
    }

    /**
     * Transpose matrix by a specified mode, not in place.
     * @param mode The mode of matrix transposition
     *             (by main diagonal, by side diagonal, horizontal flip, vertical flip).
     * @return a new Matrix object.
     */
    public Matrix transpose(int mode) {

        int rowIndex;
        int colIndex;
        double[][] newGrid;

        switch (mode) {
            case 1:
                // main diagonal transposition
                newGrid = new double[COLS][ROWS];
                for (int c = 0; c < COLS; c++) {
                    for (int r = 0; r < ROWS; r++) {
                        newGrid[c][r] = this.grid[r][c];
                    }
                }
                return new Matrix(newGrid);
            case 2:
                // side diagonal transposition
                newGrid = new double[COLS][ROWS];
                rowIndex = 0;
                for (int c = COLS - 1; c >= 0; c--) {
                    colIndex = 0;
                    for (int r = ROWS - 1; r >= 0; r--) {
                        newGrid[rowIndex][colIndex] = this.grid[r][c];
                        colIndex++;
                    }
                    rowIndex++;
                }
                return new Matrix(newGrid);
            case 3:
                // flip vertically
                newGrid = new double[ROWS][COLS];
                for (int r = 0; r < ROWS; r++) {
                    colIndex = 0;
                    for (int c = COLS - 1; c >= 0; c--) {
                        newGrid[r][colIndex] = this.grid[r][c];
                        colIndex++;
                    }
                }
                return new Matrix(newGrid);
            case 4:
                // flip horizontally
                newGrid = new double[ROWS][COLS];
                rowIndex = 0;
                for (int r = ROWS - 1; r >= 0; r--) {
                    newGrid[rowIndex] = this.grid[r];
                    rowIndex++;
                }
                return new Matrix(newGrid);
            default:
                return null;
        }
    }

    /**
     * Getter
     * @return the matrix's determinant.
     */
    public double getDeterminant() {
        if (Double.isNaN(determinant)) {
            determinant = calculateDeterminant();
        }
        return determinant;
    }

    /**
     * Calculate the matrix's determinant.
     * @return the determinant.
     */
    private double calculateDeterminant() {

        // for 1x1 matrix
        if (ROWS == 1) {
            return this.grid[0][0];
            // for 2x2 matrix
        }
        if (ROWS == 2) {
            double mainDiag = this.grid[0][0] * this.grid[1][1];
            double sideDiag = this.grid[0][1] * this.grid[1][0];
            return mainDiag - sideDiag;
            // recursion for bigger matrices than 2x2
        } else {
            double determinant = 0;
            determinant += cofactorByCoor(ROWS, COLS);
            return determinant;
        }
    }

    /**
     * Calculate the cofactor of a matrix element based on its coordinates.
     * @param rowCoor an element's row coordinate.
     * @param colCoor an element's column coordinate.
     * @return the cofactor of an element.
     */
    private double cofactorByCoor(int rowCoor, int colCoor) {

        double elementSign = Math.pow(-1, rowCoor + colCoor);
        int currentRow = 0;
        int currentCol = 0;
        double determinant = 0;
        while (currentCol < COLS) {
            Matrix matrixDet = minorMatrixByCoor(currentRow, currentCol);
            double sign = sign(currentRow, currentCol);
            determinant += sign * matrixDet.calculateDeterminant();  // recursive
            currentCol++;
        }
        return elementSign * determinant;
    }

    /**
     * Calculate the minor matrix of an element based on its coordinates.
     * @param rowCoor an element's row coordinate.
     * @param colCoor an element's column coordinate.
     * @return a new Matrix object
     */
    private Matrix minorMatrixByCoor(int rowCoor, int colCoor) {

        ArrayList<Double> row = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (i != rowCoor && j != colCoor) {
                    row.add(grid[i][j]);
                }
            }
        }

        int size = (int) Math.sqrt(row.size());
        double[][] newGrid = new double[size][size];
        int rowIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newGrid[i][j] = row.get(rowIndex);
                rowIndex++;
            }
        }

        return new Matrix(newGrid);
    }

    /**
     * Find the correct sign of the element if to be multiplied with its determinant.
     * Formula: element * (-1)^(row + column)
     * @param rowCoor an element's row coordinate.
     * @param colCoor an element's column coordinate.
     * @return the signed element
     */
    private double sign(int rowCoor, int colCoor) {

        return this.grid[rowCoor][colCoor] * Math.pow(-1, rowCoor + colCoor);
    }

    /**
     * Calculate the inverse of a matrix, not in place.
     * @return a new Matrix object.
     */
    public Matrix invert() {
        // returns the inverse of a matrix, as a new matrix

        double determ = getDeterminant();
        if (determ == 0) {
            System.out.println("Inversion for this matrix is not possible because its determinant equals 0");
            return null;
        }
        double scalar = 1 / determ;
        Matrix cofactors = cofactorMatrix().transpose(1);
        double[][] newGrid = new double[ROWS][COLS];
        for (int r = 0; r < cofactors.getRows(); r++) {
            for (int c = 0; c < cofactors.getCols(); c++) {
                newGrid[r][c] = scalar * cofactors.getGrid()[r][c];
            }
        }

        return new Matrix(newGrid);
    }

    /**
     * Calculate the matrix of cofactors of this matrix.
     * @return a new Matrix object.
     */
    private Matrix cofactorMatrix() {

        // for matrix 1x1
        if (ROWS == 1) {
            return new Matrix(new double[][] {{grid[0][0]}});
        }

        // for matrix 2x2
        if (ROWS == 2) {
            double a = grid[0][0];
            double b = grid[0][1];
            double c = grid[1][0];
            double d = grid[1][1];
            return new Matrix(new double[][] {{d, -c},{-b, a}});
        }

        // for larger matrices
        double[][] newGrid = new double[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                newGrid[r][c] = minorMatrixByCoor(r, c).cofactorByCoor(r, c);
            }
        }

        return new Matrix(newGrid);
    }
}