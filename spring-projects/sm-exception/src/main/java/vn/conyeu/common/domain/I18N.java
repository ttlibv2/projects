package vn.conyeu.common.domain;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.Nullable;
import vn.conyeu.commons.utils.Asserts;

import java.util.Locale;

public class I18N {
    public static MessageSourceAccessor messageSourceAccessor;

    public static MessageSourceAccessor message() {
        return Asserts.notNull(messageSourceAccessor);
    }
    
    /**
     * Retrieve the message for the given code and the default Locale.
     * @param code the code of the message
     * @param defaultMessage the String to return if the lookup fails
     * @return the message
     */
    public static String get(String code, String defaultMessage) {
        return message().getMessage(code, defaultMessage);
    }

    /**
     * Retrieve the message for the given code and the given Locale.
     * @param code the code of the message
     * @param defaultMessage the String to return if the lookup fails
     * @param locale the Locale in which to do lookup
     * @return the message
     */
    public static String get(String code, String defaultMessage, Locale locale) {
        return message().getMessage(code, defaultMessage, locale);
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     * @param code the code of the message
     * @param args arguments for the message, or {@code null} if none
     * @param defaultMessage the String to return if the lookup fails
     * @return the message
     */
    public static String get(String code, @Nullable Object[] args, String defaultMessage) {
        return message().getMessage(code, args, defaultMessage);
    }

    /**
     * Retrieve the message for the given code and the given Locale.
     * @param code the code of the message
     * @param args arguments for the message, or {@code null} if none
     * @param defaultMessage the String to return if the lookup fails
     * @param locale the Locale in which to do lookup
     * @return the message
     */
    public static String get(String code, @Nullable Object[] args, String defaultMessage, Locale locale) {
        String msg =  message().getMessage(code, args, defaultMessage, locale);
        return (msg != null ? msg : code);
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     * @param code the code of the message
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(String code) throws NoSuchMessageException {
        return  message().getMessage(code);
    }

    /**
     * Retrieve the message for the given code and the given Locale.
     * @param code the code of the message
     * @param locale the Locale in which to do lookup
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(String code, Locale locale) throws NoSuchMessageException {
        return  message().getMessage(code, locale);
    }

    /**
     * Retrieve the message for the given code and the default Locale.
     * @param code the code of the message
     * @param args arguments for the message, or {@code null} if none
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(String code, @Nullable Object[] args) throws NoSuchMessageException {
        return  message().getMessage(code, args);
    }

    /**
     * Retrieve the message for the given code and the given Locale.
     * @param code the code of the message
     * @param args arguments for the message, or {@code null} if none
     * @param locale the Locale in which to do lookup
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException {
        return  message().getMessage(code, args, locale);
    }

    /**
     * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance)
     * in the default Locale.
     * @param resolvable the MessageSourceResolvable
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return  message().getMessage(resolvable);
    }

    /**
     * Retrieve the given MessageSourceResolvable (e.g. an ObjectError instance)
     * in the given Locale.
     * @param resolvable the MessageSourceResolvable
     * @param locale the Locale in which to do lookup
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException if not found
     */
    public static String get(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return  message().getMessage(resolvable, locale);
    }
    
    
}