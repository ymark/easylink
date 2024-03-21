package gr.forth.ics.isl.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Component
public class EntityManager {
    public static String EASY_URL_PREFIX;
    public static String APP_URL;
    public static int ORIGINAL_URL_MAX_LENGTH;
    public static int NAME_MAX_LENGTH;
    public static int DESCRIPTION_MAX_LENGTH;
    public static int CUSTOM_URL_MIN_LENGTH;
    public static int CUSTOM_URL_MAX_LENGTH;
    public static int EASY_URL_SUFFIX_LENGTH;
    public static String QR_URL_PREFIX;

    public EntityManager(@Value("${gr.forth.ics.isl.easylink.prefix}") String easyUrl,
                         @Value("${gr.forth.ics.isl.easylink.appurl}") String appUrl,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.originalurl}") String origUrlMaxLenght,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.name}") String nameMaxLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.description}") String descrMaxLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.easylinksuffix}") String easyUrlSuffixLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.min.customurl}") String customUrlMinLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.customurl}") String customUrlMaxLength){
        EASY_URL_PREFIX=easyUrl;
        APP_URL=appUrl;
        ORIGINAL_URL_MAX_LENGTH=Integer.parseInt(origUrlMaxLenght);
        NAME_MAX_LENGTH=Integer.parseInt(nameMaxLength);
        DESCRIPTION_MAX_LENGTH=Integer.parseInt(descrMaxLength);
        EASY_URL_SUFFIX_LENGTH=Integer.parseInt(easyUrlSuffixLength);
        CUSTOM_URL_MIN_LENGTH=Integer.parseInt(customUrlMinLength);
        CUSTOM_URL_MAX_LENGTH=Integer.parseInt(customUrlMaxLength);
    }
}
