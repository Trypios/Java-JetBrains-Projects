package readability;

/**
 * Simple Measure of Gobbledygook, aka 'SMOG index'.
 * A readability test that measures readability score
 * by sentence count and polysyllable count.
 */
public class Smog extends ReadabilityScore {

    public Smog(String data) {
        super(data);
        this.METHOD_NAME = "Simple Measure of Gobbledygook (SMOG index)";
    }

    /**
     * Calculate the readability score by count sentences and polysyllables.
     * Save result to the score variable.
     */
    @Override
    public void calculateScore() {
        this.score = 1.043 * Math.sqrt(polysyllableCount * (30 / (double) sentenceCount)) + 3.1291;
    }
}
