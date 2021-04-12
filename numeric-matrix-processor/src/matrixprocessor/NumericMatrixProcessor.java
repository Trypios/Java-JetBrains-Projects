package matrixprocessor;

import java.util.*;

/**
 * Perform a variety of operations on matrices including:
 * addition, multiplication, finding the determinant, and dealing with inverse matrices.
 */
public class NumericMatrixProcessor {

    private static boolean isActive = true;
    private static final Scanner SCANNER = new Scanner(System.in);
    private static Matrix result;

    /**
     * Run this method to start the matrix processor.
     */
    public static void run() {

        while (isActive) {
            userMenu();
        }
    }

    /**
     * The matrix processor's main menu of operations.
     */
    private static void userMenu() {

        Matrix matrixA, matrixB;

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
                // subtract matrices
                matrixA = new Matrix(gridFromInput("first "));
                matrixB = new Matrix(gridFromInput("second "));
                result = matrixA.subtract(matrixB);
                break;
            case 3:
                // multiply matrix by scalar
                matrixA = new Matrix(gridFromInput());
                double scalar;
                System.out.print("Input scalar: ");
                while (true) {
                    try {
                        scalar = Double.parseDouble(SCANNER.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please try again.");
                    }
                }
                System.out.println();
                result = matrixA.multiply(scalar);
                break;
            case 4:
                // multiply matrices
                matrixA = new Matrix(gridFromInput("first "));
                matrixB = new Matrix(gridFromInput("second "));
                result = matrixA.multiply(matrixB);
                break;
            case 5:
                // matrix transposition
                int mode = transpositionMode();
                matrixA = new Matrix(gridFromInput());
                result = matrixA.transpose(mode);
                break;
            case 6:
                // calculate matrix determinant
                matrixA = new Matrix(gridFromInput());
                System.out.println("The result is :");
                System.out.println(matrixA.getDeterminant());
                break;
            case 7:
                // matrix inversion
                matrixA = new Matrix(gridFromInput());
                result = matrixA.invert();
                break;
            default:
                System.out.println("Invalid input, please try again.");
        }

        if (result != null) {
            System.out.println("The result is:");
            result.printGrid();
        }
    }

    /**
     * Request user input concerning the size of the matrix (two numbers separated by space).
     * Then request user input of all the matrix's elements (numbers separated by space, row by row).
     * @param matrixName name of the matrix (for printing purposes).
     * @return a Matrix grid in the form of a 2D array.
     */
    private static double[][] gridFromInput(String matrixName) {
        System.out.printf("Enter size of %smatrix: ", matrixName);
        int rows, cols;
        while (true) {
            try {
                String[] input = SCANNER.nextLine().split(" ");
                rows = Integer.parseInt(input[0]);
                cols = Integer.parseInt(input[1]);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter size again.");
            }
        }
        System.out.println();

        System.out.printf("Enter %smatrix: %n", matrixName);
        double[][] newGrid = new double[rows][cols];
        String[] input;
        while (true) {
            try {
                for (int r = 0; r < rows; r++) {
                    input = SCANNER.nextLine().split(" ");
                    for (int c = 0; c < cols; c++) {
                        newGrid[r][c] = Double.parseDouble(input[c]);
                    }
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter all elements again (row by row).");
                newGrid = new double[rows][cols];
            }
        }
        return newGrid;
    }

    /**
     * Overloaded method.
     * Same as double[][] gridFromInput(String) with unnamed matrix.
     */
    private static double[][] gridFromInput() {
        return gridFromInput("");
    }

    /**
     * Request user input concerning the operation to be performed.
     * @return the number of option (0-6).
     */
    private static int options() {

        System.out.println("Operations available:");
        System.out.println("    1. Add matrices");
        System.out.println("    2. Subtract matrices");
        System.out.println("    3. Multiply matrix to a constant");
        System.out.println("    4. Multiply matrices");
        System.out.println("    5. Transpose matrix");
        System.out.println("    6. Calculate a determinant");
        System.out.println("    7. Inverse matrix");
        System.out.println("    0. Exit");
        System.out.print("Your choice: ");
        while (true) {
            try {
                int option = Integer.parseInt(SCANNER.nextLine());
                if (option >= 0 && option <= 7) {
                    return option;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter operation again (0-7)");
            }
        }
    }

    /**
     * Request user input concerning the transposition mode
     * to be performed on a matrix.
     * @return the number of transposition mode option (1-4).
     */
    private static int transpositionMode() {

        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.print("Your choice: ");
        while (true) {
            try {
                int option = Integer.parseInt(SCANNER.nextLine());
                if (option >= 1 && option <= 4) {
                    return option;
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter transposition mode again (1-4)");
            }
        }
    }

}
