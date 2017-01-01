package com.feedsome.daq.test.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.daq.test.PluginProperties;
import com.feedsome.model.FeedNotification;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        CamelAutoConfiguration.class,
        DataFeedProducerRouteTester.TestConfiguration.class
})
public class DataFeedProducerRouteTester {


    private interface Values {
        String producerEndpointUri = "seda:feed:produce";

        String mockEndpointUri = "mock:feed:send";

        List<String> categories = Arrays.asList("test", "fancy", "super");

        String pluginName = "Test Plugin DAQ";

    }

    @Configuration
    @Import({
            DataFeedProducerRouteConfiguration.class
    })
    static class TestConfiguration {

        @Bean
        public ObjectMapper mapper() {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            return mapper;
        }

        @Bean
        public EndpointProperties endpointProperties() {
            final EndpointProperties endpointProperties = new EndpointProperties();
            endpointProperties.setDataFeedProducerUri(Values.producerEndpointUri);
            endpointProperties.setSendFeedNotificationUri(Values.mockEndpointUri);

            return endpointProperties;
        }

        @Bean
        public PluginProperties pluginProperties() {
          final PluginProperties pluginProperties = new PluginProperties();
          pluginProperties.setCategories(Values.categories);
          pluginProperties.setName(Values.pluginName);

          return pluginProperties;
        }

    }

    @EndpointInject(uri = Values.mockEndpointUri)
    private MockEndpoint mockEndpoint;

    @EndpointInject(uri = Values.producerEndpointUri)
    private ProducerTemplate producerTemplate;

    @Autowired
    private ObjectMapper mapper;


    @After
    public void tearDown() throws Exception {
        mockEndpoint.reset();
    }

    @Test
    public void testDataFeedProducerRoute_When_FeedNotificationIsNull_DoesNothing() throws Exception {
        producerTemplate.sendBody(null);

        mockEndpoint.expectedMessageCount(0);

        mockEndpoint.await(10, TimeUnit.SECONDS);

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    public void testDataFeedProducerRoute_When_MessageIsNotFeedNotification_DoesNothing() throws Exception {
        producerTemplate.sendBody("I'm not a feed notification! I'm just a simple string!");

        mockEndpoint.expectedMessageCount(0);

        mockEndpoint.await(10, TimeUnit.SECONDS);

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    public void testDataFeedProducerRoute_When_MessageIsFeedNotification_ProcessesAndSendsNotification() throws Exception {
        final FeedNotification notification = new FeedNotification();
        notification.setTitle("I'm a fancy title!");
        notification.setBody("For sure here goes a fancy content!");
        notification.setTags(Arrays.asList("test", "fancy", "news"));

        producerTemplate.sendBody(notification);

        mockEndpoint.expectedMessageCount(1);


        final FeedNotification expectedNotification = new FeedNotification();
        expectedNotification.setTitle(notification.getTitle());
        expectedNotification.setBody(notification.getBody());
        expectedNotification.setTags(notification.getTags());

        expectedNotification.setPluginRef(Values.pluginName);


        final String expectedMessage = mapper.writeValueAsString(expectedNotification);
        mockEndpoint.expectedBodiesReceived(expectedMessage);

        mockEndpoint.await(10, TimeUnit.SECONDS);

        mockEndpoint.assertIsSatisfied();
    }

}
