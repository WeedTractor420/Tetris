package main.java.sk.tuke.gamestudio.Service;

import main.java.sk.tuke.gamestudio.Entity.Rating;
import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "bemi1";
    public static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
    public static final String SELECT_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (player, game, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE rating SET rating = ? WHERE game = ? AND player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT);
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE)
        ) {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // If the player has already rated the game, update the rating
                    updateRating(updateStatement, rating);
                } else {
                    // If the player hasn't rated the game yet, insert a new rating
                    insertRating(insertStatement, rating);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error setting rating", e);
        }
    }

    private void updateRating(PreparedStatement updateStatement, Rating rating) throws SQLException {
        updateStatement.setInt(1, rating.getRating());
        updateStatement.setString(2, rating.getGame());
        updateStatement.setString(3, rating.getPlayer());
        updateStatement.executeUpdate();
    }

    private void insertRating(PreparedStatement insertStatement, Rating rating) throws SQLException {
        insertStatement.setString(1, rating.getPlayer());
        insertStatement.setString(2, rating.getGame());
        insertStatement.setInt(3, rating.getRating());
        insertStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        insertStatement.executeUpdate();
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE)) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the average rating from the result set
                    return rs.getInt(1); // Assuming the average rating is stored as an integer
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error getting average rating", e);
        }
        return 0; // Default return value if no average rating is found
    }


    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting player's rating", e);
        }
        return 0; // Return 0 if no rating is found for the player
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Error resetting ratings", e);
        }
    }
}
