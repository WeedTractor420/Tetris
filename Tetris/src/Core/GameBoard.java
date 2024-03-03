//Class represents playing board and actions with board (adding and deleting Shapes)
package Core;
public class GameBoard {
    private final int rowCount;
    private final int colCount;
    private final char[][] board;

    //dimensions are set by rowCount and colCount, board is represented by char[rowCount][colCount] variable
    public GameBoard(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.board = new char[rowCount][colCount];
        initializeBoard();
    }

    //some getters
    public char[][] getBoard(){
        return board;
    }
    public int getColCount() {
        return colCount;
    }
    public int getRowCount() {
        return rowCount;
    }

    //just initialize borders of the playing board
    private void initializeBoard() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++){
                if(i == rowCount - 1) {
                    board[i][j] = '_';
                } else if(j == 0 || j == colCount - 1) {
                    board[i][j] = '|';
                } else {
                    board[i][j] = ' ';
                }
            }
        }
    }

    //adding shape to the board on specific location on the board
    public void addShapeToBoard(Shape shape, int row, int col) {
        for (int i = 0; i < shape.height; i++) {
            for (int j = 0; j < shape.width; j++) {
                if (shape.block[i][j] != ' ') {
                    board[row + i][col + j] = shape.block[i][j];
                }
            }
        }
        shape.setState(ShapeState.INITIALIZED);
    }

    //check function for placing shape on specific location on the board
    public boolean canPlaceShapeOnBoard(Shape shape, int row, int col){
        // Check if any of the cells in the Core.Shape object overlap with cells on the game board that are already occupied
        for (int i = 0; i < shape.height; i++) {
            for (int j = 0; j < shape.width; j++) {
                if (shape.block[i][j] != ' ' && board[row + i][col + j] != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    //deleting shape from specific location on the board
    public void deleteShapeFromTheBoard(Shape shape, int row, int col){
        for (int i = 0; i < shape.height; i++) {
            for (int j = 0; j < shape.width; j++) {
                if (shape.block[i][j] != ' ') {
                    board[row + i][col + j] = ' ';
                }
            }
        }
        shape.setState(ShapeState.NOT_INITIALIZED);
    }

    //methods for updating board
    //Check and return true if certain row is complete
    private boolean isRowComplete(int row) {
        int count = 0;
        for (int j = 0; j < colCount; j++) {
            if(board[row][j] == '_'){
                continue;
            }
            if (board[row][j] != ' ') {
                count++;
            }
        }
        return count == colCount;
    }

    //Updates board by deleting full rows and moving down upper tiles
    //we return num of full rows for calculating score
    public int updateBoard() {
        boolean[] rowComplete = new boolean[rowCount];
        int completeRowsCount = 0;
        int lastRowDeleted = 0;

        // Check witch rows are complete
        for (int j = 0; j < rowCount; j++) {
            if(isRowComplete(j)) {
                rowComplete[j] = true;
                completeRowsCount++;
            }
        }

        for (int j = rowCount - 2; j >= 0; j--) {
            // Delete Complete rows
            if (rowComplete[j]) {
                for (int i = 1; i < colCount-1; i++) {
                    if (board[j][i] != ' ') {
                        board[j][i] = ' ';
                    }
                    lastRowDeleted = j;
                }
            }
        }

        // Move upper tiles from last deleted row down
        if (completeRowsCount != 0) {
            for (int j = lastRowDeleted - 1; j >= 0; j--) {
                for (int i = 1; i < colCount - 1; i++) {
                    if (board[j][i] != ' ') {
                        board[j + completeRowsCount][i] = board[j][i];
                        board[j][i] = ' ';
                    }
                }
            }
        }

        return completeRowsCount;
    }

}
