package main.java.sk.tuke.gamestudio.Service;

import main.java.sk.tuke.gamestudio.Entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
