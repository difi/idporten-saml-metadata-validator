package no.difi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * Provide the {@link LocaleResolver} bean.
 */
@Configuration
@PropertySource("classpath:messages_no.properties")
@PropertySource("classpath:messages_en.properties")
public class LocalisationConfig {

    public static final String VALIDATOR_LOCALE = "VALIDATOR_LOCALE";

    private CookieLocaleResolver localeResolver;

    @PostConstruct
    private void post() {
        localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName(VALIDATOR_LOCALE);
        localeResolver.setDefaultLocale(Locale.forLanguageTag("no"));
    }

    @Bean
    public LocaleResolver localeResolver() {
        return localeResolver;
    }
}
