package no.difi.application;

import no.difi.config.MetadataConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

public class Application {
    public static final String ROOT=System.getProperty("user.dir");

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(MetadataConfig.class, args);
    }
}
