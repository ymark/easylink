package gr.forth.ics.isl.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UrlVisitRepository
        extends
            JpaRepository<UrlVisit, Long>,
            JpaSpecificationExecutor<UrlVisit> {

    List<UrlVisit> findByEasySuffix(String easySuffix);
}
