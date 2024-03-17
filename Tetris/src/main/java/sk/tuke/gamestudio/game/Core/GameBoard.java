package main.java.sk.tuke.gamestudio.game.Core;

//Class represents playing board and actions with board (adding and deleting Shapes)
public class GameBoard {
    private final int rowCount;
    private final int colCount;
    private final Tile[][] board;

    public GameBoard(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.board = new Tile[rowCount][colCount];
        initializeBoard();
    }

    //some getters
    public Tile[][] getBoard() {
        return board;
    }
    public int getColCount() {
        return colCount;
    }
    public int getRowCount() {
        return rowCount;
    }

    //just initialize borders of the playing board
    public void initializeBoard() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                if (r == rowCount - 1) {
                    board[r][c] = new Tile('_', true);
                } else if (c == 0 || c == colCount - 1) {
                    board[r][c] = new Tile('|', true);
                } else {
                    board[r][c] = new Tile(' ', false);
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

    public int updateBoard() {
        int completeRowsCount = 0;
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
                } else {
                    moveRowDown(r, completeRowsCount);
                }
            }
        }

        return completeRowsCount;
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


}
