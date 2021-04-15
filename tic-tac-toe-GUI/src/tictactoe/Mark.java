package tictactoe;

public enum Mark {
    X,
    O,
    EMPTY;

    @Override
    public String toString() {
        if (this.equals(EMPTY)) {
            return " ";
        }
        return super.toString();
    }
}
