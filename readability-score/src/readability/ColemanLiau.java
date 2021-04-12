package readability;

/**
 * Coleman-Liau index.
 * A readability test that measures readability score
 * by character count instead of syllables per word.
 */
public class ColemanLiau extends ReadabilityScore {

    public ColemanLiau(String data) {
        super(data);
        this.METHOD_NAME = "Coleman-Liau index";
    }

    /**
     * Calculate the readability score by count of characters.
     * Save result to the score variable.
     */
    @Override
    public void calculateScore() {
        double averageCharsPer100 = characterCount / (double) wordCount * 100.0;
        double averageSentencesPer100 = sentenceCount / (double) wordCount * 100.0;
        this.score = 0.0588 * averageCharsPer100 - 0.296 * averageSentencesPer100 - 15.8;
    }
}
