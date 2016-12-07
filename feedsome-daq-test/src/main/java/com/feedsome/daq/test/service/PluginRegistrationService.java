package com.feedsome.daq.test.service;

import com.feedsome.model.PluginRegistration;

import javax.validation.constraints.NotNull;

/**
 * Defines behaviour for services capable of handling plugin registration actions
 */
public interface PluginRegistrationService {

    /**
     * Registers a plugin to the system
     * @param pluginRegistration the registration information
     */
    void register(@NotNull PluginRegistration pluginRegistration);

}
