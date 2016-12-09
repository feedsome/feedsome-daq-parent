package com.feedsome.daq.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.daq.test.route.EndpointProperties;
import com.feedsome.model.PluginRegistration;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@EnableConfigurationProperties(EndpointProperties.class)
public class PluginRegistrationServiceImpl implements PluginRegistrationService {
    private static final Logger LOG = LoggerFactory.getLogger(PluginRegistrationServiceImpl.class);

    private final String sendRegistrationUri;

    private final ObjectMapper mapper;
    private final MessageSender messageSender;

    public PluginRegistrationServiceImpl(@NotEmpty final String sendRegistrationUri,
                                         @NotNull final ObjectMapper mapper,
                                         @NotNull final MessageSender messageSender) {
        this.sendRegistrationUri = sendRegistrationUri;
        this.mapper = mapper;
        this.messageSender = messageSender;
    }

    @Override
    public void register(@NotNull final PluginRegistration pluginRegistration) {
        String registrationMessage = "";
        try {
            registrationMessage = mapper.writeValueAsString(pluginRegistration);
        } catch (JsonProcessingException e) {
            LOG.error("Exception while marshalling plugin registration message to JSON", e);
        }

        if(StringUtils.isBlank(registrationMessage)) {
            LOG.debug("Plugin registration message is empty string! Will do nothing");
            return;
        }

        LOG.info("Sending registration message for plugin with info: " + registrationMessage);
        messageSender.send(registrationMessage, sendRegistrationUri);

        LOG.info("Registration message sent");
    }
}
