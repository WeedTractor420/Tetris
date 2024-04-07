//Main Game Class representing logic of the game
//Creates and maintains GameBoard
//Keeping track of live cycle of every Shape that is added to board
//Keeping track and updating score
//Generating Shape queue
//Implementing player actions as rotations of the shape and its movement
package sk.tuke.gamestudio.game.Core;

import lombok.Getter;
import lombok.Setter;

public class Game {
    private final GameBoard gameBoard;
    @Getter
    private Shape currentShape;
    @Getter
    private int currentShapeRow;
    @Getter
    private int currentShapeCol;
    @Setter
    @Getter
    private int score;
    @Getter
    private GameState state;
    @Getter
    private final ShapeQueue shapeQueue;

    //In constructor, we create main playing board, first shape with information about it, and it adds first shape to the playing board
    public Game(GameBoard gameBoard) {
        this.state = GameState.PLAYING;
        this.gameBoard = gameBoard;
        this.score = 0;
        this.shapeQueue = new ShapeQueue();
        this.currentShape = shapeQueue.getNextShape();
        this.currentShapeCol = gameBoard.getColCount() / 2 - currentShape.getWidth() / 2;
        this.currentShapeRow = 0;
        gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
    }

    public void restartGame() {
        // Reset the game state
        this.state = GameState.PLAYING;
        this.score = 0;
        this.gameBoard.initializeBoard();

        // Reset the current shape and shape queue
        shapeQueue.reset();
        addNextShapeToTheGame();
    }

    //main GameTick(What happens before and after players actions)
    //it adds new shape everytime the current shape reaches bottom
    public void gameTick() {
        if (!canMoveDown() && !canMoveLeft() && !canMoveRight()) {
            currentShape.setState(ShapeState.AT_BOTTOM);
        }

        if(score > 10000){
            this.state = GameState.WON;
        }

        if (currentShape.getState() == ShapeState.AT_BOTTOM) {
            //updating score based on number of rows deleted
            //updating only whe player finishes its actions with current shape
            this.score += 100 * gameBoard.updateBoard();
            //setting GameState
            if (!addNextShapeToTheGame()) {
                this.state = GameState.FAILED;
            }
            currentShape.setState(ShapeState.FALLING);
        }
    }

    //Updating currentShape with new shape from the queue
    //Everytime we came to the end of queue we shuffle the current list and start from start
    //This way everytime we get random shape next, and we can print queue in UI
    private boolean addNextShapeToTheGame() {
        currentShape = shapeQueue.getNextShape();
        //everytime new shape is set on the top row in the middle row of the board
        currentShapeCol = gameBoard.getColCount() / 2 - currentShape.getWidth() / 2;
        currentShapeRow = 0;
        if (gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow, currentShapeCol)) {
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShape.setState(ShapeState.FALLING);
            return true;
        }
        return false;
    }

    //getter and setter methods for variables

    public GameBoard getPlayingBoard() {
        return gameBoard;
    }

    //check if current shape can be moved down
    private boolean canMoveDown() {
        gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
        boolean canMove = gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow + 1, currentShapeCol);
        gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        return canMove;
    }

    //check if current shape can be moved right
    private boolean canMoveRight() {
        gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
        boolean canMove = gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow, currentShapeCol + 1);
        gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        return canMove;
    }

    //check if current shape can be moved left
    private boolean canMoveLeft() {
        gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
        boolean canMove = gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow, currentShapeCol - 1);
        gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        return canMove;
    }

    //Player actions
    public void moveCurrentShapeDown() {
        //check if we can move if not we change state of shape to AT_BOTTOM
        if (canMoveDown()) {
            gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShapeRow++;
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        } else {
            //Once Shape state changes to AT_BOTTOM this shape is no longer accessible and is replaced by new one
            currentShape.setState(ShapeState.AT_BOTTOM);
        }
    }

    public void moveCurrentShape(boolean moveRight) {
        // Determine the direction of the movement
        int direction = moveRight ? 1 : -1;

        // Attempt to move in the desired direction
        if ((moveRight && canMoveRight()) || (!moveRight && canMoveLeft())) {
            gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
            currentShapeCol += direction; // Move right if direction is 1, left if -1
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        } else {
            // If movement in the desired direction is not possible, move the shape down
            moveCurrentShapeDown();
        }
    }

    //rotation actions true = rotateRight false = rotateLeft;
    public void rotateCurrentShape(boolean clockwise) {
        gameBoard.deleteShapeFromTheBoard(currentShape, currentShapeRow, currentShapeCol);
        currentShape.rotateShape(clockwise);
        if (gameBoard.canPlaceShapeOnBoard(currentShape, currentShapeRow, currentShapeCol)) {
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
        } else {
            currentShape.rotateShape(!clockwise);
            gameBoard.addShapeToBoard(currentShape, currentShapeRow, currentShapeCol);
            moveCurrentShapeDown();
        }
    }
}
