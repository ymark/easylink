package gr.forth.ics.isl.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.zxing.WriterException;
import gr.forth.ics.isl.views.Common;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import javax.imageio.ImageIO;
import lombok.EqualsAndHashCode;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Data @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(callSuper=false)
@Entity
public class UrlResource extends AbstractEntity{
    @Column(name="original_url", length = 5000)
    private String originalUrl;
    private String easyUrl;
    private String easySuffix;
    private String name;
    @Column(name="description", length = 5000)
    private String description;
    private Date created;
    private Date lastUsed;
    private int visited;
    @Column(name="qr_code", length = 4096) @JsonIgnore
    private byte[] qrCode;
    private boolean customUrlSuffix;
    private Date expirationDate;
    @Transient
    private String message;

    public UrlResource(String url, String customSuffix) throws IOException, WriterException {
        this.originalUrl=url;
        if(customSuffix!=null){
            this.easyUrl=EntityManager.EASY_URL_PREFIX+customSuffix;
            this.easySuffix=customSuffix;
            this.customUrlSuffix=true;
        }else {
            this.generateEasyUrl();
            this.easySuffix=this.getEasyUrlSuffix();
            this.customUrlSuffix=false;
        }
        this.generateQrCode();
    }
    
    @JsonIgnore
    public LocalDate getCreatedDateLocal() {
        return this.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @JsonIgnore
    public LocalDate getLastUsedDateLocal(){
        return (this.getLastUsed()==null)?null:this.getLastUsed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @JsonIgnore
    public LocalDate getExpirationDateLocal(){
        return (this.getExpirationDate()==null)?null:this.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public String generateEasyUrl(){
        this.easyUrl=EntityManager.EASY_URL_PREFIX+RandomStringUtils.randomAlphanumeric(EntityManager.EASY_URL_SUFFIX_LENGTH);
        return this.easyUrl;
    }

    @JsonIgnore
    public String getEasyUrlSuffix(){
        return this.easyUrl.replace(EntityManager.EASY_URL_PREFIX,"");
    }

    @JsonIgnore
    public String getQrFilename(){
        return this.getEasyUrlSuffix()+".png";
    }

    private void generateQrCode() throws WriterException, IOException {
        BufferedImage qrCodeImage=Common.createQrImage(this.easyUrl);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage,"png",baos);
        this.qrCode=baos.toByteArray();
    }

    public boolean isActive(){
        if(this.expirationDate==null){
            return true;
        }else{
            return Calendar.getInstance().getTime().before(this.expirationDate);
        }
    }

    public UrlResource visit(){
        this.lastUsed= Calendar.getInstance().getTime();
        this.visited+=1;
        return this;
    }
}
