package com.feedsome.daq.test.route;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.feedsome.daq.test.PluginProperties;
import com.feedsome.model.FeedNotification;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolationException;

@Configuration
@EnableConfigurationProperties({
        EndpointProperties.class,
        PluginProperties.class
})
public class DataFeedProducerRouteConfiguration {

    @Autowired
    private EndpointProperties endpointProperties;

    @Autowired
    private PluginProperties pluginProperties;

    @Bean
    public RouteBuilder dataFeedProducerRouteBuilder() {
        final JacksonDataFormat dataFormat = new JacksonDataFormat(FeedNotification.class);
        dataFormat.addModule(new Jdk8Module());

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(ConstraintViolationException.class)
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR)
                        .stop();

                onException(Exception.class)
                        .maximumRedeliveries(5)
                        .redeliveryDelay(3000)
                        .useExponentialBackOff()
                        .logRetryStackTrace(true)
                        .logStackTrace(true)
                        .logHandled(true)
                        .retryAttemptedLogLevel(LoggingLevel.WARN)
                        .retriesExhaustedLogLevel(LoggingLevel.ERROR);

                from(endpointProperties.getDataFeedProducerUri()).routeId("data feed producer route")
                        .log("received a generated data feed for processing")
                        .filter(body().isNotNull())
                        .filter(body().isInstanceOf(FeedNotification.class))
                        .log("enriching data feed with plugin information")
                        .process((processor) -> {
                            final FeedNotification feedNotification = processor.getIn()
                                    .getBody(FeedNotification.class);

                            feedNotification.setPluginRef(pluginProperties.getName());
                        })
                        .log("trying to marshal data feed to JSON...")
                        .marshal(dataFormat)
                        .log("data feed transformed to JSON message format")
                        .log("now sending data feed to the system consumers...")
                        .to(endpointProperties.getSendFeedNotificationUri())
                        .log("data feed sent");
            }
        };
    }


}
