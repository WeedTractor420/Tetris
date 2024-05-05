package sk.tuke.gamestudio.Server.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.Entity.Users;
import sk.tuke.gamestudio.Service.UserService;
import sk.tuke.gamestudio.Service.UserException;

@RestController
@RequestMapping("/api/users")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Users user) {
        try {
            userService.addUser(user);
            return ResponseEntity.ok().body("User added successfully");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body("Failed to add user: " + e.getMessage());
        }
    }

    @GetMapping("/login/username={username}/password={password}")
    public ResponseEntity<?> getUser(@RequestParam String username, @RequestParam String password) {
        try {
            Users user = userService.getUser(username, password);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}
