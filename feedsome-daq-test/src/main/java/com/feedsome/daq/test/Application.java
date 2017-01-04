package com.feedsome.daq.test;

import com.feedsome.daq.test.component.ComponentConfiguration;
import com.feedsome.daq.test.component.task.SchedulerConfiguration;
import com.feedsome.daq.test.route.RouteConfiguration;
import com.feedsome.daq.test.service.PluginRegistrationService;
import com.feedsome.daq.test.service.ServiceConfiguration;
import com.feedsome.model.PluginRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@Import({
        ServiceConfiguration.class,
        RouteConfiguration.class,
        ComponentConfiguration.class,
        SchedulerConfiguration.class
})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableConfigurationProperties({
        PluginProperties.class
})
public class Application implements CommandLineRunner {

    @Autowired
    private PluginProperties pluginProperties;

    @Autowired
    private PluginRegistrationService registrationService;


    public static void main(String[] args) {
        final long milis = System.currentTimeMillis();

        final ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        System.out.println("feedsome DAQ Test -- Started in " + (System.currentTimeMillis() - milis) + "(ms)");

        applicationContext.registerShutdownHook();
    }

    @Override
    public void run(String... args) throws Exception {
        final PluginRegistration plugin = new PluginRegistration();

        plugin.setName(pluginProperties.getName());
        plugin.getCategories().addAll(pluginProperties.getCategories());

        registrationService.register(plugin);
    }
}
