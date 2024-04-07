package sk.tuke.gamestudio.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity // Marks this class as a JPA entity
public class Comment {

    @Id // Marks this field as the primary key of the entity
    @GeneratedValue// Specifies the primary key generation strategy
    private long ident;

    @Setter @Getter
    private String player;

    @Setter @Getter
    private String game;

    @Setter @Getter
    private String comment;

    @Setter @Getter
    private Date commentedOn;

    public Comment() {
        // No-argument constructor required by JPA
    }

    public Comment(String player, String game, String comment, Date commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment='" + comment + '\'' +
                ", commentedOn=" + commentedOn +
                '}';
    }
}
