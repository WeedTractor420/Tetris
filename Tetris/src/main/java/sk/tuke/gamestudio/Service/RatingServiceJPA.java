package sk.tuke.gamestudio.Service;

import sk.tuke.gamestudio.Entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void setRating(Rating rating) throws RatingException {
        Rating existingRating = entityManager.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultStream().findFirst().orElse(null);

        if (existingRating != null) {
            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(new Date());
            entityManager.merge(existingRating);
        } else {
            entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Double averageRating = entityManager.createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game", Double.class)
                    .setParameter("game", game)
                    .getSingleResult();

            return averageRating != null ? averageRating.intValue() : 0; // Convert Double to int
        } catch (Exception e) {
            throw new RatingException("Error retrieving average rating: " + e.getMessage(), e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating rating = entityManager.createQuery("SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player", Rating.class)
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultStream().findFirst().orElse(null);

        return rating != null ? rating.getRating() : 0;
    }


    @Override
    public void reset() throws RatingException {
        entityManager.createQuery("DELETE FROM Rating").executeUpdate();
    }

}
