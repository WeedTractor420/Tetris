package sk.tuke.gamestudio.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.Entity.Rating;

public class RatingServiceRestClient implements RatingService {

    @Value("${remote.server.api}")
    private String url;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url + "/rating", rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForObject(url + "/rating/average/" + game, Integer.class);
    }

    @Override
    public int getRating(String game, String player) {
        return restTemplate.getForObject(url + "/rating/" + game + "/" + player, Integer.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
