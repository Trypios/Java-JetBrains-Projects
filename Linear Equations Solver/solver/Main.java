package solver;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main {

    private static String inputFilename = "." + File.separator + "test8.txt";
    private static String outputFilename = "." + File.separator + "./defaultOut.txt";


    private static void parseArgs(String[] args) {
        /*
        * Parses arguments passed in terminal
        * "-in" = path of input file
        * "-out" = path of output file
         * */

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

    private static File createFile(String filepath) {
        /*
         * Create a new file in the specified location (filepath)
         * */

        File file;
        try {
            file = new File(filepath);
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

    private static ArrayList<Row> matrixFromFile() {
        /*
         * Parses the matrix of complex numbers from input textfile
         * */

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

    private static ComplexNum resolveFromString(String str) {
        /*
         * Parses complex number from given string
         * to two parts (real part & imaginary part)
         * */

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

    private static void saveToFile(String text) {
        /*
         * Save given string to the output file
         * */

        File outFile = createFile(outputFilename);

        try (PrintWriter pw = new PrintWriter(outFile)) {
            pw.print(text.trim());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        System.out.printf("Saved to file %s%n", outFile.getName());
    }

    public static void main(String[] args) {

        // get inputFile and outputFile
        parseArgs(args);
        ArrayList<Row> grid = matrixFromFile();

        // if initialization is correct, proceed
        if (grid != null) {
            Matrix matrix = new Matrix(grid);
            matrix.run();
            saveToFile(matrix.getSolution());
        } else {
            System.out.println("Invalid input or output file.");
        }
    }
}
