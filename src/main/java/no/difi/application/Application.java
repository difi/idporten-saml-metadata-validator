package no.difi.application;

import com.google.common.collect.ImmutableList;
import no.difi.config.LocalisationConfig;
import no.difi.config.MetadataConfig;
import org.springframework.boot.SpringApplication;

public class Application {
    public static final String ROOT=System.getProperty("user.dir");

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(
                ImmutableList.of(MetadataConfig.class, LocalisationConfig.class).toArray(),
                args);
    }
}
