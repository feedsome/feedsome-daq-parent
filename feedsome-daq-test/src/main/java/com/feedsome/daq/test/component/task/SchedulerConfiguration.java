package com.feedsome.daq.test.component.task;

import com.feedsome.daq.test.component.Generator;
import com.feedsome.daq.test.route.EndpointProperties;
import com.feedsome.daq.test.service.MessageSender;
import com.feedsome.model.FeedNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(EndpointProperties.class)
public class SchedulerConfiguration {

    @Autowired
    private EndpointProperties endpointProperties;

    @Bean
    public ScheduledTask feedNotificationTask(final MessageSender messageSender,
                                              final Generator<FeedNotification> feedNotificationGenerator) {
        return new FeedNotificationTask(
                endpointProperties.getDataFeedProducerUri(), messageSender, feedNotificationGenerator);
    }

}
