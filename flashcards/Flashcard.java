package flashcards;

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

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int count) {
        this.errorCount = count;
    }

    public void increaseErrorCount() {
        this.errorCount++;
    }
}
