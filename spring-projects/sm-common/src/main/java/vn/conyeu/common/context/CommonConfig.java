package vn.conyeu.common.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import vn.conyeu.common.domain.I18N;
import vn.conyeu.commons.utils.MapperHelper;

@Configuration
public class CommonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return MapperHelper.mapper();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        MessageSourceAccessor accessor = new MessageSourceAccessor(messageSource());
        I18N.messageSourceAccessor = accessor;
        return accessor;
    }
}