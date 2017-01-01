package com.feedsome.daq.test.component;

import com.feedsome.model.FeedNotification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ComponentConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Generator<FeedNotification> feedNotificationGenerator(final RestTemplate restTemplate) {
        return new FeedNotificationGenerator(restTemplate);
    }

}
