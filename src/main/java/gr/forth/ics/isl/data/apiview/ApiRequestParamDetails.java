package gr.forth.ics.isl.data.apiview;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Simple POJO documenting the request parameters of various API methods
 * 
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor
public class ApiRequestParamDetails {
    private String parameterName;
    private String parameterType;
    private String parameterDescription;
    private String parameterExample;
}
