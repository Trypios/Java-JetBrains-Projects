package life;

import javax.swing.*;
import java.awt.*;

/**
 * VIEW of the Universe.
 * Main panel, displays all the Universe's cells on a 2D grid.
 */
public class UniversePanel extends JPanel {

    private CellPanel[][] cellArray;
    private Color color;

    UniversePanel() {
        super();
        this.color = Color.BLACK;
    }

    /**
     * Fill this panel with CellPanels each representing a Universe cell.
     * @param height rows of this panel.
     * @param width columns of this panel.
     */
    void initialize(int height, int width) {

        this.setLayout(new GridLayout(height, width, 1, 1));
        this.cellArray = new CellPanel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cellArray[i][j] = new CellPanel();
                cellArray[i][j].setBackground(Color.BLACK);
                this.add(cellArray[i][j]);
            }
        }
    }

    /**
     * Update the state of each Cell of the 2D cellArray.
     * @param matrix the grid of the represented Universe.
     */
    void updateCellArray(Cell[][] matrix) {
        if (cellArray != null) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (cellArray[i][j].isAlive() != (matrix[i][j].getState() == 'O')) {
                        cellArray[i][j].setAlive(matrix[i][j].getState() == 'O');
                        cellArray[i][j].setBackground(color);
                    }
                }
            }
        } else {
            initialize(matrix.length, matrix[0].length);
            updateCellArray(matrix);
        }
    }

    /**
     * Update the color of each Cell of the 2D cellArray.
     * @param color a color for alive cells.
     */
    void updateColor(Color color) {
        this.color = color;
        for (CellPanel[] row : cellArray) {
            for (CellPanel cell: row) {
                cell.setBackground(color);
            }
        }
    }
}