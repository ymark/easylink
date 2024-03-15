package gr.forth.ics.isl.data;

import gr.forth.ics.isl.rest.UrlResourceController;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class UrlResource extends AbstractEntity{
    @Column(name="original_url", length = 5000)
    private String originalUrl;
    private String easyUrl;
    private String name;
    @Column(name="description", length = 5000)
    private String description;
    private Date created;
    private Date lastUsed;
    private int visited;

    public UrlResource(String url){
        this.originalUrl=url;
        this.generateEasyUrl();
    }

    public LocalDate getCreatedDateLocal() {
        return this.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getLastUsedDateLocal(){
        return this.getLastUsed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String generateEasyUrl(){
        this.easyUrl=EntityManager.EASY_URL_PREFIX+RandomStringUtils.randomAlphanumeric(10);
        return this.easyUrl;
    }
}
