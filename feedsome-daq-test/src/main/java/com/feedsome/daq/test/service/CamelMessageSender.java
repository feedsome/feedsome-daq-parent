package com.feedsome.daq.test.service;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

@Validated
public class CamelMessageSender implements MessageSender {

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    public void send(@NotEmpty String message, @NotEmpty String endpoint) {
        if(StringUtils.isAnyBlank(message, endpoint)) {
            throw new ConstraintViolationException("One or more arguments are blank!", Collections.emptySet());
        }
        producerTemplate.sendBody(endpoint, message);
    }
}
