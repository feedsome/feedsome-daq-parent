package com.feedsome.daq.test.component.task;


import com.feedsome.daq.test.component.Generator;
import com.feedsome.daq.test.service.MessageSender;
import com.feedsome.model.FeedNotification;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class FeedNotificationTask implements ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(FeedNotificationTask.class);

    private final String produceFeedEndpointUri;

    private final MessageSender messageSender;
    private final Generator<FeedNotification> feedNotificationGenerator;

    public FeedNotificationTask(@NotEmpty final String produceFeedEndpointUri,
                                @NotNull final MessageSender messageSender,
                                @NotNull final Generator<FeedNotification> feedNotificationGenerator) {
        this.produceFeedEndpointUri = produceFeedEndpointUri;
        this.messageSender = messageSender;
        this.feedNotificationGenerator = feedNotificationGenerator;
    }

    @Override
    @Scheduled(fixedRateString = "${feed.generate.interval}")
    public void run() {
        logger.info("ready to generate feed notification");
        final FeedNotification feedNotification = feedNotificationGenerator.get();

        logger.info("sending feed notification for processing");
        messageSender.send(feedNotification, produceFeedEndpointUri);
    }
}
