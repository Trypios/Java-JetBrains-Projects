package readability;

/**
 * Flesch-Kincaid readability test.
 * A readability test that measures readability score
 * by word length and sentence length
 */
public class FleschKincaid extends ReadabilityScore {

    public FleschKincaid(String data) {
        super(data);
        this.METHOD_NAME = "Flesch-Kincaid readability test";
    }

    /**
     * Calculate the readability score by word length and sentence length.
     * Save result to the score variable.
     */
    @Override
    public void calculateScore() {
        this.score = 0.39 * ((double) wordCount / (double) sentenceCount) +
                11.8d * ((double) syllableCount / (double) wordCount) - 15.59d;
    }
}
