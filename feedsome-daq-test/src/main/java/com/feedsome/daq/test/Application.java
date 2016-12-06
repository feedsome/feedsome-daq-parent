package com.feedsome.daq.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final long milis = System.currentTimeMillis();

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        System.out.println("feedsome DAQ Test -- Started in " + (System.currentTimeMillis() - milis) + "(ms)");

        // TODO: check how the above interacts with apache camel
        applicationContext.close();
    }
}
