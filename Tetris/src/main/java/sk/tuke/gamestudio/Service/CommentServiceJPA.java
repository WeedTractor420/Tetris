package sk.tuke.gamestudio.Service;

import sk.tuke.gamestudio.Entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) throws CommentException {
        try {
            entityManager.persist(comment);
        } catch (Exception e) {
            throw new CommentException("Error adding comment: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try {
            return entityManager.createQuery("SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.commentedOn DESC", Comment.class)
                    .setParameter("game", game)
                    .getResultList();
        } catch (Exception e) {
            throw new CommentException("Error retrieving comments: " + e.getMessage(), e);
        }
    }


    @Override
    public void reset() throws CommentException {
        try {
            entityManager.createQuery("DELETE FROM Comment").executeUpdate();
        } catch (Exception e) {
            throw new CommentException("Error resetting comments: " + e.getMessage(), e);
        }
    }

}
