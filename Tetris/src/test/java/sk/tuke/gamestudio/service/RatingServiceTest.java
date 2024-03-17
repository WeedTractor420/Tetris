package test.java.sk.tuke.gamestudio.service;

import main.java.sk.tuke.gamestudio.Entity.Rating;
import main.java.sk.tuke.gamestudio.Service.RatingException;
import main.java.sk.tuke.gamestudio.Service.RatingService;
import main.java.sk.tuke.gamestudio.Service.RatingServiceJDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RatingServiceTest {

    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceJDBC();
    }

    @AfterEach
    void tearDown() {
        ratingService.reset();
    }

    @Test
    void setRating() throws RatingException {
        Rating rating1 = new Rating("player1", "game1", 5, new java.util.Date());
        ratingService.setRating(rating1);
        int rating = ratingService.getRating("game1", "player1");
        assertEquals(5, rating);

        Rating rating2 = new Rating("player2", "game2", 3, new java.util.Date());
        ratingService.setRating(rating2);
        int rating02 = ratingService.getRating("game2", "player2");
        assertEquals(3, rating02);
    }

    @Test
    void getAverageRating() throws RatingException {
        Rating rating1 = new Rating("player1", "game1", 5, new java.util.Date());
        Rating rating2 = new Rating("player2", "game1", 3, new java.util.Date());
        ratingService.setRating(rating1);
        ratingService.setRating(rating2);
        double averageRating = ratingService.getAverageRating("game1");
        assertEquals(4, averageRating);
    }

    @Test
    void getRating() throws RatingException {
        Rating rating1 = new Rating("player1", "game3", 5, new java.util.Date());
        ratingService.setRating(rating1);
        int rating = ratingService.getRating("game3", "player1");
        assertEquals(5, rating);

        Rating rating2 = new Rating("player2", "game3", 3, new java.util.Date());
        ratingService.setRating(rating2);
        rating = ratingService.getRating("game3", "player2");
        assertEquals(3, rating);
    }

}