package gr.forth.ics.isl.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class UrlResource extends AbstractEntity{
    @Column(name="original_url", length = 5000)
    private String originalUrl;
    private String shortUrl;
    private String name;
    @Column(name="description", length = 5000)
    private String description;
    private Date created;
    private Date lastUsed;
    private int visited;

    public LocalDate getCreatedDateLocal() {
        return this.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getLastUsedDateLocal(){
        return this.getLastUsed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String shortenUrl(){
        this.shortUrl="https://isl.ics.forth.gr/easylink/r/"+RandomStringUtils.randomAlphanumeric(10);
        return this.shortUrl;
    }
}
