package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.views.MainLayout;

/** This view informs the users about the methods provided from the RESTfull API.
 *
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
@PageTitle("EasyLink")
@Route(value = "api", layout = MainLayout.class)
public class ApiView extends VerticalLayout {
    
    public ApiView(){
        add(createHeader());
        add(createMethodsDocumentation());
    }

    private Component createHeader(){
        HorizontalLayout headerLayout=new HorizontalLayout();
        Image img = new Image("images/api-logo.png", "RESTfull API");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.AUTO);
        getStyle().set("text-align", "justify");
        headerLayout.add(img,new H1("EasyLink RESTfull API"));
        headerLayout.setSizeFull();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return headerLayout;
    }

    private Component createMethodsDocumentation(){
        return new HorizontalLayout();
    }
}
