package com.feedsome.daq.test.service;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@Import({
        CamelAutoConfiguration.class,
        ValidationConfiguration.class,
        CamelMessageSenderTester.TestConfiguration.class
})
public class CamelMessageSenderTester {

    @Configuration
    static class TestConfiguration {

        @Bean
        public MessageSender messageSender() {
            return new CamelMessageSender();
        }

    }

    private static final String sendMessageUriMock = "mock:feed:send";

    @EndpointInject(uri = sendMessageUriMock)
    private MockEndpoint mockEndpoint;

    @Autowired
    private MessageSender messageSender;

    @After
    public void tearDown() throws Exception {
        mockEndpoint.reset();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenMessageIsNull_Throws_ConstraintViolationException() {
        messageSender.send(null, sendMessageUriMock);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenMessageIsEmpty_Throws_ConstraintViolationException() {
        messageSender.send("", sendMessageUriMock);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenMessageIsBlank_Throws_ConstraintViolationException() {
        messageSender.send(" ", sendMessageUriMock);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenEndpointUriIsNull_Throws_ConstraintViolationException() {
        messageSender.send("plaintext message", null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenEndpointUriIsEmpty_Throws_ConstraintViolationException() {
        messageSender.send("plaintext message", "");
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSend_whenEndpointUriIsBlank_Throws_ConstraintViolationException() {
        messageSender.send("plaintext message", " ");
    }


    @Test
    public void testSend_sendsMessageToEndpoint() throws Exception {
        final String messageBody = "plaintext message";

        messageSender.send(messageBody, sendMessageUriMock);

        mockEndpoint.whenAnyExchangeReceived((processor) -> {
            final String body = processor.getIn().getBody(String.class);

            Assert.assertEquals(messageBody, body);
        });

        mockEndpoint.expectedMessageCount(1);

        mockEndpoint.assertIsSatisfied();
    }


}
