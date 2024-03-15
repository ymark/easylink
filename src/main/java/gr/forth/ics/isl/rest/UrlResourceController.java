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
            UrlResource usedUrlResource=optUrlResource.get().visit();
            service.update(usedUrlResource);
            httpServletResponse.setHeader("Location", usedUrlResource.getOriginalUrl());
            httpServletResponse.setStatus(302);
        }else{
            //should I show a default 404 page ?
            httpServletResponse.setStatus(404);
        }
    }

    @RequestMapping(value = "/qr/{id}", method = RequestMethod.GET)
    public void methodQr(HttpServletResponse httpServletResponse, @PathVariable String id) {
        String filePath=EntityManager.QR_FOLDER+"/"+id;
        File file = new File(filePath);
        httpServletResponse.setContentType("image/png");
        httpServletResponse.setHeader("Content-Disposition", "inline; filename="+id);
        httpServletResponse.setContentLength((int)file.length());
        byte[] imageData=new byte[(int)file.length()];
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            bis.read(imageData);
        }catch(IOException ex){
            System.out.println("fnf "+ex.toString());
        }
        try (ServletOutputStream outputStream = httpServletResponse.getOutputStream()) {
            outputStream.write(imageData);
        }
        catch(IOException ex){
            System.out.println("resp "+ex.toString());
        }
    }
}