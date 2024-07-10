package gr.forth.ics.isl.rest;

import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */
@RestController
public class UrlResourceController{
    private final UrlResourceService service;

    public UrlResourceController(UrlResourceService srv){
        this.service=srv;
    }

    @RequestMapping(value = "/r/{suffix}", method = RequestMethod.GET)
    public void method(HttpServletResponse httpServletResponse, @PathVariable String suffix) {
        Optional<UrlResource> optUrlResource=service.findByUrl(EntityManager.EASY_URL_PREFIX+suffix,true);
        if(optUrlResource.isPresent()){
            if(optUrlResource.get().isActive()) {
                UrlResource usedUrlResource = optUrlResource.get().visit();
                service.update(usedUrlResource);
                httpServletResponse.setHeader("Location", usedUrlResource.getOriginalUrl());
                httpServletResponse.setStatus(httpServletResponse.SC_FOUND);
            }else{
                httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
            }
        }else{
            //should I show a default 404 page ?
            httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @GetMapping("/all")
    public List<UrlResource> all(){
        return this.service.getAll();
    }
    
    @GetMapping("/easy/{suffix}")
    public UrlResource getUrlResource(HttpServletResponse httpServletResponse, @PathVariable String suffix){
        Optional<UrlResource> optResource=this.service.findBySuffix(suffix);
        if(optResource.isPresent()){
            httpServletResponse.setStatus(httpServletResponse.SC_OK);
            return optResource.get();
        }else{
            httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
    
    @PostMapping("/easy")
    UrlResource newUrlResource(HttpServletResponse httpServletResponse, @RequestBody UrlResource newUrlResource){
        if(newUrlResource.getOriginalUrl()==null){
            httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
        }
        
        return newUrlResource;
    }
}