package main.java.sk.tuke.gamestudio;

import main.java.sk.tuke.gamestudio.Service.*;
import main.java.sk.tuke.gamestudio.game.ConsoleUI.ConsoleUI;
import main.java.sk.tuke.gamestudio.game.Core.Game;
import main.java.sk.tuke.gamestudio.game.Core.GameBoard;

public class Main {
    public static void main(String[] args){
        ScoreService scoreService = new ScoreServiceJDBC();
        RatingService ratingService = new RatingServiceJDBC();
        CommentService commentService = new CommentServiceJDBC();
        GameBoard gameBoard = new GameBoard(16,9);
        Game newGame = new Game(gameBoard);
        ConsoleUI game = new ConsoleUI(newGame);
        game.setScoreService(scoreService);
        game.setRatingService(ratingService);
        game.setCommentService(commentService);
        game.start();
    }

}
