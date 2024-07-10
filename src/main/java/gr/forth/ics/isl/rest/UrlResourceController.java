package gr.forth.ics.isl.rest;

import com.google.zxing.WriterException;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
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
        if(newUrlResource.getOriginalUrl()==null && newUrlResource.getOriginalUrl().isBlank()){ //do not accept an empty URL
            httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);  
            return null;
        }else{  
            //search if the original URL already exists
            Optional<UrlResource> optUrlResource=this.service.findByUrl(newUrlResource.getOriginalUrl(),false);
            if(optUrlResource.isPresent()){
                optUrlResource.get().setMessage("Found an existing easy URL with the provided original URL");
                httpServletResponse.setStatus(httpServletResponse.SC_FOUND);
                return optUrlResource.get();
            }
            if(newUrlResource.getEasySuffix()!=null && !newUrlResource.getEasySuffix().isBlank()){
                if(newUrlResource.getEasySuffix().length()<EntityManager.CUSTOM_URL_MIN_LENGTH || newUrlResource.getEasySuffix().length()>EntityManager.CUSTOM_URL_MAX_LENGTH){
                    newUrlResource.setMessage("ERROR: incorrect length for easy url suffix. The accepted length is ["+EntityManager.CUSTOM_URL_MIN_LENGTH+","+EntityManager.CUSTOM_URL_MAX_LENGTH+"]");
                    httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
                    return newUrlResource;
                }
                optUrlResource=this.service.findByUrl(EntityManager.EASY_URL_PREFIX+newUrlResource.getEasySuffix(),true);
                if(optUrlResource.isPresent()){
                    newUrlResource.setMessage("ERROR: the provided easy suffix already exists. Please select another one");
                    httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
                    return newUrlResource;
                }
            }
            try{
                UrlResource createdUrlResource=new UrlResource(newUrlResource.getOriginalUrl(),newUrlResource.getEasySuffix());
                if(newUrlResource.getName()!=null && !newUrlResource.getName().isBlank()){
                    createdUrlResource.setName(newUrlResource.getName());
                }else{
                    createdUrlResource.setName("-");
                }
                if(newUrlResource.getDescription()!=null && !newUrlResource.getDescription().isBlank()){
                    createdUrlResource.setDescription(newUrlResource.getDescription());
                }else{
                    createdUrlResource.setDescription("-");
                }
                createdUrlResource.setCreated(Calendar.getInstance().getTime());
                createdUrlResource.setVisited(0);
                if(newUrlResource.getExpirationDate()!=null){
                    createdUrlResource.setExpirationDate(newUrlResource.getExpirationDate());
                }
                this.service.update(createdUrlResource);
                httpServletResponse.setStatus(httpServletResponse.SC_OK);
                createdUrlResource.setMessage("Successfully created easy URL");
                return createdUrlResource;
            }catch(IOException | WriterException ex){
                // add some logs here
                httpServletResponse.setStatus(httpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
    }
    
    @GetMapping("/qr/{suffix}")
    public @ResponseBody byte[] getQrCode(HttpServletResponse httpServletResponse, @PathVariable String suffix){
        Optional<UrlResource> optResource=this.service.findBySuffix(suffix);
        if(optResource.isPresent()){
            httpServletResponse.setStatus(httpServletResponse.SC_OK);
            return optResource.get().getQrCode();
        }else{
            httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}