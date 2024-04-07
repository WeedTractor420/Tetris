package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.Entity.Score;
import sk.tuke.gamestudio.Service.ScoreService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void reset() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("Tetris").size());
    }

    @Test
    public void addScore() {
        scoreService.reset();
        var date = new Date();

        scoreService.addScore(new Score("Jaro", "Tetris", 100, date));

        var scores = scoreService.getTopScores("Tetris");
        assertEquals(1, scores.size());
        assertEquals("Tetris", scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
    }

    @Test
    public void getTopScores() {
        scoreService.reset();
        var date = new Date();
        scoreService.addScore(new Score("Jaro", "Tetris", 120, date));
        scoreService.addScore(new Score("Katka", "Tetris", 150, date));
        scoreService.addScore(new Score("Jaro", "Tetris",100, date));

        var scores = scoreService.getTopScores("Tetris");

        assertEquals(3, scores.size());

        assertEquals("Tetris", scores.get(0).getGame());
        assertEquals("Katka", scores.get(0).getPlayer());
        assertEquals(150, scores.get(0).getPoints());

        assertEquals("Tetris", scores.get(1).getGame());
        assertEquals("Jaro", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());

        assertEquals("Tetris", scores.get(2).getGame());
        assertEquals("Jaro", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
    }
}

