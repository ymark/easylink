package gr.forth.ics.isl.services;

import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.data.UrlResourceRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
    
    public List<UrlResource> getAll(){
        return this.repository.findAll();
    }

    public Optional<UrlResource> findByUrl(String givenUrl, boolean isShortUrl){
        if(isShortUrl) {
            return repository.findByEasyUrl(givenUrl);
        }else{
            return repository.findByOriginalUrl(givenUrl);
        }
    }
    
    public Optional<UrlResource> findBySuffix(String suffix){
        return this.repository.findByEasyUrl(EntityManager.EASY_URL_PREFIX+suffix);
    }
    
    public Optional<UrlResource> findByObject(UrlResource givenResource){
        if(givenResource.getOriginalUrl()!=null && givenResource.getEasyUrl()!=null){
            return this.repository.findByOriginalUrlAndEasyUrl(givenResource.getOriginalUrl(), givenResource.getEasyUrl());
        }else if(givenResource.getOriginalUrl()!=null && givenResource.getEasyUrl()==null){
            return this.repository.findByOriginalUrl(givenResource.getOriginalUrl());
        }else if(givenResource.getOriginalUrl()==null && givenResource.getEasyUrl()!=null){
            return this.repository.findByEasyUrl(givenResource.getEasyUrl());
        }else{
            return null;
        }
    }

    public int count() {
        return (int) repository.count();
    }

}
