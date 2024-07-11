package gr.forth.ics.isl.data.apiview;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Simple POJO documenting the HTTP response codes and the corresponding parameters types
 * 
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor
public class ApiResponseCodeDetails {
    private int responceCode;
    private String responseCondition;
}
