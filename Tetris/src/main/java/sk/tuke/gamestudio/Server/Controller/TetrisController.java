package sk.tuke.gamestudio.Server.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.Entity.Comment;
import sk.tuke.gamestudio.Entity.Rating;
import sk.tuke.gamestudio.Entity.Score;
import sk.tuke.gamestudio.Entity.Users;
import sk.tuke.gamestudio.Service.CommentService;
import sk.tuke.gamestudio.Service.RatingService;
import sk.tuke.gamestudio.Service.ScoreService;
import sk.tuke.gamestudio.game.Core.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tetris")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TetrisController {
    private Game game = new Game(new GameBoard(18, 9));

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String menuLogin(Model model){
        updateModel(model);
        return "menu-login";
    }

    @GetMapping("/menu")
    public String menu(Model model){
        game.restartGame();
        game.setState(GameState.MENU);
        updateModel(model);
        return "menu";
    }

    @GetMapping("/menuCommentRateView")
    public String menuCommentRateView(Model model){
        updateModel(model);
        return "menu-comment-rate-view";
    }

    @GetMapping("/gameInfo")
    public String gameInfo(){
        return "game-info";
    }

    @GetMapping("/new")
    public String newGame(Model model) {
        if (game.getState() == GameState.FAILED || game.getState() == GameState.MENU) {
            game = new Game(new GameBoard(18, 9)); // Proper initialization
        }
        game.setState(GameState.PLAYING);
        updateModel(model);
        return "tetris";
    }

    private void performGameTickAndUpdateModel(Model model) {
        if (game != null) {
            game.gameTick();
            updateModel(model);
        }
    }

    @GetMapping("/rotateRight")
    public String rotateRight(Model model) {
        if (game != null) {
            game.rotateCurrentShape(true);
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }

    @GetMapping("/hold")
    public String hold(Model model) {
        if (game != null) {
            game.holdShape();
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }

    @GetMapping("/rotateLeft")
    public String rotateLeft(Model model) {
        if (game != null) {
            game.rotateCurrentShape(false);
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }

    @GetMapping("/moveLeft")
    public String moveLeft(Model model) {
        if (game != null) {
            game.moveCurrentShape(false);
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }

    @GetMapping("/moveRight")
    public String moveRight(Model model) {
        if (game != null) {
            game.moveCurrentShape(true);
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }

    @GetMapping("/moveDown")
    public String moveDown(Model model) {
        if (game != null) {
            game.moveCurrentShapeDown();
            performGameTickAndUpdateModel(model);
        }
        return "tetris";
    }


    // Rendering HTML table with style attributes
    private String getHtmlField() {
        GameBoard board = game.getPlayingBoard();
        Tile[][] tiles = board.getBoard();
        StringBuilder sb = new StringBuilder();

        sb.append("<table class='tetris-board'>\n");
        for (int row = 0; row < board.getRowCount(); row++) {
            sb.append(String.format("<tr id='row-%d'>\n", row));
            for (int column = 0; column < board.getColCount(); column++) {
                Tile tile = tiles[row][column];
                String cellClass = getCssClassForTile(tile); // Modify to get the appropriate class
                sb.append(String.format("<td class='%s'></td>\n", cellClass));
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

    // This method returns a specific CSS class based on the state and type of the Tile
    private String getCssClassForTile(Tile tile) {
        if (tile.isOccupied()) {
            return "tetris-block " + tileTypeToCssClass(tile.getSymbol());
        } else if (tile.isShadow()) {
            return "tetris-shadow " + "shadow-" + game.getCurrentShape().getShapeSymbol();
        } else {
            return "tetris-empty"; // Class for empty tiles
        }
    }

    // This method converts a tile symbol to a CSS class
    private String tileTypeToCssClass(char symbol) {
        return switch (symbol) {
            case 'I' -> "tile-I";
            case 'J' -> "tile-J";
            case 'L' -> "tile-L";
            case 'O' -> "tile-O";
            case 'S' -> "tile-S";
            case 'T' -> "tile-T";
            case 'Z' -> "tile-Z";
            case '_' -> "h-border";
            case '|' -> "v-border";
            default -> ""; // No additional class for undefined types
        };
    }


    @GetMapping("/getBoard")
    public ResponseEntity<String> getBoardHtml() {
        if (game != null) {
            String boardHtml = getHtmlField();
            return new ResponseEntity<>(boardHtml, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/rate")
    public String rateGame(@ModelAttribute Rating rating, Model model, HttpServletRequest request) {
        // Retrieve user from session and check if they are a guest
        Users user = null;
        Object userObj = request.getSession().getAttribute("user");
        Boolean isGuest = (Boolean) request.getSession().getAttribute("isGuest");

        if (userObj instanceof Users) {
            user = (Users) userObj;
        }

        // Only allow non-guest users to submit ratings
        if (user != null && (isGuest == null || !isGuest)) {
            try {
                rating.setPlayer(user.getUsername());
                rating.setGame("Tetris");
                rating.setRatedOn(new Date());
                ratingService.setRating(rating);
                model.addAttribute("message", "Rating Submitted Successfully!");
                return "menu-rate-submit"; // Redirect back to the Tetris game menu
            } catch (Exception e) {
                model.addAttribute("message", "Error submitting rating: " + e.getMessage());
                return "menu-rate-submit"; // Stay on the rating page and display the error
            }
        } else {
            model.addAttribute("message", "You must be logged in to rate.");
            return "menu-rate-submit"; // Redirect or notify on the rating page
        }
    }
    @GetMapping("/myRating")
    public ResponseEntity<String> getMyRating(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute("user");
        Boolean isGuest = (Boolean) request.getSession().getAttribute("isGuest");


        if (userObj instanceof Users user && !isGuest) {
            try {
                String username = user.getUsername();
                int rating = ratingService.getRating("Tetris", username);
                return ResponseEntity.ok(Integer.toString(rating));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting rating: " + e.getMessage());
            }
        } else {
            // This else block handles cases where userObj is not an instance of Users
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not logged in or session is corrupted.");
        }
    }

    @PostMapping("/comment")
    public String addComment(@ModelAttribute Comment comment, Model model, HttpServletRequest request) {
        Users user = null;
        Object userObj = request.getSession().getAttribute("user");
        if (userObj instanceof Users) {
            user = (Users) userObj;
            try {
                comment.setGame("Tetris");
                comment.setCommentedOn(new Date());
                comment.setPlayer(user.getUsername());
                commentService.addComment(comment);
                model.addAttribute("message", "Comment Saved Successfully!");
                return "menu-comment-submit";
            } catch (Exception e) {
                model.addAttribute("message", "Error submitting comment: " + e.getMessage());
                return "menu-comment-submit";
            }
        } else {
            model.addAttribute("message", "You must be logged in to post comments.");
            return "menu-comment-submit";
        }
    }

    @PostMapping("/submitScore")
    public String submitScore(@RequestParam("points") int points, HttpServletRequest request, Model model) {
        Users user = (Users) request.getSession().getAttribute("user");
        Boolean isGuest = (Boolean) request.getSession().getAttribute("isGuest");

        if (user != null && (isGuest == null || !isGuest)) {
            try {
                Score score = new Score();
                score.setPlayer(user.getUsername());
                score.setPoints(points);
                score.setPlayedOn(new Date());
                score.setGame("Tetris");

                scoreService.addScore(score);
                return "redirect:/tetris/menu"; // Redirect to the game menu or dashboard
            } catch (Exception e) {
                model.addAttribute("error", "Failed to submit score: " + e.getMessage());
                return "menu-login"; // Stay on the current game page
            }
        } else {
            model.addAttribute("error", "You must be logged in to submit scores.");
            return "menu-login"; // Redirect to login if not logged in or is a guest
        }
    }

    private String getNextShapesHtml(List<Shape> nextShapes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='shapes-tables'>\n"); // Container for all the shapes
        sb.append("<h2>NEXT</h2>\n");
        for (Shape shape : nextShapes) {
            Tile[][] tiles = shape.getBlock(); // Assuming Shape has a method getTiles()
            sb.append("<div class='single-shape'>\n"); // Container for a single shape
            sb.append("<table class='shape-table'>\n");
            for (Tile[] row : tiles) {
                for (Tile tile : row) {
                    // Apply 'tetris-block' class if the tile is occupied, otherwise 'tetris-empty'
                    String cellClass = (tile != null && tile.isOccupied()) ? "tetris-block " + getCssClassForTile(tile) : "tetris-empty";
                    sb.append(String.format("<td class='%s'></td>\n", cellClass));
                }
                sb.append("</tr>\n");
            }
            sb.append("</table>\n");
            sb.append("</div>\n"); // Close single shape container
        }
        sb.append("</div>\n"); // Close shape queue container
        return sb.toString();
    }

    private String getHeldShapeHtml() {
        if (game.getHeldShape() == null) {
            return "<div class='held-shape'>No shape held</div>";
        }

        // Similar to how you generate shapes for the shape queue
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='held-shape'>\n"); // Container for all the shapes
        sb.append("<h2>Hold</h2>\n");
        Tile[][] tiles = game.getHeldShape().getBlock();
        sb.append("<table class='shape-table'>\n");
        for (Tile[] row : tiles) {
            sb.append("<tr>");
            for (Tile tile : row) {
                String cssClass = tile != null && tile.isOccupied() ? "tetris-block " + tileTypeToCssClass(tile.getSymbol()) : "tetris-empty";
                sb.append(String.format("<td class='%s'></td>", cssClass));
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</div>\n"); // Close shape queue container
        return sb.toString();
    }

    @GetMapping("/getShapeQueue")
    public ResponseEntity<String> getShapeQueueHtml() {
        if (game != null) {
            List<Shape> nextShapes = game.getShapeQueue().getShapeQueue()
                    .subList(game.getShapeQueue().getIndex(), game.getShapeQueue().getIndex() + 3);
            String nextShapesHtml = getNextShapesHtml(nextShapes);
            return new ResponseEntity<>(nextShapesHtml, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getCurrentScore")
    public ResponseEntity<String> getCurrentScore() {
        if (game != null) {
            return new ResponseEntity<>(String.valueOf(game.getScore()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getCurrentGameState")
    public ResponseEntity<String> getCurrentGameState() {
        if (game != null) {
            return new ResponseEntity<>(game.getState().toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getHeldShape")
    public ResponseEntity<String> holdShape() {
        if (game != null) {
            String heldShapeHtml = getHeldShapeHtml();  // Get the HTML for the held shape
            return new ResponseEntity<>(heldShapeHtml, HttpStatus.OK); // Return only the held shape HTML
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Add the game board state to the model to update the view
    private void updateModel(Model model) {
        int numOf5StarRatings = 0;
        int numOf4StarRatings = 0;
        int numOf3StarRatings = 0;
        int numOf2StarRatings = 0;
        int numOf1StarRatings = 0;

        List<Score> topScores = scoreService.getTopScores("Tetris");
        List<Comment> comments = commentService.getComments("Tetris");
        List<Rating> ratings = ratingService.getAllRatingsForGame("Tetris");
        List<Shape> nextShapes = game.getShapeQueue().getShapeQueue().subList(game.getShapeQueue().getIndex(),
                game.getShapeQueue().getIndex() + 3);

        for (Rating rating : ratings) {
            switch (rating.getRating()) {
                case 5 -> numOf5StarRatings++;
                case 4 -> numOf4StarRatings++;
                case 3 -> numOf3StarRatings++;
                case 2 -> numOf2StarRatings++;
                case 1 -> numOf1StarRatings++;
            }
        }

        model.addAttribute("numOf5StarRatings", numOf5StarRatings);
        model.addAttribute("numOf4StarRatings", numOf4StarRatings);
        model.addAttribute("numOf3StarRatings", numOf3StarRatings);
        model.addAttribute("numOf2StarRatings", numOf2StarRatings);
        model.addAttribute("numOf1StarRatings", numOf1StarRatings);
        model.addAttribute("numOfRatings", ratings.size());
        model.addAttribute("ratings", ratings);
        model.addAttribute("comments", comments);
        model.addAttribute("score", game.getScore());
        model.addAttribute("topScores", topScores); // Pass the list to the model
        model.addAttribute("gameState", game.getState());
        model.addAttribute("averageRating", ratingService.getAverageRating("Tetris"));
        model.addAttribute("boardHtml", getHtmlField());
        model.addAttribute("heldShapeHtml", getHeldShapeHtml());
        model.addAttribute("nextShapesHtml", getNextShapesHtml(nextShapes)); // Add HTML for next shapes
    }

}
