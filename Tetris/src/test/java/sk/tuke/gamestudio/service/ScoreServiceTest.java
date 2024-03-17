package test.java.sk.tuke.gamestudio.service;

import main.java.sk.tuke.gamestudio.Entity.Score;
import main.java.sk.tuke.gamestudio.Service.ScoreException;
import main.java.sk.tuke.gamestudio.Service.ScoreServiceJDBC;
import org.junit.jupiter.api.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceTest {

    private ScoreServiceJDBC scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new ScoreServiceJDBC();
    }

    @AfterEach
    void tearDown() {
        scoreService.reset(); // Reset the scores after each test
    }

    @Test
    void addScoresForDifferentGamesAndPlayers() throws ScoreException {
        // Add scores for different games and players
        Score score1 = new Score("Game1", "Player1", 100, new Date());
        Score score2 = new Score("Game2", "Player2", 200, new Date());
        Score score3 = new Score("Game3", "Player3", 300, new Date());

        scoreService.addScore(score1);
        scoreService.addScore(score2);
        scoreService.addScore(score3);

        // Check if scores were added correctly
        List<Score> topScores1 = scoreService.getTopScores("Game1");
        List<Score> topScores2 = scoreService.getTopScores("Game2");
        List<Score> topScores3 = scoreService.getTopScores("Game3");

        assertEquals(1, topScores1.size());
        assertEquals(1, topScores2.size());
        assertEquals(1, topScores3.size());
    }

    @Test
    void addScoresWithSameScoreValueForDifferentPlayersInSameGame() throws ScoreException {
        // Add scores with the same score value for different players in the same game
        Score score1 = new Score("Game1", "Player1", 120, new Date());
        Score score2 = new Score("Game1", "Player2", 100, new Date());

        scoreService.addScore(score1);
        scoreService.addScore(score2);

        // Check if scores were added correctly
        List<Score> topScores = scoreService.getTopScores("Game1");

        assertEquals(2, topScores.size());
        assertEquals(score1.getPoints(), topScores.get(0).getPoints());
        assertEquals(score1.getGame(), topScores.get(0).getGame());

        assertEquals(score2.getPoints(), topScores.get(1).getPoints());
        assertEquals(score2.getGame(), topScores.get(1).getGame());
    }

    @Test
    void addScoresWithSameScoreValueAndPlayerInDifferentGames() throws ScoreException {
        // Add scores with the same score value and player in different games
        Score score1 = new Score("Game1", "Player1", 100, new Date());
        Score score2 = new Score("Game2", "Player1", 100, new Date());

        scoreService.addScore(score1);
        scoreService.addScore(score2);

        // Check if scores were added correctly
        List<Score> topScores1 = scoreService.getTopScores("Game1");
        List<Score> topScores2 = scoreService.getTopScores("Game2");

        assertEquals(1, topScores1.size());
        assertEquals(1, topScores2.size());

        assertEquals(score1.getPoints(), topScores1.get(0).getPoints());
        assertEquals(score1.getGame(), topScores1.get(0).getGame());

        assertEquals(score2.getPoints(), topScores2.get(0).getPoints());
        assertEquals(score2.getGame(), topScores2.get(0).getGame());
    }

    @Test
    void getTopScores() throws ScoreException {
        // Add scores for different games and players
        Score score1 = new Score("Game1", "Player1", 120, new Date());
        Score score2 = new Score("Game1", "Player2", 100, new Date());
        Score score3 = new Score("Game1", "Player3", 150, new Date());
        Score score4 = new Score("Game1", "Player4", 130, new Date());
        Score score5 = new Score("Game1", "Player5", 200, new Date());

        scoreService.addScore(score1);
        scoreService.addScore(score2);
        scoreService.addScore(score3);
        scoreService.addScore(score4);
        scoreService.addScore(score5);

        // Check if top scores were retrieved correctly
        List<Score> topScores = scoreService.getTopScores("Game1");

        assertEquals(5, topScores.size());
        assertEquals(score5.getPoints(), topScores.get(0).getPoints());
        assertEquals(score3.getPoints(), topScores.get(1).getPoints());
        assertEquals(score4.getPoints(), topScores.get(2).getPoints());
        assertEquals(score1.getPoints(), topScores.get(3).getPoints());
        assertEquals(score2.getPoints(), topScores.get(4).getPoints());
    }


}