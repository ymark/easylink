package gr.forth.ics.isl.services;

import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.data.UrlResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UrlResourceService {

    @Autowired
    private final UrlResourceRepository repository;

    public UrlResourceService(UrlResourceRepository repository) {
        this.repository = repository;
    }

    public Optional<UrlResource> get(Long id) {
        return repository.findById(id);
    }

    public UrlResource update(UrlResource entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<UrlResource> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<UrlResource> list(Pageable pageable, Specification<UrlResource> filter) {
        return repository.findAll(filter, pageable);
    }

    public Optional<UrlResource> findByUrl(String givenUrl){
        UrlResource exampleResource=new UrlResource();
        exampleResource.setOriginalUrl(givenUrl);
        return repository.findOne(Example.of(exampleResource));
    }

    public int count() {
        return (int) repository.count();
    }

}
