package flashcards;

/**
 * A flashcard consists of a term and its definition.
 * It also keeps track how many times the user gave
 * a wrong definition. Attributes and methods are self-explanatory.
 */
public class Flashcard {

    String term;
    String definition;
    int errorCount;

    public Flashcard(String term, String definition, int errorCount) {
        this.term = term;
        this.definition = definition;
        this.errorCount = errorCount;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void resetErrorCount() {
        this.errorCount = 0;
    }

    public void increaseErrorCount() {
        this.errorCount++;
    }
}
