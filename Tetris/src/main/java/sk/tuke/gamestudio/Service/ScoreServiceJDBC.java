package sk.tuke.gamestudio.Service;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.Entity.Score;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreServiceJDBC implements ScoreService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "bemi1";
    public static final String SELECT = "SELECT game, player, points, playedOn FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM score";
    public static final String INSERT = "INSERT INTO score (game, player, points, playedOn) VALUES (?, ?, ?, ?)";
    public static final String SELECT_PLAYER = "SELECT game, player, points, playedOn FROM score WHERE game = ? AND player = ?";

    @Override
    public void addScore(Score score) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, score.getGame());
            statement.setString(2, score.getPlayer());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Score already exists, update it
                    updateScore(connection, score);
                } else {
                    // Score doesn't exist, insert new score
                    insertScore(insertStatement, score);
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem setting score", e);
        }
    }

    private void updateScore(Connection connection, Score score) throws SQLException {
        // Check if the new score is higher than the existing one
        int existingScore = getExistingScore(connection, score.getGame(), score.getPlayer());
        if (existingScore < score.getPoints()) {
            // If the new score is higher, update the score
            try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE score SET points = ?, playedOn = ? WHERE game = ? AND player = ?")) {
                updateStatement.setInt(1, score.getPoints());
                updateStatement.setTimestamp(2, new Timestamp(score.getPlayedOn().getTime()));
                updateStatement.setString(3, score.getGame());
                updateStatement.setString(4, score.getPlayer());
                updateStatement.executeUpdate();
            }
        }
    }

    // Helper method to retrieve the existing score for a player in a game
    private int getExistingScore(Connection connection, String game, String player) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT points FROM score WHERE game = ? AND player = ?")) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    // If no existing score found, return a value indicating no score
                    return Integer.MIN_VALUE;
                }
            }
        }
    }

    private void insertScore(PreparedStatement insertStatement, Score score) throws SQLException {
        insertStatement.setString(1, score.getGame());
        insertStatement.setString(2, score.getPlayer());
        insertStatement.setInt(3, score.getPoints());
        insertStatement.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
        insertStatement.executeUpdate();
    }

    @Override
    public List<Score> getTopScores(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}
