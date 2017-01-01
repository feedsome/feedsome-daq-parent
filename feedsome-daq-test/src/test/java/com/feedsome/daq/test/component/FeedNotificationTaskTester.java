package com.feedsome.daq.test.component;

import com.feedsome.daq.test.component.task.SchedulerConfiguration;
import com.feedsome.daq.test.route.EndpointProperties;
import com.feedsome.daq.test.service.MessageSender;
import com.feedsome.model.FeedNotification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        FeedNotificationTaskTester.TestConfiguration.class,
}, properties = "classpath:application.yml")
public class FeedNotificationTaskTester {

    @Configuration
    @Import(SchedulerConfiguration.class)
    static class TestConfiguration {

        @Bean
        public EndpointProperties endpointProperties() {
            final EndpointProperties endpointProperties = new EndpointProperties();
            endpointProperties.setDataFeedProducerUri("mock:feed:producer");

            return endpointProperties;
        }

        @Bean
        public MessageSender messageSender() {
            return mock(MessageSender.class);
        }

        @Bean
        public Generator<FeedNotification> feedNotificationGenerator() {
            return FeedNotification::new;
        }
    }

    @Autowired
    private EndpointProperties endpointProperties;

    @Autowired
    private MessageSender messageSender;

    @Before
    public void setUp() throws Exception {
        Mockito.reset(
                messageSender
        );
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(
                messageSender
        );

    }

    @Test
    public void testScheduledFeedTaskGeneration() throws Exception {
        final short times = 3;

        final CountDownLatch countDownLatch = new CountDownLatch(times);
        Mockito.doAnswer((ans) -> {
            countDownLatch.countDown();

            return null;
        }).when(messageSender).send(any(FeedNotification.class), eq(endpointProperties.getDataFeedProducerUri()));

        countDownLatch.await(15, TimeUnit.SECONDS);

        verify(messageSender, times(times))
                .send(any(FeedNotification.class), eq(endpointProperties.getDataFeedProducerUri()));

    }

}
