package sk.tuke.gamestudio.Service;

import sk.tuke.gamestudio.Entity.Rating;

import java.util.List;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    List<Rating> getAllRatingsForGame(String game) throws RatingException;
    void reset() throws RatingException;
}
