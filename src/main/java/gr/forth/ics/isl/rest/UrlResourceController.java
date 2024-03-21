package gr.forth.ics.isl.rest;

import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.io.*;
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
                httpServletResponse.setStatus(302);
            }else{
                httpServletResponse.setStatus(404);
            }
        }else{
            //should I show a default 404 page ?
            httpServletResponse.setStatus(404);
        }
    }
}