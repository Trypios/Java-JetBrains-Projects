// MATRIX CALCULATIONS

import java.util.*;

public class NumericMatrixProcessor {

    private static boolean isActive = true;
    private static Scanner scanner = new Scanner(System.in);
    private static Matrix matrixA;
    private static Matrix matrixB;
    private static Matrix result;

    public static void userMenu() {

        while (isActive) {
            int option = options();
            switch (option) {
                case 0:
                    // exit
                    isActive = false;
                    return;
                case 1:
                    // add matrices
                    matrixA = new Matrix(gridFromInput("first "));
                    matrixB = new Matrix(gridFromInput("second "));
                    result = matrixA.add(matrixB);
                    break;
                case 2:
                    // multiply matrix by scalar
                    matrixA = new Matrix(gridFromInput());
                    System.out.print("Input scalar: ");
                    double scalar = scanner.nextDouble();
                    System.out.println();
                    result = matrixA.multiplyByScalar(scalar);
                    break;
                case 3:
                    // multiply matrices
                    matrixA = new Matrix(gridFromInput("first "));
                    matrixB = new Matrix(gridFromInput("second "));
                    result = matrixA.multiply(matrixB);
                    break;
                case 4:
                    // matrix transposition
                    int mode = transpMode();
                    matrixA = new Matrix(gridFromInput());
                    result = matrixA.transpose(mode);
                    break;
                case 5:
                    // calculate matrix determinant
                    matrixA = new Matrix(gridFromInput());
                    System.out.println("The result is :");
                    System.out.println(matrixA.getDeterminant());
                    break;
                case 6:
                    // matrix inversion
                    // matrixA = new Matrix(gridFromInput());
                    result = matrixA.invert();
                    break;
                default:
                    System.out.println("Invalid input");
            }

            if (result != null) {
                System.out.println("The result is:");
                result.printGrid();
            }
        }
    }

    private static double[][] gridFromInput(String matrixName) {
        System.out.printf("Enter size of %smatrix: ", matrixName);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        System.out.println();

        System.out.printf("Enter %smatrix: %n", matrixName);
        double[][] newGrid = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = scanner.nextDouble();
            }
        }
        return newGrid;
    }

    private static double[][] gridFromInput() {
        return gridFromInput("");
    }

    private static int options() {
        System.out.println("1. Add matrices");
        System.out.println("2. Multiply matrix to a constant");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose matrix");
        System.out.println("5. Calculate a determinant");
        System.out.println("6. Inverse matrix");
        System.out.println("0. Exit");
        System.out.printf("Your choice: ");
        return scanner.nextInt();
    }

    private static int transpMode() {
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.printf("Your choice: ");
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        userMenu();
    }
}



class Matrix {

    int rows;
    int cols;
    double[][] grid;
    double determinant = Double.NaN;

    public Matrix(double[][] grid) {
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.grid = grid;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public double[][] getGrid() {
        return grid;
    }

    public void printGrid() {
        for (double[] row : grid) {
            for (double number : row) {
                System.out.printf("%f ", number);
            }
            System.out.println();
        }
    }

    public Matrix add(Matrix otherMatrix) {
        // Adds another matrix to this matrix. Returns the resulting new matrix object
        if (rows != otherMatrix.getRows() || cols != otherMatrix.getCols()) {
            System.out.println("The operation cannot be performed.");
            return null;
        }
        double[][] otherGrid = otherMatrix.getGrid();
        double[][] newGrid = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = grid[r][c] + otherGrid[r][c];
            }
        }
        return new Matrix(newGrid);
    }

    public Matrix subtract(Matrix otherMatrix) {
        // Subtracts another matrix from this matrix. Returns the resulting new matrix object
        return add(otherMatrix.multiplyByScalar(-1d));
    }

    public Matrix multiplyByScalar(double scalar) {
        // multiplies this matrix by a scalar. Returns the resulting matrix
        double[][] newGrid = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = grid[r][c] * scalar;
            }
        }
        return new Matrix(newGrid);
    }

    public Matrix multiply(Matrix otherMatrix) {
        // multiplies this matrix (A) by another matrix (B). Returns the resulting AB matrix
        if (cols != otherMatrix.getRows()) {
            System.out.println("Operation cannot be performed.");
            return null;
        }
        double[][] otherGrid = otherMatrix.getGrid();
        double[][] newGrid = new double[rows][otherMatrix.getCols()];
        for (int ra = 0; ra < rows; ra++) {
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

    public Matrix transpose(int mode) {
        // transposes matrix four different ways: By Main/Side diagonal or by Horizonta/Vertical flip
        // Returns the resulting new matrix object

        int rowIndex;
        int colIndex;
        double[][] newGrid;

        switch (mode) {
            case 1:
                // main diagonal transposition
                newGrid = new double[cols][rows];
                for (int c = 0; c < cols; c++) {
                    for (int r = 0; r < rows; r++) {
                        newGrid[c][r] = this.grid[r][c];
                    }
                }
                return new Matrix(newGrid);
            case 2:
                // side diagonal transposition
                newGrid = new double[cols][rows];
                rowIndex = 0;
                for (int c = cols - 1; c >= 0; c--) {
                    colIndex = 0;
                    for (int r = rows - 1; r >= 0; r--) {
                        newGrid[rowIndex][colIndex] = this.grid[r][c];
                        colIndex++;
                    }
                    rowIndex++;
                }
                return new Matrix(newGrid);
            case 3:
                // flip vertically
                newGrid = new double[rows][cols];
                for (int r = 0; r < rows; r++) {
                    colIndex = 0;
                    for (int c = cols - 1; c >= 0; c--) {
                        newGrid[r][colIndex] = this.grid[r][c];
                        colIndex++;
                    }
                }
                return new Matrix(newGrid);
            case 4:
                // flip horizontally
                newGrid = new double[rows][cols];
                rowIndex = 0;
                for (int r = rows - 1; r >= 0; r--) {
                    newGrid[rowIndex] = this.grid[r];
                    rowIndex++;
                }
                return new Matrix(newGrid);
            default:
                return null;
        }
    }

    public double getDeterminant() {
        if (Double.isNaN(determinant)) {
            determinant = calculateDeterminant();
        }
        return determinant;
    }

    private double calculateDeterminant() {
        // calculates and returns the matrix determinant

        // for 1x1 matrix
        if (rows == 1) {
            return this.grid[0][0];
        // for 2x2 matrix
        }
        if (rows == 2) {
            double mainDiag = this.grid[0][0] * this.grid[1][1];
            double sideDiag = this.grid[0][1] * this.grid[1][0];
            return mainDiag - sideDiag;
        // recursion for bigger matrices than 2x2
        } else {
            double determinant = 0;
            determinant += cofactorByCoor(rows, cols);
            return determinant;
        }
    }

    private double cofactorByCoor(int rowCoor, int colCoor) {
        // calculates and returns the cofactor of a matrix element based on coordinates

        double elementSign = Math.pow(-1, rowCoor + colCoor);
        int currentRow = 0;
        int currentCol = 0;
        double determinant = 0;
        while (currentCol < cols) {
            Matrix matrixDet = minorMatrixByCoor(currentRow, currentCol);
            double sign = sign(currentRow, currentCol);
            determinant += sign * matrixDet.calculateDeterminant();  // recursive
            currentCol++;
        }
        return elementSign * determinant;
    }

    private Matrix minorMatrixByCoor(int currentRow, int currentCol) {
        // returns the minor matrix of a matrix element based on its coordinates
        int rowIndex = 0;
        ArrayList<Double> row = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i != currentRow && j != currentCol) {
                    row.add(grid[i][j]);
                }
            }
        }
        int size = (int) Math.sqrt(row.size());
        double[][] newGrid = new double[size][size];
        rowIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newGrid[i][j] = row.get(rowIndex);
                rowIndex++;
            }
        }

        return new Matrix(newGrid);
    }

    private double sign(int rowCoor, int colCoor) {
        // returns the signed element to be multiplied with its determinant:
        // element * (-1) to the power of row + column

        return this.grid[rowCoor][colCoor] * Math.pow(-1, rowCoor + colCoor);
    }

    public Matrix invert() {
        // returns the inverse of a matrix, as a new matrix

        double determ = getDeterminant();
        if (determ == 0) {
            System.out.println("Inversion for this matrix is not possible because its determinant equals 0");
            return null;
        }
        double scalar = 1 / determ;
        Matrix cofactors = cofactorMatrix().transpose(1);
        double[][] newGrid = new double[rows][cols];
        for (int r = 0; r < cofactors.getRows(); r++) {
            for (int c = 0; c < cofactors.getCols(); c++) {
                newGrid[r][c] = scalar * cofactors.getGrid()[r][c];
            }
        }

        return new Matrix(newGrid);
    }

    private Matrix cofactorMatrix() {
        // Returns a matrix of cofactors

        // for matrix 1x1
        if (rows == 1) {
            return new Matrix(new double[][] {{grid[0][0]}});
        }

        // for matrix 2x2
        if (rows == 2) {
            double a = grid[0][0];
            double b = grid[0][1];
            double c = grid[1][0];
            double d = grid[1][1];
            return new Matrix(new double[][] {{d, -c},{-b, a}});
        }

        // for larger matrices
        double[][] newGrid = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = minorMatrixByCoor(r, c).cofactorByCoor(r, c);
            }
        }

        return new Matrix(newGrid);
    }
}
