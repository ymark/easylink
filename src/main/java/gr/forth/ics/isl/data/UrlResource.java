package gr.forth.ics.isl.data;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class UrlResource extends AbstractEntity{
    private String originalUrl;
    private String shortUrl;
    private String name;
    private String description;
    private Date created;
    private Date lastUsed;
    private int visited;
}
