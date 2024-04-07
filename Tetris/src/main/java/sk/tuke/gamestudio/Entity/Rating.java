package sk.tuke.gamestudio.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity // This annotation marks the class as a JPA entity
public class Rating {

    @Id // Marks this field as the primary key
    @GeneratedValue// Configures the way ID is generated
    private long ident;

    @Setter @Getter
    private String player;

    @Setter @Getter
    private String game;

    @Setter @Getter
    private int rating;

    @Setter @Getter
    private Date ratedOn;

    public Rating() {
    }

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }
}
