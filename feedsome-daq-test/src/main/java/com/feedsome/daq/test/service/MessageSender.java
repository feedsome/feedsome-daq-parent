package com.feedsome.daq.test.service;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Defines behaviour for components capable of sending marshaled messages
 * to a provided endpoint
 */
public interface MessageSender {

    /**
     * Sends a marshaled message to a specific endpoint
     * @param message the {@link String} representation of the message body
     * @param endpoint the {@link String} representation that corresponds
     *                 to the target endpoint, where the message should be sent
     */
    void send(@NotEmpty String message, @NotEmpty String endpoint);

    void send(@NotNull Object body, @NotEmpty String endpoint);

}
