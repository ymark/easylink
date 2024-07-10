package gr.forth.ics.isl.rest;

import com.google.zxing.WriterException;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.data.UrlVisit;
import gr.forth.ics.isl.services.UrlResourceService;
import gr.forth.ics.isl.services.UrlVisitService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */
@RestController
public class UrlResourceController{
    private final UrlResourceService service;
    private final UrlVisitService visitService;
    
    public UrlResourceController(UrlResourceService srv, UrlVisitService visitSrv){
        this.service=srv;
        this.visitService=visitSrv;
    }

    @RequestMapping(value = "/r/{suffix}", method = RequestMethod.GET)
    public void method(HttpServletResponse httpServletResponse, @PathVariable String suffix) {
        Optional<UrlResource> optUrlResource=service.findByUrl(EntityManager.EASY_URL_PREFIX+suffix,true);
        if(optUrlResource.isPresent()){
            if(optUrlResource.get().isActive()) {
                UrlResource usedUrlResource = optUrlResource.get().visit();
                UrlVisit urlVisit=new UrlVisit(usedUrlResource.getEasyUrlSuffix(),usedUrlResource.getLastUsed());
                service.update(usedUrlResource);
                visitService.update(urlVisit);
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
    
    @GetMapping(value="/qr/{suffix}",produces=MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getQrCode(HttpServletResponse httpServletResponse, @PathVariable String suffix){
        Optional<UrlResource> optResource=this.service.findBySuffix(suffix);
        if(optResource.isPresent()){
            httpServletResponse.setStatus(httpServletResponse.SC_OK);
            httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+suffix+".png\"");
            return optResource.get().getQrCode();
        }else{
            httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
    
    @GetMapping("/find")
    public UrlResource findUrlResources(HttpServletResponse httpServletResponse, @RequestBody UrlResource queryResource){
        Optional<UrlResource> optUrlResource=this.service.findByObject(queryResource);
        if(optUrlResource==null){   // null is thrown when neither easy URL nor original URL was provided
            httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
            return null;
        }else{
            if(optUrlResource.isPresent()){
                httpServletResponse.setStatus(httpServletResponse.SC_OK);
                return optUrlResource.get();
            }else{
                httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
                return null;
            }
        }
    }
    
    @GetMapping("/visits/{suffix}")
    public List<UrlVisit> getUrlVisits(HttpServletResponse httpServletResponse,@PathVariable String suffix){
        List<UrlVisit> urlVisitList=this.visitService.findByEasySuffix(suffix);
        if(urlVisitList.isEmpty()){
            httpServletResponse.setStatus(httpServletResponse.SC_NOT_FOUND);
        }else{
            httpServletResponse.setStatus(httpServletResponse.SC_OK);
        }
        return urlVisitList;
    }
}