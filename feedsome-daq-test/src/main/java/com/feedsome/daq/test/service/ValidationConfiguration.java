package com.feedsome.daq.test.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * Validation configuration that defines and exposes beans,
 * that handle the JSR-303 {@link javax.validation.Validator}.
 */
@Configuration
public class ValidationConfiguration {

    @Bean(name = "localValidatorFactoryBean")
    public SpringValidatorAdapter validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}
