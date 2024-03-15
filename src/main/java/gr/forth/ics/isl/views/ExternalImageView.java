package gr.forth.ics.isl.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.data.EntityManager;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@Route("external-image")
public class ExternalImageView extends VerticalLayout {
    public ExternalImageView(String url, String qrSuffix){
        String imageUrl= EntityManager.QR_URL_PREFIX+qrSuffix;
        Image qrImage=new Image(imageUrl,url);
        qrImage.setWidth("80px");
        qrImage.setHeight("80px");
        add(qrImage);
    }
}
