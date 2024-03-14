package gr.forth.ics.isl.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UrlResourceRepository
        extends
            JpaRepository<UrlResource, Long>,
            JpaSpecificationExecutor<UrlResource> {

}
