package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.views.MainLayout;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@PageTitle("EasyLink")
@Route(value = "api", layout = MainLayout.class)
public class ApiView extends Div {
    
    public ApiView(){
        IFrame iframe = new IFrame("https://app.swaggerhub.com/apis/YannisMarketakis/easylink/1.0.0");
        iframe.setSizeFull();
        add(iframe);
        setSizeFull();
    }
}
