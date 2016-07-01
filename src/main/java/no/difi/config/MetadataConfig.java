package no.difi.config;

import no.difi.controller.MetadataController;
import no.difi.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableAutoConfiguration
@Configuration
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:validator.properties")
@PropertySource("classpath:application.properties")
public class MetadataConfig {
    private static final String VIEW_PREFIX = "spring.mvc.view.prefix";
    private static final String VIEW_SUFFIX = "spring.mvc.view.suffix";

    @Autowired
    private Environment environment;

    @Bean
    public ValidatorService validator(){
        return new ValidatorService(environment);
    }

    @Bean
    public MetadataController metadataController(){
        return new MetadataController(validator());
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver () {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(environment.getRequiredProperty(VIEW_PREFIX));
        viewResolver.setSuffix(environment.getRequiredProperty(VIEW_SUFFIX));
        return viewResolver;
    }
}
