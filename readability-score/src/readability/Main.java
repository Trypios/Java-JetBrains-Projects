package readability;

/**
 * Input source text-file from terminal
 * i.e: java readability.Main.java in.txt
 * otherwise a default file will be used.
 *
 * Program calculates:
 *    Automated Readability index,
 *    Flesch-Kincaid Readability test,
 *    Simple Measure of Gobbledygook, and
 *    Coleman-Liau index
 */
public class Main {

    public static void main(String[] args) {

        Controller.run(args);
    }
}
