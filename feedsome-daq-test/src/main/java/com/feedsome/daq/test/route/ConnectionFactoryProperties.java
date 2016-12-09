package com.feedsome.daq.test.route;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "camel.connection.rabbitmq")
public class ConnectionFactoryProperties {

    private String host;

    private int port;

    private String username;

    private String password;
}
