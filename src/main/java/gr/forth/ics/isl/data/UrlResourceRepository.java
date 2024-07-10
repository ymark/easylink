package gr.forth.ics.isl.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UrlResourceRepository
        extends
            JpaRepository<UrlResource, Long>,
            JpaSpecificationExecutor<UrlResource> {

    Optional<UrlResource> findByEasyUrl(String easyUrl);

    Optional<UrlResource> findByOriginalUrl(String originalUrl);
    
    Optional<UrlResource> findByOriginalUrlAndEasyUrl(String originalUrl,String easyUrl);
}
