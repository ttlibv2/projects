package vn.conyeu.common.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import vn.conyeu.common.domain.I18N;
import vn.conyeu.commons.utils.MapperHelper;

import java.util.List;
import java.util.Locale;

@Configuration
public class CommonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return MapperHelper.mapper();
    }

//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasenames("messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localResolver = new SessionLocaleResolver();
        localResolver.setDefaultLocale(Locale.of("vi", "VN"));
        return localResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(10); //reload messages every 10 seconds
        messageSource.setFileExtensions(List.of(".properties", ".xml", ".json"));
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource());
        I18N.messageSourceAccessor = accessor;
        return accessor;
    }
}