package main.java.sk.tuke.gamestudio.Service;

import main.java.sk.tuke.gamestudio.Entity.Rating;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String player) throws RatingException;
    void reset() throws RatingException;
}
