package com.feedsome.daq.test.service;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Validated
public class CamelMessageSender implements MessageSender {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void send(@NotEmpty String message, @NotEmpty String endpointUri) {
        if(StringUtils.isAnyBlank(message, endpointUri)) {
            throw new ConstraintViolationException("One or more arguments are blank!", Collections.emptySet());
        }
        producerTemplate.sendBody(endpointUri, message);
    }

    @Override
    public void send(@NotNull Object body, @NotEmpty String endpointUri) {
        if(StringUtils.isBlank(endpointUri)) {
            throw new ConstraintViolationException("Endpoint uri arguments are blank!", Collections.emptySet());
        }
        producerTemplate.sendBody(endpointUri, body);
    }
}
