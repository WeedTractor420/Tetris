package sk.tuke.gamestudio.Service;

public class UserException extends Exception {

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
