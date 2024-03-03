//Main Core.Game Class representing logic of the game
//Creates and maintains Core.GameBoard
//Keeping track of live cycle of every Core.Shape that is added to board
//Keeping track and updating score
package Core;
import java.util.Objects;
import java.util.Random;

public class Game{
    private final GameBoard gameBoard;
    private Shape currentShape;
    private int currentShapeRow;
    private int currentShapeCol;
    private int score;
    private GameState state = GameState.PLAYING;

    //In constructor, we create main playing board, first shape with information about it, and it adds first shape to the playing board
    public Game(){
        this.gameBoard = new GameBoard(10, 20);
        this.currentShape = generateRandomShape();
        this.score = 0;
        this.currentShapeCol = 1;
        this.currentShapeRow = 0;
        gameBoard.addShapeToBoard(currentShape,currentShapeRow,currentShapeCol);
    }


    //main GameTick(What happens between players actions)
    //it adds new shape everytime the current shape reaches bottom
    public void gameTick(){
        if (Objects.requireNonNull(currentShape.getState()) == ShapeState.AT_BOTTOM) {
            this.score += 100 * gameBoard.updateBoard();
            if(!generateNextShapeToTheGame()){
                setState(GameState.FAILED);
            }else if(score > 100000){
                setState(GameState.WON);
            }
        }
    }

    //Generating new random shape
    public Shape generateRandomShape() {
        int random = new Random().nextInt(7);
        return switch (random) {
            case 0 -> new OShape();
            case 1 -> new IShape();
            case 2 -> new ZShape();
            case 3 -> new LShape();
            case 4 -> new JShape();
            case 5 -> new SShape();
            case 6 -> new TShape();
            default -> null;
        };
    }

    //If new shape can be placed on first row than place it
    //Else set Core.GameState to Failed
    public boolean generateNextShapeToTheGame(){
        currentShape = generateRandomShape();
        for (int col = 0; col < gameBoard.getColCount(); col++) {
            if (gameBoard.canPlaceShapeOnBoard(currentShape, 0, col)) {
                gameBoard.addShapeToBoard(currentShape, 0, col);
                currentShapeRow = 0;
                currentShapeCol = col;
                return true;
            }
        }
        return false;
    }

    //getter and setter methods for variables
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Shape getCurrentShape(){
        return currentShape;
    }

    public GameBoard getPlayingBoard(){
        return gameBoard;
    }
    public int getScore(){
        return score;
    }


    //checking collision methods for movement
    public boolean checkMoveDownCollision(){
        for(int j = 0; j < currentShape.width; j++){
            if(gameBoard.getBoard()[currentShapeRow + currentShape.height][currentShapeCol + j] != ' ' && currentShape.block[currentShape.height - 1][j] != ' ') {
                return true;
            }
        }
        return false;
    }
    public boolean checkMoveRightCollision(){
        for(int j = 0; j < currentShape.height; j++){
            if(gameBoard.getBoard()[currentShapeRow + j][currentShapeCol + currentShape.width] != ' ' && currentShape.block[j][currentShape.width - 1] != ' ') {
                return true;
            }
        }
        return false;
    }
    public boolean checkMoveLeftCollision(){
        for(int j = 0; j < currentShape.height; j++){
            if(gameBoard.getBoard()[currentShapeRow + j][currentShapeCol - 1] != ' ' && currentShape.block[j][0] != ' ') {
                return true;
            }
        }
        return false;
    }

    //Player actions
    public void moveCurrentShapeDown(){
        //check if we can move if not we change state of shape to AT_BOTTOM
        if(!checkMoveDownCollision()){
            gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShapeRow++;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }else{
            currentShape.setState(ShapeState.AT_BOTTOM);
        }
    }
    public void moveCurrentShapeRight(){
        //check if we can move if not we move shape down instead of right
        if(!checkMoveRightCollision()){
            gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShapeCol++;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }else{
            moveCurrentShapeDown();
        }
    }
    public void moveCurrentShapeLeft(){
        //check if we can move if not we move shape down instead of left
        if(!checkMoveLeftCollision()){
            gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShapeCol--;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }else{
            moveCurrentShapeDown();
        }
    }

    //rotation actions
    public void rotateCurrentShape(boolean orientation) {
        //delete current shape from the board
        gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);

        //changing height and width because of rotation
        int newHeight = currentShape.width;
        int newWidth = currentShape.height;

        // Create a new block to store the rotated shape
        Shape rotatedShape = currentShape;
        rotatedShape.setHeight(newHeight);
        rotatedShape.setWidth(newWidth);
        char[][] newBlock = new char[newHeight][newWidth];

        // Rotate the block 90 degrees to the right or left based on orientation
        // Performing Transpose
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if(!orientation) {
                    newBlock[i][j] = currentShape.block[j][newHeight - 1 - i];
                }else{
                    newBlock[i][j] = currentShape.block[newWidth - 1 - j][i];
                }
            }
        }

        //try to place rotated shape if not possible than place original shape on board
        rotatedShape.setBlock(newBlock);
        if(gameBoard.canPlaceShapeOnBoard(rotatedShape, currentShapeRow, currentShapeCol)) {
            currentShape = rotatedShape;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }else{
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }
    }

}
