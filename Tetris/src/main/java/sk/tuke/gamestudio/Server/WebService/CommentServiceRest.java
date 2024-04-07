package sk.tuke.gamestudio.Server.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.Entity.Comment;
import sk.tuke.gamestudio.Service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public List<Comment> getCommentsByGame(@PathVariable String game) { return commentService.getComments(game); }

    @PostMapping
    public void addComment(@RequestBody Comment comment) { commentService.addComment(comment); }
}
