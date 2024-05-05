package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.gamestudio.Entity.Comment;
import sk.tuke.gamestudio.Service.CommentException;
import sk.tuke.gamestudio.Service.CommentService;
import sk.tuke.gamestudio.Service.CommentServiceJDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;


    @AfterEach
    void tearDown() {
        commentService.reset();
    }

    @Test
    void addComment() throws CommentException {
        Comment comment = new Comment("player1", "game1", "comment1", new java.util.Date());
        commentService.addComment(comment);
        List<Comment> comments = commentService.getComments("game1");
        assertEquals(1, comments.size());
        assertEquals(comment.getComment(), comments.get(0).getComment());
    }

    @Test
    void getComments() throws CommentException {
        Comment comment1 = new Comment("player2", "game2", "comment2", new java.util.Date());
        Comment comment2 = new Comment("player3", "game2", "comment3", new java.util.Date());
        commentService.addComment(comment1);
        commentService.addComment(comment2);
        List<Comment> comments = commentService.getComments("game2");
        assertEquals(2, comments.size());
        assertEquals(comment1.getComment(), comments.get(0).getComment());
        assertEquals(comment2.getComment(), comments.get(1).getComment());
    }

    @Test
    void reset() throws CommentException {
        Comment comment1 = new Comment("player4", "game3", "comment4", new java.util.Date());
        Comment comment2 = new Comment("player5", "game3", "comment5", new java.util.Date());
        commentService.addComment(comment1);
        commentService.addComment(comment2);
        List<Comment> comments = commentService.getComments("game3");
        assertEquals(2, comments.size());
        commentService.reset();
        comments = commentService.getComments("game3");
        assertEquals(0, comments.size());
    }
}