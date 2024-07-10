package gr.forth.ics.isl.services;

import gr.forth.ics.isl.data.UrlVisit;
import gr.forth.ics.isl.data.UrlVisitRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UrlVisitService {

    @Autowired
    private final UrlVisitRepository repository;

    public UrlVisitService(UrlVisitRepository repository) {
        this.repository = repository;
    }

    public Optional<UrlVisit> get(Long id) {
        return repository.findById(id);
    }

    public UrlVisit update(UrlVisit entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UrlVisit> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UrlVisit> list(Pageable pageable, Specification<UrlVisit> filter) {
        return repository.findAll(filter, pageable);
    }
    
    public List<UrlVisit> getAll(){
        return this.repository.findAll();
    }
    
    public List<UrlVisit> findByEasySuffix(String suffix){
        return this.repository.findByEasySuffix(suffix);
    }

    public int count() {
        return (int) repository.count();
    }

}
