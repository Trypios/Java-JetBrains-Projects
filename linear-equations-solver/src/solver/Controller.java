package solver;

import java.io.*;
import java.util.ArrayList;

public class Controller {

    private static String inputFilename;
    private static String outputFilename;

    // defaults
    static {
        inputFilename = "." + File.separator + "test8.txt";
        outputFilename = "." + File.separator + "./defaultOut.txt";
    }

    /**
     * Run this method to start the linear equations solver.
     * Arguments from terminal are necessary.
     * Parse terminal arguments to specify in/out files.
     * Parse a matrix from input file.
     * Solve the matrix and save solution to output file.
     * @param args arguments passed from terminal
     */
    public static void run(String[] args) {

        // check if arguments exist
        if (args.length == 0) {
            System.out.println("Open a terminal and navigate to 'out\\production\\linear-equations-solver\\' ");
            System.out.println("Type command in terminal:");
            System.out.println("java solver.Main -in ./tests/test.txt -out exampleOutputFile.txt");
            System.out.println("(more test files in that folder and also you may replace the out filename)");
            return;
        }

        // initialize inputFile and outputFile
        parseArgs(args);

        // initialize matrix
        ArrayList<Row> grid = matrixRowsFromFile();
        if (grid != null) {
            Matrix matrix = new Matrix(grid);
            matrix.solve();
            saveToFile(matrix.getSolution());
        } else {
            System.out.println("Invalid input or output file.");
        }
    }

    /**
     * Parse terminal arguments
     * "-in" = path of input file
     * "-out" = path of output file
     * @param args arguments passed from terminal
     */
    private static void parseArgs(String[] args) {

        for (int i = 0; i < args.length - 1; i += 2) {
            switch (args[i]) {
                case "-in":
                    inputFilename = String.format(".%s%s", File.separator, args[i + 1]);
                    break;
                case "-out":
                    outputFilename = String.format(".%s%s", File.separator, args[i + 1]);
            }
        }
    }

    /**
     * Create a new file by the specified filename
     * @param filename name of the new file to be created.
     * @return a File object of a newly created file.
     */
    private static File createFile(String filename) {

        File file;
        try {
            file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            return file;
        } catch (NullPointerException e) {
            System.out.println("Error with filepath");
        } catch (IOException e2) {
            System.out.println("Error creating new file");
        }

        return null;
    }

    /**
     * Parse the matrix of complex numbers from a textfile (inputFilename)
     * @return an ArrayList of Row objects, or null if something goes wrong.
     */
    private static ArrayList<Row> matrixRowsFromFile() {

        ArrayList<String> input = new ArrayList<>();
        if (inputFilename == null) {
            return null;
        }

        // read from file, line by line
        // adding to --> ArrayList<String> input
        String row;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilename))) {
            row = br.readLine();
            while (row != null) {
                input.add(row);
                row = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading from file");
            return null;
        } catch (IOException e2) {
            System.out.println("Error reading file");
            return null;
        }

        if (input.isEmpty()) {
            System.out.println("Empty input");
            return null;
        }

        int rows = Integer.parseInt(input.get(0).split(" ")[1]);
        int cols = Integer.parseInt(input.get(0).split(" ")[0]) + 1;
        input.remove(0);

        ArrayList<Row> matrix = new ArrayList<>();

        // for each input line there are complex numbers to parse
        for (int r = 0; r < rows; r++) {

            String[] currentRow = input.get(r).split(" ");

            ComplexNum[] complexElements = new ComplexNum[currentRow.length];
            for (int c = 0; c < cols; c++) {
                // add complex num to the row
                complexElements[c] = resolveFromString(currentRow[c]);
            }
            matrix.add(new Row(complexElements));
        }

        return matrix;
    }

    /**
     * Parse complex number from given string
     * (real part & imaginary part, e.g: 5.2 -3i)
     * @param str a string denoting a complex number.
     * @return a ComplexNum object
     */
    private static ComplexNum resolveFromString(String str) {

        String[] parts = str.split("(?=[+-])");

        double realPart = 0;
        double imaginaryPart = 0;

        if (parts.length == 2) {
            realPart = Double.parseDouble(parts[0]);
            if ("+i".equals(parts[1])) {
                imaginaryPart = 1d;
            } else if ("-i".equals(parts[1])) {
                imaginaryPart = -1d;
            } else {
                imaginaryPart = Double.parseDouble(
                        parts[1].substring(0, parts[1].length() - 1)
                );
            }
        } else {
            if ("i".equals(parts[0])) {
                imaginaryPart = 1d;
            } else if ("-i".equals(parts[0])) {
                imaginaryPart = -1d;
            } else if (parts[0].endsWith("i")) {
                imaginaryPart = Double.parseDouble(
                        parts[0].substring(0, parts[0].length() - 1)
                );
            } else {
                realPart = Double.parseDouble(parts[0]);
            }
        }

        return new ComplexNum(realPart, imaginaryPart);
    }

    /**
     * Save given string to the output file.
     * @param text a string to be saved to a file.
     */
    private static void saveToFile(String text) {

        File outFile = createFile(outputFilename);

        try (PrintWriter pw = new PrintWriter(outFile)) {
            pw.print(text.trim());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        System.out.printf("Saved to file %s%n", outFile.getName());
    }
}
