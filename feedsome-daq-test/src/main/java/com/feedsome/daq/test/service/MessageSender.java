package com.feedsome.daq.test.service;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public interface MessageSender {

    <T> void send(@NotNull T message, @NotEmpty String endpoint);

}
