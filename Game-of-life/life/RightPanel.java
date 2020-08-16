package life;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private GameGridPanel[][] cellArray;
    private Color color;

    public RightPanel() {
        super();
        this.color = Color.BLACK;
    }

    public void initialize(int height, int width) {
        this.setLayout(new GridLayout(height, width, 1, 1));
        this.cellArray = new GameGridPanel[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cellArray[i][j] = new GameGridPanel();
                cellArray[i][j].setBackground(Color.BLACK);
                this.add(cellArray[i][j]);
            }
        }
    }

    public void updateCellArray(Cell[][] board) {
        if (cellArray != null) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (cellArray[i][j].isAlive() != (board[i][j].getState() == 'O')) {
                        cellArray[i][j].setAlive(board[i][j].getState() == 'O');
                        cellArray[i][j].setBackground(color);
                    }
                }
            }
        } else {
            initialize(board.length, board[0].length);
            updateCellArray(board);
        }
    }

    public void updateColor(Color color) {
        this.color = color;
        for (GameGridPanel[] row : cellArray) {
            for (GameGridPanel cell: row) {
                cell.setBackground(color);
            }
        }
    }
}