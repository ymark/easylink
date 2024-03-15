package gr.forth.ics.isl.rest;

import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.data.UrlResourceRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */
@RestController
public class UrlResourceController{
    private final UrlResourceRepository repository;
    @Value("${gr.forth.ics.isl.easylink.prefix}")
    private String urlPrefix;

    public UrlResourceController(UrlResourceRepository repo){
        this.repository=repo;
    }

    /* Currently not used*/
    @GetMapping("/urlresources")
    public void all(){
        List<UrlResource> allResources=this.repository.findAll();
        System.out.println(allResources.size());
    }

    @RequestMapping(value = "/r/{suffix}", method = RequestMethod.GET)
    public void method(HttpServletResponse httpServletResponse, @PathVariable String suffix) {
        System.out.println(urlPrefix);
        System.out.println("search for url with suffix "+suffix);
        //search for the URL here
        httpServletResponse.setHeader("Location", "https://www.google.com");
        httpServletResponse.setStatus(302);
    }


}
