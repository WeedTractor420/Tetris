package test.java.sk.tuke.gamestudio.service;

import main.java.sk.tuke.gamestudio.Entity.Comment;
import main.java.sk.tuke.gamestudio.Service.CommentException;
import main.java.sk.tuke.gamestudio.Service.CommentService;
import main.java.sk.tuke.gamestudio.Service.CommentServiceJDBC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceJDBC();
    }

    @AfterEach
    void tearDown() {
        commentService.reset();
    }

    @Test
    void addComment() throws CommentException {
        Comment comment = new Comment("game1", "player1", "comment1", new java.util.Date());
        commentService.addComment(comment);
        List<Comment> comments = commentService.getComments("game1");
        assertEquals(1, comments.size());
        assertEquals(comment.getComment(), comments.get(0).getComment());
    }

    @Test
    void getComments() throws CommentException {
        Comment comment1 = new Comment("game2", "player2", "comment2", new java.util.Date());
        Comment comment2 = new Comment("game2", "player3", "comment3", new java.util.Date());
        commentService.addComment(comment1);
        commentService.addComment(comment2);
        List<Comment> comments = commentService.getComments("game2");
        assertEquals(2, comments.size());
        assertEquals(comment1.getComment(), comments.get(0).getComment());
        assertEquals(comment2.getComment(), comments.get(1).getComment());
    }

    @Test
    void reset() throws CommentException {
        Comment comment1 = new Comment("game3", "player4", "comment4", new java.util.Date());
        Comment comment2 = new Comment("game3", "player5", "comment5", new java.util.Date());
        commentService.addComment(comment1);
        commentService.addComment(comment2);
        List<Comment> comments = commentService.getComments("game3");
        assertEquals(2, comments.size());
        commentService.reset();
        comments = commentService.getComments("game3");
        assertEquals(0, comments.size());
    }
}