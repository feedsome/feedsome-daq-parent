package com.feedsome.daq.test.service;

import org.apache.camel.ProducerTemplate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

public class CamelMessageSender implements MessageSender {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public <T> void send(@NotNull T message, @NotEmpty String endpoint) {
        producerTemplate.sendBody(endpoint, message);
    }
}
