//Class handles UI and handles input from user
package ConsoleUI;
import Core.Game;
import Core.GameState;
import Core.Shape;
import Core.ShapeState;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ConsoleUI {
    private final Game game;

    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.game = new Game();
    }
    public void play(){
        do{
            game.gameTick();
            printHeader();
            printBody();
            handleInput();
            //if we cant move anywhere after player action we can set Core.ShapeState to AT_BOTTOM
            //Once Shape state changes to AT_BOTTOM this shape is no longer accessible and is replaced by new one
            if (game.checkMoveDownCollision() && game.checkMoveLeftCollision() && game.checkMoveRightCollision()){
                game.getCurrentShape().setState(ShapeState.AT_BOTTOM);
            }
        }while(game.getState() == GameState.PLAYING);
    }

    public void handleInput() {
        System.out.println("Enter command (Q - quit, S - moveDown, D - moveRight, A - moveLeft, R - rotateRight, L - rotateLeft): ");
        String input = scanner.nextLine();
        Pattern pattern = Pattern.compile("[QSADRLqsadrl]");
        if (!pattern.matcher(input).matches()) {
            System.out.println("Invalid command, please enter a valid command.");
            handleInput();
            return;
        }
        switch (input.toUpperCase()) {
            case "Q":
                System.exit(0);
                break;
            case "S":
                game.moveCurrentShapeDown();
                break;
            case "A":
                game.moveCurrentShapeLeft();
                break;
            case "D":
                game.moveCurrentShapeRight();
                break;
            case "R":
                game.rotateCurrentShape(true);
                break;
            case "L":
                game.rotateCurrentShape(false);
                break;
            default:
                break;
        }
    }

    //Methods for printing UI
    private void printHeader() {
        System.out.println("Welcome to Tetris!");
        System.out.println("Press 'q' to quit the game at any time.");
        printQueue(game.getShapeQueue(), game.getShapeQueueIndex());
        System.out.println("Core.Game State: " + game.getState());
        System.out.println("Score: " + game.getScore());
    }
    private void printBody() {
        for (int i = 0; i < game.getPlayingBoard().getRowCount(); i++) {
            for (int j = 0; j < game.getPlayingBoard().getColCount(); j++) System.out.print(game.getPlayingBoard().getBoard()[i][j] + " ");
            System.out.println();
        }
    }
    private void printQueue(List<Shape> shapeQueue, int currentQueueIndex) {
        int maxRows = 0;

        // Find the maximum number of rows in the shapes in the current queue
        for (int i = currentQueueIndex; i < Math.min(currentQueueIndex + 3, shapeQueue.size()); i++) {
            maxRows = Math.max(maxRows, shapeQueue.get(i).getHeight());
        }
        System.out.println("ShapeQueue: ");
        // Print each row of each shape side by side
        for (int row = 0; row < maxRows; row++) {
            for (int i = currentQueueIndex; i < Math.min(currentQueueIndex + 3, shapeQueue.size()); i++) {
                Shape currentShape = shapeQueue.get(i);

                // Print the current row of the current shape
                if (row < currentShape.getHeight()) {
                    for (int k = 0; k < currentShape.getWidth(); k++) {
                        System.out.print(currentShape.getBlock()[row][k]);
                    }
                } else {
                    // Print empty spaces if the row is beyond the height of the current shape
                    for (int k = 0; k < currentShape.getWidth(); k++) {
                        System.out.print(' ');
                    }
                }

                // Add spacing between shapes
                if (i < Math.min(currentQueueIndex + 3, shapeQueue.size()) - 1) {
                    System.out.print("   ");
                }
            }
            System.out.println(); // Move to the next row
        }
    }
}
