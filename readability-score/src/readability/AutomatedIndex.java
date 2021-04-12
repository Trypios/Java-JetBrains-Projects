package readability;

/**
 * Automated Readability index.
 * A readability test that measures readability score
 * by character count, word count and sentence count.
 */
public class AutomatedIndex extends ReadabilityScore {

    public AutomatedIndex(String data) {
        super(data);
        this.METHOD_NAME = "Automated Readability Index";
    }

    /**
     * Calculate the readability score by counts of characters, words and sentences.
     * Save result to the score variable.
     */
    @Override
    protected void calculateScore() {
        score = 4.71 * (characterCount / (double) wordCount) + 0.5 * (wordCount / (double) sentenceCount) - 21.43;
    }
}
