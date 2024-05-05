package sk.tuke.gamestudio.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
public class Users {
    @Id
    @GeneratedValue
    private Long ident;

    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private Date dateRegistered;

    public Users() {
    }

    public Users(String username, String password, String email, Date dateRegistered) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + ident +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", dateRegistered=" + dateRegistered +
                '}';
    }
}
