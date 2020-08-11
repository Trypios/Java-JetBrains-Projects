/* 
input source textfile from terminal
i.e: java Main.java in.txt

Program Calculates:
* Automated Readability index, 
* Flesch-Kincaid Readability test, 
* Simple Measure of Gobbledygook and 
* Coleman-Liau index

*/

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class ReadabilityScore {

    private static Scanner scanner = new Scanner(System.in);
    private static String text;

    public static void main(String[] args) {
        String filename = args[0];
        text = readFromFile(filename);
        menu();
        scanner.close();
    }

    private static void menu() {
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String userInput = scanner.nextLine();
        if (userInput.matches("ARI|FK|SMOG|CL|all")) {
            ReadabilityScoreFactory.applyAlgorithm(userInput, text);
        } else {
            System.out.println("Invalid input.");
        }
    }

    public static String readFromFile(String filename) {
        String data = "";
        try (FileReader reader = new FileReader(filename)) {
            int unicode = reader.read();
            while (unicode != -1) {
                data += Character.toString(unicode);
                unicode = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

abstract class ReadabilityScore {

    protected String METHOD_NAME;
    protected String data;
    protected int characterCount;
    protected int wordCount;
    protected int sentenceCount;
    protected int syllableCount;
    protected int polysyllableCount;
    protected double score;
    protected String ageRange;

    public ReadabilityScore(String data) {
        this.data = data;
        this.characterCount = data.replaceAll("\\s","").split("").length;
        this.wordCount = data.split("\\s").length;
        this.sentenceCount = data.split("[.!?]\\s[A-Z]").length;
        calculateSyllables();
        calculateScore();
        calculateAgeRange();
    }

    abstract void calculateScore();

    public void printFullStats() {
        printDataStats();
        printScoreAgeStats();
    }

    public void printDataStats() {
        System.out.println("Words: " + wordCount);
        System.out.println("Sentences: " + sentenceCount);
        System.out.println("Characters: " + characterCount);
        System.out.println("Syllables: " + syllableCount);
        System.out.println("Polysyllables: " + polysyllableCount);
        System.out.println();
    }

    public void printScoreAgeStats() {
        System.out.printf("%s: %.2f (about %s year olds)%n", METHOD_NAME, score, getAge());
    }

    public int getAge() {
        return ageRange.contains("+") ? 24 : Integer.parseInt(ageRange.split("-")[1]);
    }

    protected void calculateAgeRange() {
        int scoreInt = (int) Math.round(score);
        switch (scoreInt) {
            case 2:
                this.ageRange = "6-7";
                break;
            case 3:
                this.ageRange = "7-9";
                break;
            case 13:
                this.ageRange = "18-24";
                break;
            case 14:
                this.ageRange = "24+";
                break;
            default:
                this.ageRange = String.format("%d-%d", scoreInt + 5, scoreInt + 6);
        }
    }

    protected void calculateSyllables() {
        syllableCount = 0;
        polysyllableCount = 0;
        String[] words = data.split("\\s");
        for (String word : words) {
            int syllables = syllableCounter(word);
            syllableCount += syllables;
            if (syllables > 2) {
                polysyllableCount++;
            }
        }
    }

    protected int syllableCounter(String word) {
        // 1. Count the number of vowels in the word.
        int vowelCounter = 0;
        for (int i = 1; i < word.length(); i++) {
            boolean previousIsVowel = Character.toString(word.charAt(i - 1)).matches("[aeiouyAEIOUY]");
            boolean currentIsVowel = Character.toString(word.charAt(i)).matches("[aeiouyAEIOUY]");
            // 2. Do not count double-vowels (for example, "rain" has 2 vowels but is only 1 syllable)
            if (previousIsVowel && !currentIsVowel) {
                vowelCounter++;
            }
            // 3. If the last letter in the word is 'e' do not count it as a vowel (for example, "side" is 1 syllable)
            if (i == word.length() - 1) {
                if (currentIsVowel) {
                    vowelCounter++;
                }
                if (!previousIsVowel && word.charAt(i) == 'e') {
                    vowelCounter--;
                }
            }
        }
        // 4. If at the end it turns out that the word contains 0 vowels, then consider this word as 1-syllable.
        if (vowelCounter == 0) {
            return 1;
        }
        return vowelCounter;
    }

}

class AutomatedReadability extends ReadabilityScore {
    
    public AutomatedReadability(String data) {
        super(data);
        this.METHOD_NAME = "Automated Readability Index";
    }

    @Override
    protected void calculateScore() {
        score = 4.71 * (characterCount / (double) wordCount) + 0.5 * (wordCount / (double) sentenceCount) - 21.43;
    }
}

class FleschKincaid extends ReadabilityScore {

    public FleschKincaid(String data) {
        super(data);
        this.METHOD_NAME = "Flesch-Kincaid readability tests";
    }

    @Override
    public void calculateScore() {
        this.score = 0.39 * ((double) wordCount / (double) sentenceCount) + 11.8d * ((double) syllableCount / (double) wordCount) - 15.59d;
    }
}

class Smog extends ReadabilityScore {
    
    public Smog(String data) {
        super(data);
        this.METHOD_NAME = "Simple Measure of Gobbledygook";
    }

    @Override
    public void calculateScore() {
        this.score = 1.043 * Math.sqrt(polysyllableCount * (30 / (double) sentenceCount)) + 3.1291;
    }
}

class ColemanLiau extends ReadabilityScore {
    
    public ColemanLiau(String data) {
        super(data);
        this.METHOD_NAME = "Coleman-Liau index";
    }

    @Override
    public void calculateScore() {
        double averageCharsPer100 = characterCount / (double) wordCount * 100.0;
        double averageSentencesPer100 = sentenceCount / (double) wordCount * 100.0;
        this.score = 0.0588 * averageCharsPer100 - 0.296 * averageSentencesPer100 - 15.8;
    }
}

class ReadabilityScoreFactory {

    public static void applyAlgorithm(String selection, String data) {
        ReadabilityScore ARI = new AutomatedReadability(data);
        ReadabilityScore FK = new FleschKincaid(data);
        ReadabilityScore SMOG = new Smog(data);
        ReadabilityScore CL = new ColemanLiau(data);
        switch (selection) {
            case "ARI":
                ARI.printFullStats();
                break;
            case "FK":
                FK.printFullStats();
                break;
            case "SMOG":
                SMOG.printFullStats();
                break;
            case "CL":
                CL.printFullStats();
                break;
            case "all":
                ARI.printDataStats();
                ARI.printScoreAgeStats();
                FK.printScoreAgeStats();
                SMOG.printScoreAgeStats();
                CL.printScoreAgeStats();
                double averageAge = (ARI.getAge() + FK.getAge() + SMOG.getAge() + CL.getAge()) / 4.0;
                System.out.printf("%nThis text should be understood in average by %.2f year olds.%n", averageAge);
                break;
            default:
                System.out.println("Invalid selection...");
        }
    }
}
