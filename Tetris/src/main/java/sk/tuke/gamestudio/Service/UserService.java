package sk.tuke.gamestudio.Service;

import sk.tuke.gamestudio.Entity.Users;

public interface UserService {

    void addUser(Users User) throws UserException;

    Users getUser(String username, String password) throws UserException;
}
