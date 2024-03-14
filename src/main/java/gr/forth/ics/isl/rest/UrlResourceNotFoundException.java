package gr.forth.ics.isl.rest;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */
public class UrlResourceNotFoundException extends RuntimeException{

    public UrlResourceNotFoundException(){
        super("The URL does not exist");
    }

    public UrlResourceNotFoundException(String suffix){
        super("The URL '"+suffix+"' does not exist");
    }
}