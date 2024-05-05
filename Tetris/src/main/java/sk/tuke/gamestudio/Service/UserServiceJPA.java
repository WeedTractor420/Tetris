package sk.tuke.gamestudio.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sk.tuke.gamestudio.Entity.Users;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserServiceJPA implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public void addUser(Users user) throws UserException {
        try {
            // Check if a user with the same username already exists
            Users existingUser = null;
            try {
                existingUser = entityManager.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class)
                        .setParameter("username", user.getUsername())
                        .getSingleResult();
            } catch (NoResultException ignored) {
                // No user found with the username is okay
            }

            if (existingUser != null) {
                throw new UserException("A user with the same username already exists");
            }

            // Proceed to persist new user
            entityManager.persist(user);
        } catch (UserException e) {
            // Rethrow if it's a UserException
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions
            throw new UserException("Error adding user: " + e.getMessage(), e);
        }
    }

    @Override
    public Users getUser(String username, String password) throws UserException {
        try {
            Users user = entityManager.createQuery("SELECT u FROM Users u WHERE u.username = :username", Users.class)
                    .setParameter("username", username)
                    .getSingleResult();
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
            throw new UserException("Invalid username or password");
        } catch (NoResultException e) {
            throw new UserException("User not found: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserException("Error retrieving user: " + e.getMessage(), e);
        }
    }
}
