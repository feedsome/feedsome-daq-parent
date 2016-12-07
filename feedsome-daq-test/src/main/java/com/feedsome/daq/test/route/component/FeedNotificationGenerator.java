package com.feedsome.daq.test.route.component;

import com.feedsome.model.FeedNotification;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class FeedNotificationGenerator implements Generator<FeedNotification> {

    private final RestTemplate restTemplate;

    public FeedNotificationGenerator(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FeedNotification get() {
        final FeedNotification feed = new FeedNotification();

        final JokeResponseDTO jokeResponse = restTemplate.getForObject(
                "http://api.icndb.com/jokes/random/", JokeResponseDTO.class);

        if(jokeResponse == null) {
           throw new RuntimeException("Could not manage to acquire a valid response from service!");
        }

        final String jokeBody = jokeResponse.getValue().getJoke();

        feed.setTitle(jokeBody.substring(0, 9));
        feed.setBody(jokeBody);

        feed.getCategories().addAll(jokeResponse.getValue().getCategories());
        feed.setTags(Arrays.asList("funny", "joke", "random"));

        return feed;
    }

}
