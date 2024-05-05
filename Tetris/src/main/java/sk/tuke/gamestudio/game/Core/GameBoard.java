package sk.tuke.gamestudio.game.Core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

//Class represents playing board and actions with board (adding and deleting Shapes)
@Getter
public class GameBoard {
    private final int rowCount;
    private final int colCount;
    //some getters
    private final Tile[][] board;

    public GameBoard(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.board = new Tile[rowCount][colCount];
        initializeBoard();
    }

    public Tile getTile(int row, int col){ return board[row][col]; }

    //just initialize borders of the playing board
    public void initializeBoard() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                if (r == rowCount - 1) {
                    board[r][c] = new Tile('_', true, false);
                } else if (c == 0 || c == colCount - 1) {
                    board[r][c] = new Tile('|', true, false);
                } else {
                    board[r][c] = new Tile(' ', false, false);
                }
            }
        }
    }

    //adding shape to the board on specific location on the board
    public void addShapeToBoard(Shape shape, int row, int col) {
        for (int r = 0; r < shape.getHeight(); r++) {
            for (int c = 0; c < shape.getWidth(); c++) {
                if (shape.getBlock()[r][c].isOccupied()) {
                    board[row + r][col + c].setSymbol(shape.getBlock()[r][c].getSymbol());
                    board[row + r][col + c].setOccupied(true);
                }
            }
        }
    }

    //check function for placing shape on specific location on the board
    public boolean canPlaceShapeOnBoard(Shape shape, int row, int col) {
        for (int r = 0; r < shape.getHeight(); r++) {
            for (int c = 0; c < shape.getWidth(); c++) {
                if(!isWithinBoard(row + r, col + c)){
                    return false;
                }
                if (board[row + r][col + c].isOccupied() && shape.getBlock()[r][c].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    //deleting shape from specific location on the board
    public void deleteShapeFromTheBoard(Shape shape, int row, int col) {
        for (int r = 0; r < shape.getHeight(); r++) {
            for (int c = 0; c < shape.getWidth(); c++) {
                if (shape.getBlock()[r][c].isOccupied()){
                    board[row + r][col + c].setSymbol(' ');
                    board[row + r][col + c].setOccupied(false);
                }
            }
        }
    }

    public List<Integer> updateBoard() {
        int completeRowsCount = 0;
        List<Integer> clearedRows = new ArrayList<>();
        int lastRowDeleted = -1; // Initialize to -1 to handle the case where no rows are deleted

        // Step 1: Identify complete rows and count them
        for (int r = 0; r < rowCount - 1; r++) {
            if (isRowComplete(r)) {
                completeRowsCount++;
                lastRowDeleted = r;
            }
        }

        // Step 2: Delete complete rows and move upper tiles down
        if (completeRowsCount > 0) {
            for (int r = lastRowDeleted; r >= 0; r--) {
                if (isRowComplete(r)) {
                    clearRow(r);
                    clearedRows.add(r);
                } else {
                    moveRowDown(r, completeRowsCount);
                }
            }
        }

        return clearedRows;
    }

    // Helper method to check if a row is complete
    private boolean isRowComplete(int row) {
        for (int c = 1; c < colCount - 1; c++) {
            if (!board[row][c].isOccupied()) {
                return false; // Empty cell found, row is not complete
            }
        }
        return true;
    }

    // Helper method to clear a complete row
    private void clearRow(int row) {
        for (int c = 1; c < colCount - 1; c++) {
            board[row][c].setSymbol(' ');
            board[row][c].setOccupied(false);
        }
    }

    // Helper method to move a row down by a certain offset
    private void moveRowDown(int row, int offset) {
        for (int c = 1; c < colCount - 1; c++) {
            if (board[row][c].isOccupied()) {
                board[row + offset][c].setSymbol(board[row][c].getSymbol());
                board[row + offset][c].setOccupied(true);
                board[row][c].setSymbol(' ');
                board[row][c].setOccupied(false);
            }
        }
    }

    public void updateWithShadow(Shape shape, int shadowRow, int shadowCol) {
        // Clear the previous shadow
        clearShadow();

        // Calculate the drop distance for the shadow (how far down it would go)
        int dropDistance = calculateDropDistance(shape, shadowRow, shadowCol);

        // Iterate over the shape's block to add the shadow to the board representation
        Tile[][] shapeConfiguration = shape.getBlock();
        for (int row = 0; row < shapeConfiguration.length; row++) {
            for (int col = 0; col < shapeConfiguration[row].length; col++) {
                if (shapeConfiguration[row][col].isOccupied()) {
                    // Ensure the shadow position is within board boundaries
                    int shadowRowPosition = shadowRow + row + dropDistance;
                    int shadowColPosition = shadowCol + col;
                    if (isWithinBoard(shadowRowPosition, shadowColPosition)) {
                        // Set the corresponding board cell to SHADOW
                        board[shadowRowPosition][shadowColPosition].setShadow(true);
                    }
                }
            }
        }
    }

    private int calculateDropDistance(Shape shape, int shadowRow, int shadowCol) {
        int dropDistance = shape.height + 1;
        while (canPlaceShapeOnBoard(shape, shadowRow + dropDistance, shadowCol)) {
            dropDistance++;
        }
        // Subtract one to get the position just before collision
        return dropDistance - 1;
    }

    private boolean isWithinBoard(int row, int col) {
        // Check if the specified row and column are within the board boundaries
        return row >= 0 && row < this.rowCount && col >= 0 && col < this.colCount;
    }

    private void clearShadow() {
        // Clear the shadow state for all tiles on the board
        for (Tile[] row : getBoard()) {
            for (Tile tile : row) {
                tile.setShadow(false);
            }
        }
    }

}
