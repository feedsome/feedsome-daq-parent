package com.feedsome.daq.test;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "feedsome.plugin", ignoreUnknownFields = false)
public class PluginProperties {

    private String name;

    private List<String> categories = new ArrayList<>();

}
