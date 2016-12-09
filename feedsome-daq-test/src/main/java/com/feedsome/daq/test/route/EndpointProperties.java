package com.feedsome.daq.test.route;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "camel.endpoint.plugin")
public class EndpointProperties {

    private String dataFeedProducerUri = "seda:feed:produce";

    private String sendRegistrationUri;

    private String sendFeedNotificationUri;

}
