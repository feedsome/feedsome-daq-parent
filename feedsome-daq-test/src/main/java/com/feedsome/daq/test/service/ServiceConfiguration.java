package com.feedsome.daq.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedsome.daq.test.route.EndpointProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ValidationConfiguration.class
})
@EnableConfigurationProperties(EndpointProperties.class)
public class ServiceConfiguration {

    @Autowired
    private EndpointProperties endpointProperties;

    @Bean
    public ObjectMapper mapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        return mapper;
    }

    @Bean
    public MessageSender messageSender() {
        return new CamelMessageSender();
    }

    @Bean
    public PluginRegistrationService pluginRegistrationService(final ObjectMapper mapper,
                                                               final MessageSender messageSender) {
        return new PluginRegistrationServiceImpl(endpointProperties.getSendRegistrationUri(), mapper, messageSender);
    }

}
