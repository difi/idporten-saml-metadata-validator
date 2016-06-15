package no.difi.config;

import no.difi.filevalidator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:messages.properties")
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
}
