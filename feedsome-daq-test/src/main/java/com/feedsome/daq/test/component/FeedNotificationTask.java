package com.feedsome.daq.test.component;


import com.feedsome.daq.test.service.MessageSender;
import com.feedsome.model.FeedNotification;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class FeedNotificationTask implements ScheduledTask {

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
        final FeedNotification feedNotification = feedNotificationGenerator.get();

        messageSender.send(feedNotification, produceFeedEndpointUri);
    }
}
