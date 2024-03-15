package gr.forth.ics.isl.rest;

import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.data.UrlResourceRepository;
import gr.forth.ics.isl.services.UrlResourceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        System.out.println("Found it "+optUrlResource.isPresent());
        if(optUrlResource.isPresent()){
            UrlResource usedUrlResource=optUrlResource.get().visit();
            service.update(usedUrlResource);
            httpServletResponse.setHeader("Location", usedUrlResource.getOriginalUrl());
            httpServletResponse.setStatus(302);
        }else{
            //should I show a default 404 page ?
            httpServletResponse.setStatus(404);
        }
    }
}