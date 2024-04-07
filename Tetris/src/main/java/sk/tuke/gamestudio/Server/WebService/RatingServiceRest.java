package sk.tuke.gamestudio.Server.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.Entity.Rating;
import sk.tuke.gamestudio.Service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    @GetMapping("/average/{game}")
    public int getAverageRating(@PathVariable String game) { return ratingService.getAverageRating(game); }

    @GetMapping("/{game}/{player}")
    public int getRating(@PathVariable String game, @PathVariable String player) {
        return ratingService.getRating(game, player);
    }

    @PostMapping
    public void setRating(@RequestBody Rating rating) { ratingService.setRating(rating); }
}
