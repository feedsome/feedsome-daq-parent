package com.feedsome.daq.test.service;

import com.feedsome.model.PluginRegistration;

import javax.validation.constraints.NotNull;

public interface PluginRegistrationService {

    void register(@NotNull PluginRegistration pluginRegistration);

}
