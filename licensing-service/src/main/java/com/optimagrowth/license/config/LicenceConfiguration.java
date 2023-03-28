package com.optimagrowth.license.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author: Ezekiel Eromosei
 * @created: 28 March 2023
 */

@Configuration
public class LicenceConfiguration {
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);  // don't throw an error if a message isn't found instead it returns the message code
        //i.e 'license.creates.message' compared to  "No message found under code 'license.creates.message' for locale 'es'
        messageSource.setBasenames("messages");
        return messageSource;
    }
}
