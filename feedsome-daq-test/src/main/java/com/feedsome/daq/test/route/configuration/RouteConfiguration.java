package com.feedsome.daq.test.route.configuration;

import com.feedsome.daq.test.route.component.FeedNotificationGenerator;
import com.feedsome.daq.test.route.component.Generator;
import com.feedsome.model.FeedNotification;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(ConnectionFactory.class)
public class RouteConfiguration {

    public Generator<FeedNotification> feedNotificationGenerator(final RestTemplate restTemplate) {
        return new FeedNotificationGenerator(restTemplate);
    }

}
