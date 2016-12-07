package com.feedsome.daq.test;

import com.feedsome.daq.test.route.configuration.RouteConfiguration;
import com.feedsome.daq.test.service.PluginRegistrationService;
import com.feedsome.daq.test.service.ServiceConfiguration;
import com.feedsome.model.PluginRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        ServiceConfiguration.class,
        RouteConfiguration.class
})
@EnableConfigurationProperties(PluginProperties.class)
public class Application implements CommandLineRunner {

    @Autowired
    private PluginProperties pluginProperties;

    @Autowired
    private PluginRegistrationService registrationService;


    public static void main(String[] args) {
        final long milis = System.currentTimeMillis();

        final ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        System.out.println("feedsome DAQ Test -- Started in " + (System.currentTimeMillis() - milis) + "(ms)");

        // TODO: check how the above interacts with apache camel
        applicationContext.close();
    }

    @Override
    public void run(String... args) throws Exception {
        final PluginRegistration plugin = new PluginRegistration();

        plugin.setName(pluginProperties.getName());
        plugin.getCategories().addAll(pluginProperties.getCategories());

        registrationService.register(plugin);
    }
}
