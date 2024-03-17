package main.java.sk.tuke.gamestudio.Service;

import main.java.sk.tuke.gamestudio.Entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
