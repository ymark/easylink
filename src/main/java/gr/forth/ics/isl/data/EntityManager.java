package gr.forth.ics.isl.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Component
public class EntityManager {
    public static String EASY_URL_PREFIX;

    public EntityManager(@Value("${gr.forth.ics.isl.easylink.prefix}") String easyUrl){
        EASY_URL_PREFIX=easyUrl;
    }
}
