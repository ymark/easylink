package gr.forth.ics.isl.data;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(callSuper=false)
@Entity
public class UrlVisit extends AbstractEntity{
    private String easySuffix;
    private Date visitDate;
}