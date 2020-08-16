package life;

public class Cell {
/*
    MODEL
    =====
    A Cell has a certain position based on the Universe created in:
        rowPos = Universe's matrix X-axis position
        colPos = Universe's matrix Y-axis position
    It can either be alive (designated 'O') or dead (designated ' ')
    It can evolve, forming the next generation Universe, based on certain evolution rules
    relative to its neighboring cells' state. Hence we count alive and dead cells
*/

    private final int rowPos;
    private final int colPos;
    private char state;
    private int aliveNeighborsCount = -1;
    private int deadNeighborsCount = -1;

    public Cell(int rowPos, int colPos) {
        this.rowPos = rowPos;
        this.colPos = colPos;
    }

    public Cell(int rowPos, int colPos, char state) {
        this(rowPos, colPos);
        this.state = state;
    }

    public Cell copy() {
//        Returns a deep-copy of current Cell
        Cell newCell = new Cell(rowPos, colPos);
        newCell.setAliveNeighborsCount(aliveNeighborsCount);
        newCell.setDeadNeighborsCount(deadNeighborsCount);
        newCell.setState(state);
        return newCell;
    }

    public void setAliveNeighborsCount(int count) {
//        Sets the alive neighbors count (for deep-copy purposes)
        this.aliveNeighborsCount = count;
    }

    public void setDeadNeighborsCount(int count) {
//        Sets the dead neighbors count (for deep-copy purposes)
        this.deadNeighborsCount = count;
    }

    public char getState() {
//        Returns the current Cell's state
//        (Alive: 'O' or Dead: ' ')
        return state;
    }

    public void setState(char state) {
//        Sets the current Cell's state, for evolve() method
//        (Alive: 'O' or Dead: ' ')

        this.state = state;
    }

    public int getRowPos() {
//        Returns Cell's position based on its Universe X-axis
        return rowPos;
    }

    public int getColPos() {
//        Returns Cell's position based on its Universe Y-axis
        return colPos;
    }

    public void evolve() {
//        changes state of current Cell based on following evolution rules:

        if (state == 'O') {
//            1. An alive cell survives if has two or three alive neighbors
//            otherwise, it dies of boredom (<2) or overpopulation (>3)

            setState(aliveNeighborsCount == 2 || aliveNeighborsCount == 3 ? 'O' : ' ');
        } else {
//            2. A dead cell is reborn if it has exactly three alive neighbors

            setState(aliveNeighborsCount == 3 ? 'O' : ' ');
        }
    }
}