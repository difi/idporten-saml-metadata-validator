package no.difi.config;

import no.difi.filevalidator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@PropertySource({"classpath:messages.properties", "classpath:validator.properties"})
public class MetadataConfig {
    @Autowired
    private Environment environment;

    @Bean
    public Validator validator(){
        return new Validator(environment);
    }

    @Bean
    public MetadataController metadataController(){
        return new MetadataController(validator());
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver () {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
