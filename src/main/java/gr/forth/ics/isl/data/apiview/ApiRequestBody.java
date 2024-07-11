package gr.forth.ics.isl.data.apiview;

import lombok.Data;

/** Simple POJO documenting the request body of various API methods
 * 
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data 
public class ApiRequestBody {
    private String mediaType;
    private String example;
}
