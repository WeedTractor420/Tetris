//Class handles UI and handles input from user
package sk.tuke.gamestudio.game.ConsoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.Entity.Comment;
import sk.tuke.gamestudio.Entity.Rating;
import sk.tuke.gamestudio.Entity.Score;
import sk.tuke.gamestudio.Service.CommentService;
import sk.tuke.gamestudio.Service.RatingException;
import sk.tuke.gamestudio.Service.RatingService;
import sk.tuke.gamestudio.Service.ScoreService;
import sk.tuke.gamestudio.game.Core.Game;
import sk.tuke.gamestudio.game.Core.GameState;
import sk.tuke.gamestudio.game.Core.Shape;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


public class ConsoleUI {
    private final Game game;
    private final Scanner scanner;
    private ScoreService scoreService;
    private RatingService ratingService;
    private CommentService commentService;
    private String userName;

    public ConsoleUI(Game game) {
        this.scanner = new Scanner(System.in);
        this.game = game;
        this.userName = null;
    }

    @Autowired
    public void setScoreService(ScoreService scoreService) {this.scoreService = scoreService;}
    @Autowired
    public void setRatingService(RatingService ratingService) { this.ratingService = ratingService; }
    @Autowired
    public void setCommentService(CommentService commentService){ this.commentService = commentService;}

    public void start() {
        if(userName == null) {
            System.out.print("Enter your username: ");
            userName = scanner.nextLine();
        }
        while(true) {
            printMenu();
            int choice = getUserChoice();
            handleUserChoice(choice, userName);
        }
    }

    private void printMenu() {
        System.out.println("Welcome to the game!");
        System.out.println("1. Start game");
        System.out.println("2. Rate Game");
        System.out.println("3. Comment Game");
        System.out.println("4. ScoreBoard");
        System.out.println("5. View Comments");
        System.out.println("6. View My Rating");
        System.out.println("7 View Average Rating");
        System.out.println("8. Quit");
        System.out.println("---------------------------------------");
    }

    private int getUserChoice() {
        System.out.print("Enter your choice: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid choice.");
            scanner.nextLine(); // consume the invalid input
            return getUserChoice(); // call the method recursively
        }
    }

    private void handleUserChoice(int choice, String userName) {
        switch (choice) {
            case 1:
                game.restartGame();
                play(userName);
                break;
            case 2:
                saveRating(userName);
                break;
            case 3:
                addComment(userName);
                break;
            case 4:
                printScores();
                break;
            case 5:
                printComments();
                break;
            case 6:
                printRating(userName);
                break;
            case 7:
                printAverageRating();
                break;
            case 8:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public boolean playAgain() {
        Pattern PATTERN_YES_NO = Pattern.compile("[YNyn]");
        System.out.println("---------------------------------------");
        System.out.println("Game over! Your score is: " + game.getScore());
        System.out.println("Do you want to restart the game (Y/N)?");
        System.out.println("---------------------------------------");
        String command = scanner.nextLine().trim().toLowerCase();

        if (!PATTERN_YES_NO.matcher(command).matches()) {
            System.out.println("Invalid input! Please enter Y or N.");
            return playAgain();
        }

        return command.equals("y");
    }

    public void play(String userName){
        do{
            game.gameTick();
            printHeader();
            printBody();
            handleInput(userName);
        }while(game.getState() == GameState.PLAYING);

        if (game.getState() == GameState.FAILED || game.getState() == GameState.WON) {
            saveScore(userName);
            if(playAgain()){
                System.out.println("Starting new Game");
                System.out.println("---------------------------------------");
                game.restartGame();
                play(userName);
            }else{
                System.out.println("Returning to menu");
                System.out.println("---------------------------------------");
                start();
            }
        }
    }

    public void handleInput(String userName) {
        System.out.println("Enter command (Q - quit, S - moveDown, D - moveRight, A - moveLeft, R - rotateRight, L - rotateLeft, M - Menu): ");
        String input = scanner.nextLine();
        Pattern pattern = Pattern.compile("[QSADRLMqsadrlm]");
        if (!pattern.matcher(input).matches()) {
            System.out.println("Invalid command, please enter a valid command.");
            handleInput(userName);
            return;
        }
        switch (input.toUpperCase()) {
            case "Q":
                saveScore(userName);
                System.exit(0);
                break;
            case "S":
                game.moveCurrentShapeDown();
                break;
            case "A":
                game.moveCurrentShape(false);
                break;
            case "D":
                game.moveCurrentShape(true);
                break;
            case "R":
                game.rotateCurrentShape(true);
                break;
            case "L":
                game.rotateCurrentShape(false);
                break;
            case "M":
                saveScore(userName);
                start();
            default:
                break;
        }
    }

    //Methods for printing UI
    private void printHeader() {
        System.out.println("Welcome to Tetris!");
        System.out.println("Press 'q' to quit the game at any time.");
        System.out.println("---------------------------------------");
        printQueue(game.getShapeQueue().getShapeQueue(), game.getShapeQueue().getIndex());
        System.out.println("---------------------------------------");
        System.out.println("Core.Game State: " + game.getState());
        System.out.println("Score: " + game.getScore());
        System.out.println("---------------------------------------");
    }
    private void printBody() {
        for (int i = 0; i < game.getPlayingBoard().getRowCount(); i++) {
            for (int j = 0; j < game.getPlayingBoard().getColCount(); j++)
                System.out.print(game.getPlayingBoard().getBoard()[i][j].getSymbol() + " ");
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
                        System.out.print(currentShape.getBlock()[row][k].getSymbol());
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

    private void printScores() {
        var scores = scoreService.getTopScores("Tetris");
        System.out.println("---------------------------------------------------------------");
        for (int i = 0; i < scores.size(); i++) {
            var score = scores.get(i);
            System.out.printf("%d. %s %d\n", i + 1, score.getPlayer(), score.getPoints());
        }
        System.out.println("---------------------------------------------------------------");

    }

    private void saveScore(String player) {
        scoreService.addScore(
                new Score(player, "Tetris", game.getScore(), new Date()));
    }

    public void saveRating(String player) {
        if (ratingService != null) {
            System.out.println("---------------------------------------");
            System.out.print("Enter your rating (1-5): ");
            try {
                int rating = scanner.nextInt();
                if (rating < 1 || rating > 5) {
                    System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
                    saveRating(player);
                } else {
                    Rating newRating = new Rating(player, "Tetris", rating, new Date());
                    ratingService.setRating(newRating);
                    System.out.println("Rating saved successfully.");
                    System.out.println("---------------------------------------");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid rating.");
                scanner.nextLine(); // consume the invalid input
                saveRating(player); // call the method recursively
            }
        } else {
            System.out.println("Score service is not initialized. Unable to save rating.");
        }
    }

    public void printRating(String player) {
        if (ratingService != null) {
            try {
                int rating = ratingService.getRating("Tetris", player);
                System.out.println("---------------------------------------");
                System.out.println("Rating for player " + player + ": " + rating);
                System.out.println("---------------------------------------");
            } catch (RatingException e) {
                System.out.println("Error retrieving rating: " + e.getMessage());
            }
        } else {
            System.out.println("Score service is not initialized. Unable to retrieve rating.");
        }
    }

    public void printAverageRating() {
        if (ratingService != null) {
            try {
                int rating = ratingService.getAverageRating("Tetris");
                System.out.println("---------------------------------------");
                System.out.println("Average rating for Tetris is " + rating);
                System.out.println("---------------------------------------");
            } catch (RatingException e) {
                System.out.println("Error retrieving rating: " + e.getMessage());
            }
        } else {
            System.out.println("Score service is not initialized. Unable to retrieve rating.");
        }
    }

    // Method to add a comment
    public void addComment(String player) {
        if (commentService != null) {
            System.out.println("---------------------------------------");
            System.out.print("Enter your comment: ");
            String commentText = scanner.nextLine();
            try {
                Comment comment = new Comment(player, "Tetris", commentText, new Date());
                commentService.addComment(comment);
                System.out.println("Comment added successfully.");
                System.out.println("---------------------------------------");
            } catch (Exception e) {
                System.out.println("Error adding comment: " + e.getMessage());
            }
        } else {
            System.out.println("Comment service is not initialized. Unable to add comment.");
        }
    }

    // Method to print comments
    public void printComments() {
        if (commentService != null) {
            try {
                List<Comment> comments = commentService.getComments("Tetris");
                System.out.println("---------------------------------------------------------------");
                System.out.println("Comments: ");
                for (int i = 0; i < comments.size(); i++) {
                    var comment = comments.get(i);
                    System.out.printf("%d. From player %s : %s\n", i + 1, comment.getPlayer(), comment.getComment());
                }
                System.out.println("---------------------------------------------------------------");
            } catch (Exception e) {
                System.out.println("Error retrieving comments: " + e.getMessage());
            }
        } else {
            System.out.println("Comment service is not initialized. Unable to retrieve comments.");
        }
    }

}
