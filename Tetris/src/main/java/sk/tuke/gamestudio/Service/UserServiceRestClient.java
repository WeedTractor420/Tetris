package sk.tuke.gamestudio.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.Entity.Users;

@Service
public class UserServiceRestClient implements UserService {

    @Value("${remote.server.api}")
    private String url;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addUser(Users User) {
        // Assuming there is a dedicated endpoint to add a user
        restTemplate.postForEntity(url + "/users", User, Users.class);
    }

    @Override
    public Users getUser(String username, String password) {
        // Assuming the server can accept username and password and validate it
        // This is usually NOT how you should handle passwords. Consider security implications.
        return restTemplate.getForObject(url + "/users/login/username=" + username + "/password=" + password, Users.class);
    }

}
