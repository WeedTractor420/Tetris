package sk.tuke.gamestudio.Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.Entity.Users;
import sk.tuke.gamestudio.Service.UserService;
import sk.tuke.gamestudio.Service.UserException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @GetMapping
    public String menu(){
        return "menu-login";
    }

    @GetMapping("/registerForm")
    public String redirectToRegisterForm(){
        return "menu-register";
    }

    @GetMapping("/loginForm")
    public String redirectToLoginForm(){
        return "menu-login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute Users credentials, Model model, HttpServletRequest request) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();
        try {
            Users user = userService.getUser(username, password);
            if (user != null) {
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("isGuest", false); // Reset guest flag
                model.addAttribute("loginSuccess", "Login successful!");
                return "redirect:/tetris/menu"; // Redirect to game page or dashboard
            } else {
                model.addAttribute("loginError", "Invalid username or password");
                return "menu-login"; // Stay on the current page, showing error
            }
        } catch (UserException e) {
            model.addAttribute("loginError", "Login error: " + e.getMessage());
            return "menu-login"; // Stay on the current page, showing error
        }
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users credentials, Model model) {
        try {
            credentials.setDateRegistered(new Date());
            credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
            userService.addUser(credentials);
            model.addAttribute("registerSuccess", "Registration successful. Please log in.");
            return "menu-login"; // Redirect to the login page
        } catch (UserException e) {
            model.addAttribute("registerError", "Registration error: " + e.getMessage());
            return "menu-register"; // Stay on the registration form page, showing error
        }
    }

    @PostMapping("/guestLogin")
    public String guestLogin(HttpServletRequest request) {
        request.getSession().setAttribute("user", "guest");
        request.getSession().setAttribute("isGuest", true); // Additional flag to identify guest users
        return "redirect:/tetris/menu"; // Redirect them to the main game page
    }
}
