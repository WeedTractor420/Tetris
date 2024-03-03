//Class handles UI and handles input from user
package ConsoleUI;
import Core.Game;
import Core.GameState;
import Core.ShapeState;
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
    public void printHeader() {
        System.out.println("Welcome to Tetris!");
        System.out.println("Press 'q' to quit the game at any time.");
        System.out.println("Core.Game State: " + game.getState());
        System.out.println("Score: " + game.getScore());
    }
    public void printBody() {
        for (int i = 0; i < game.getPlayingBoard().getRowCount(); i++) {
            for (int j = 0; j < game.getPlayingBoard().getColCount(); j++) System.out.print(game.getPlayingBoard().getBoard()[i][j] + " ");
            System.out.println();
        }
    }
}
