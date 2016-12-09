package com.feedsome.daq.test.component;

import com.feedsome.daq.test.route.EndpointProperties;
import com.feedsome.daq.test.service.MessageSender;
import com.feedsome.model.FeedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(EndpointProperties.class)
public class SchedulerConfiguration {

    @Autowired
    private EndpointProperties endpointProperties;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Generator<FeedNotification> feedNotificationGenerator(final RestTemplate restTemplate) {
        return new FeedNotificationGenerator(restTemplate);
    }

    @Bean
    public ScheduledTask feedNotificationTask(final MessageSender messageSender,
                                              final Generator<FeedNotification> feedNotificationGenerator) {
        return new FeedNotificationTask(
                endpointProperties.getDataFeedProducerUri(), messageSender, feedNotificationGenerator);
    }

}
