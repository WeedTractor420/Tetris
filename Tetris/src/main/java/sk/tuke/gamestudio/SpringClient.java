package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import sk.tuke.gamestudio.Service.ScoreServiceRestClient;
import sk.tuke.gamestudio.Service.*;
import sk.tuke.gamestudio.game.ConsoleUI.ConsoleUI;
import sk.tuke.gamestudio.game.Core.Game;
import sk.tuke.gamestudio.game.Core.GameBoard;


@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.Server.*"))

public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI consoleUI) {
        return s -> consoleUI.start();
    }

    @Bean
    public GameBoard board() {
        return new GameBoard(12, 9);
    }

    @Bean
    public Game game(GameBoard gameBoard) {
        return new Game(gameBoard);
    }

    @Bean
    public ConsoleUI consoleUI(Game game) {
        return new ConsoleUI(game);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    /*@Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }*/
}
