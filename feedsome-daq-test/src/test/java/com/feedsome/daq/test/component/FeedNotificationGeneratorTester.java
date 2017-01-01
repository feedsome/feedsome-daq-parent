package com.feedsome.daq.test.component;

import com.feedsome.model.FeedNotification;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        FeedNotificationGeneratorTester.TestConfiguration.class
})
public class FeedNotificationGeneratorTester {

    @Configuration
    static class TestConfiguration {

        @Bean
        public RestTemplate restTemplate() {
            return new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_REDIRECTS)
                    .getRestTemplate();
        }

        @Bean
        public Generator<FeedNotification> feedNotificationGenerator(final RestTemplate restTemplate) {
            return new FeedNotificationGenerator(restTemplate);
        }
    }


    @Autowired
    private Generator<FeedNotification> feedGenerator;


    @Test
    public void testGenerate_generatesFeedNotification() {
        final FeedNotification feedNotification = feedGenerator.get();

        Assert.assertNotNull(feedNotification);
        Assert.assertFalse(feedNotification.getTitle().isEmpty());
        Assert.assertFalse(feedNotification.getBody().isEmpty());
    }


}
