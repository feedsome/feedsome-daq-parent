package com.feedsome.daq.test.route;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DataFeedProducerRouteConfiguration.class
})
@EnableConfigurationProperties(ConnectionFactoryProperties.class)
public class RouteConfiguration {

    @Autowired
    private ConnectionFactoryProperties connectionFactoryProperties;

    @Bean
    public ConnectionFactory customConnectionFactory() {
        final ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(connectionFactoryProperties.getHost());
        connectionFactory.setPort(connectionFactoryProperties.getPort());
        connectionFactory.setUsername(connectionFactoryProperties.getUsername());
        connectionFactory.setPassword(connectionFactoryProperties.getPassword());

        return connectionFactory;
    }

}
