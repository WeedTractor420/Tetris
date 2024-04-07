package sk.tuke.gamestudio.Server.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.Entity.Score;
import sk.tuke.gamestudio.Service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) { return scoreService.getTopScores(game); }

    @PostMapping
    public void addScore(@RequestBody Score score) { scoreService.addScore(score); }
}