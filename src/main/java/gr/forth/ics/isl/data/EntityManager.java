package gr.forth.ics.isl.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Component
public class EntityManager {
    public static String EASY_URL_PREFIX;
    public static int ORIGINAL_URL_MAX_LENGTH;
    public static int NAME_MAX_LENGTH;
    public static int DESCRIPTION_MAX_LENGTH;
    public static int EASY_URL_SUFFIX_LENGTH;

    public EntityManager(@Value("${gr.forth.ics.isl.easylink.prefix}") String easyUrl,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.originalurl}") String origUrlMaxLenght,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.name}") String nameMaxLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.max.description}") String descrMaxLength,
                         @Value("${gr.forth.ics.isl.easylink.lengths.easylinksuffix}") String easyUrlSuffixLength){
        EASY_URL_PREFIX=easyUrl;
        ORIGINAL_URL_MAX_LENGTH=Integer.parseInt(origUrlMaxLenght);
        NAME_MAX_LENGTH=Integer.parseInt(nameMaxLength);
        DESCRIPTION_MAX_LENGTH=Integer.parseInt(descrMaxLength);
        EASY_URL_SUFFIX_LENGTH=Integer.parseInt(easyUrlSuffixLength);
    }
}
