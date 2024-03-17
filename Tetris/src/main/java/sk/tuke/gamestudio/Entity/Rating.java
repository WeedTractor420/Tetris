package main.java.sk.tuke.gamestudio.Entity;

import java.util.Date;

public class Rating {
    private String player;
    private String game;
    private int rating;
    private Date ratedOn;

    public Rating(String player, String game, int rating, Date RatedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = RatedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn (Date RatedOn) {
        this.ratedOn = RatedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + rating +
                ", playedOn=" + ratedOn +
                '}';
    }
}
