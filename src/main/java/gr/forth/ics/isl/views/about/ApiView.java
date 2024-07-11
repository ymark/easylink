package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
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
        add(introductionDescription());
        add(createMethodsDocumentation());
    }

    private Component createHeader(){
        HorizontalLayout headerLayout=new HorizontalLayout();
        Image img = new Image("images/api-logo-150.png", "RESTfull API");
        setDefaultHorizontalComponentAlignment(Alignment.AUTO);
        getStyle().set("text-align", "justify");
        headerLayout.add(img,new H1("EasyLink RESTfull API"));
        headerLayout.setSizeFull();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return headerLayout;
    }
    
    private Component introductionDescription(){
        Div text=new Div("Easy links can be created and managed through a RESTfull API. "
                        +"Below we provide the documentation for using the various methods of the API");
        return text;
    }

    private Component createMethodsDocumentation(){
        Accordion methodsAccordion=new Accordion();
        methodsAccordion.add("GET easy link details",createGetEasyWithSuffixComponent());
        
        
        return methodsAccordion;
    }
    
    private Component createGetEasyWithSuffixComponent(){
        VerticalLayout documentationLayout=new VerticalLayout();
        Span getBadge=new Span("GET method");
        getBadge.getElement().getThemeList().add("badge contrast primary");
        
        Span configurationSpan=new Span("/easy/{easy_suffix}");
        configurationSpan.getElement().getStyle().set("font-family", "'Courier New', monospace");
        
        Span descriptionSpan=new Span("This method allows you to fetch all the details of an easylink using only its suffix. "
                                     +"If the easy link exists, its details are returned in the form of a JSON object as described below");
        
        
        
        documentationLayout.add(getBadge,configurationSpan,descriptionSpan);
        return documentationLayout;
    }
}
