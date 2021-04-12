package readability;

/**
 * Class responsible for calculating the readability score of a text by analysis
 * and then determine the average lower-bound age to understand this text.
 * Contains generic methods most readability tests use to calculate the score.
 *
 * Readability is the ease with which a reader can understand a written text.
 * In natural language, the readability of text depends on its content
 * (the complexity of its vocabulary and syntax)
 */
public abstract class ReadabilityScore {

    protected String METHOD_NAME;
    protected String data;
    protected int characterCount;
    protected int wordCount;
    protected int sentenceCount;
    protected int syllableCount;
    protected int polysyllableCount;
    protected double score;
    protected String ageRange;

    /**
     * Constructor is package-private. Preferably use the applyAlgorithm().
     * @param data Text meant for readability score analysis.
     */
    ReadabilityScore(String data) {
        this.data = data;
        this.characterCount = data.replaceAll("\\s","").split("").length;
        this.wordCount = data.split("\\s").length;
        this.sentenceCount = data.split("[.!?]\\s[A-Z]").length;
        calculateSyllables();
        calculateScore();
        calculateAgeRange();
    }

    /**
     * Various concrete Readability tests differ and should
     * implement this abstract method according to their measurements.
     */
    abstract void calculateScore();

    /**
     * Display the data and age stats.
     * Run method after data analysis.
     */
    private void printFullStats() {

        printDataStats();
        printScoreAgeStats();
    }

    /**
     * Display the data stats.
     * Run method after data analysis.
     */
    private void printDataStats() {
        System.out.println("Words: " + wordCount);
        System.out.println("Sentences: " + sentenceCount);
        System.out.println("Characters: " + characterCount);
        System.out.println("Syllables: " + syllableCount);
        System.out.println("Polysyllables: " + polysyllableCount);
        System.out.println();
    }

    /**
     * Display the age stats.
     * Run method after data analysis.
     */
    private void printScoreAgeStats() {

        System.out.printf("%s: %.2f (%s+ year olds)%n", METHOD_NAME, score, getAge());
    }

    /**
     * Modified getter.
     * Return the lower-bound age saved in ageRange.
     * @return an age as number
     */
    private int getAge() {

        return ageRange.contains("+") ? 24 : Integer.parseInt(ageRange.split("-")[0]);
    }

    /**
     * Calculate the average lower-bound age being able to understand the given data.
     */
    private void calculateAgeRange() {

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

    /**
     * Analyze data for
     *    count of syllables and
     *    count of polysyllables.
     */
    private void calculateSyllables() {

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

    /**
     * Helper of calculateSyllables().
     * Count how many syllables a word consists of.
     * @param word a word to be analyzed.
     * @return a number (count of syllables).
     */
    private int syllableCounter(String word) {

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

    /**
     * Run this method as a ReadabilityScore factory,
     * to display results of any of the four extending classes,
     * or all at once.
     * @param selection which readability test to use for analysis.
     * @param data the text to analyze.
     */
    public static void applyAlgorithm(String selection, String data) {

        ReadabilityScore ARI = new AutomatedIndex(data);
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
            case "ALL":
                ARI.printDataStats();
                ARI.printScoreAgeStats();
                FK.printScoreAgeStats();
                SMOG.printScoreAgeStats();
                CL.printScoreAgeStats();
                double averageAge = (ARI.getAge() + FK.getAge() + SMOG.getAge() + CL.getAge()) / 4.0;
                System.out.printf("%nThis text should be understood in average by %.1f year olds.%n", averageAge);
                break;
            default:
                System.out.println("Invalid selection...");
        }
    }
}
