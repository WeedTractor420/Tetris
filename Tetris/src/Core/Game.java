//Main Game Class representing logic of the game
//Creates and maintains GameBoard
//Keeping track of live cycle of every Shape that is added to board
//Keeping track and updating score
//Generating Shape queue
//Implementing player actions as rotations of the shape and its movement
package Core;
import java.util.*;

public class Game{
    private final GameBoard gameBoard;
    private Shape currentShape;
    private int currentShapeRow;
    private int currentShapeCol;
    private int score;
    private GameState state = GameState.PLAYING;
    private final List<Shape> shapeQueue;
    private int shapeQueueIndex;

    //In constructor, we create main playing board, first shape with information about it, and it adds first shape to the playing board
    public Game(){
        this.gameBoard = new GameBoard(18, 10);
        this.score = 0;
        this.shapeQueue = new ArrayList<>();
        generateShapeQueue();
        this.shapeQueueIndex = 0;
        this.currentShape = shapeQueue.get(shapeQueueIndex);
        this.currentShapeCol = gameBoard.getColCount()/2 - currentShape.width/2;
        this.currentShapeRow = 0;
        gameBoard.addShapeToBoard(currentShape,currentShapeRow,currentShapeCol);
    }

    //main GameTick(What happens between players actions)
    //it adds new shape everytime the current shape reaches bottom
    public void gameTick(){
        if (Objects.requireNonNull(currentShape.getState()) == ShapeState.AT_BOTTOM) {
            //updating score based on number of rows deleted
            //updating only whe player finishes its actions with current shape\
            this.score += 100 * gameBoard.updateBoard();
            //setting GameState
            if(!addNextShapeToTheGame()){
                setState(GameState.FAILED);
            }else if(score > 100000){
                setState(GameState.WON);
            }
        }
    }

    //Generating new random shape
    private Shape generateRandomShape() {
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
    //generating ShapeQueue
    private void generateShapeQueue() {
        for (int i = 0; i < 7; i++) {
            Shape shape = generateRandomShape();
            shapeQueue.add(shape);
        }
    }
    //Updating currentShape with new shape from the queue
    //Everytime we came to the end of queue we shuffle the current list and start from start
    //This way everytime we get random shape next, and we can print queue in UI
    private boolean addNextShapeToTheGame(){
        shapeQueueIndex++;
        if(shapeQueueIndex > 6){
            Collections.shuffle(shapeQueue);
            shapeQueueIndex = 0;
        }
        currentShape = shapeQueue.get(shapeQueueIndex);
        //everytime new shape is set on the top row in the middle row of the board
        currentShapeCol = gameBoard.getColCount()/2 - currentShape.width/2;
        currentShapeRow = 0;
        if(gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow, currentShapeCol)){
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
            return true;
        }
        setState(GameState.FAILED);
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
    public List<Shape> getShapeQueue(){ return shapeQueue; }
    public int getShapeQueueIndex(){ return shapeQueueIndex; }


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
            //Once Shape state changes to AT_BOTTOM this shape is no longer accessible and is replaced by new one
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

    //rotation actions true = rotateRight false = rotateLeft;
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

        //try to place rotated shape if not possible than moveDownCurrentShape
        rotatedShape.setBlock(newBlock);
        if(gameBoard.canPlaceShapeOnBoard(rotatedShape, currentShapeRow, currentShapeCol)) {
            currentShape = rotatedShape;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        }else{
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
            moveCurrentShapeDown();
        }
    }
}
