package com.feedsome.daq.test;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;
import java.util.HashSet;

@Data
@ConfigurationProperties(prefix = "feedsome.plugin")
public class PluginProperties {

    private String name;

    private Collection<String> categories = new HashSet<>();

}
