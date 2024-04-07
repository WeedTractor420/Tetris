package sk.tuke.gamestudio.Entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Score {
    @Id
    @GeneratedValue
    private long ident;

    @Setter
    @Getter
    private String player;

    @Setter
    @Getter
    private String game;

    @Setter
    @Getter
    private int points;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Bratislava")
    @Setter
    @Getter
    private Date playedOn;

    public Score() {
    }

    public Score(String player, String game, int points, Date playedOn) {
        this.player = player;
        this.game = game;
        this.points = points;
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", points=" + points +
                ", playedAt=" + playedOn +
                '}';
    }
}

