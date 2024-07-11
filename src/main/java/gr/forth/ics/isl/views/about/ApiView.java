package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.data.apiview.ApiRequestParamDetails;
import gr.forth.ics.isl.data.apiview.ApiResponseCodeDetails;
import gr.forth.ics.isl.views.MainLayout;
import java.util.ArrayList;
import java.util.List;

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
        
        Span requestParametersTitle=new Span("Request Parameters");
        requestParametersTitle.getElement().getStyle().set("text-decoration", "underline");
        List<ApiRequestParamDetails> requestList=new ArrayList<>();
        requestList.add(new ApiRequestParamDetails("suffix","path variable", "The suffix of an easy link (abcd from https://link-it.gr/r/abcd)", "abcd"));
        Grid<ApiRequestParamDetails> requestParamsGrid=new Grid<>(ApiRequestParamDetails.class,false);
        requestParamsGrid.addColumn(ApiRequestParamDetails::getParameterName).setHeader("Parameter").setAutoWidth(true);
        requestParamsGrid.addColumn(ApiRequestParamDetails::getParameterType).setHeader("Type").setAutoWidth(true);
        requestParamsGrid.addColumn(ApiRequestParamDetails::getParameterDescription).setHeader("Description").setAutoWidth(true);
        requestParamsGrid.setItems(requestList);
        requestParamsGrid.setHeight(100,Unit.PIXELS);
        
        Span responseCodesTitle=new Span("Response Codes");
        responseCodesTitle.getElement().getStyle().set("text-decoration", "underline");
        List<ApiResponseCodeDetails> responseCodesList=new ArrayList<>();
        responseCodesList.add(new ApiResponseCodeDetails(200, "The easy link was found and its details are provided in JSON format"));
        responseCodesList.add(new ApiResponseCodeDetails(404, "The easy link was not found"));
        Grid<ApiResponseCodeDetails> responseCodesGrid=new Grid<>();
        responseCodesGrid.addComponentColumn(report -> createResponseCodeBadge(report.getResponceCode())).setHeader("Response code").setAutoWidth(true);
        responseCodesGrid.addColumn(ApiResponseCodeDetails::getResponseCondition).setHeader("Condition").setAutoWidth(true);
        responseCodesGrid.setItems(responseCodesList);
        responseCodesGrid.setHeight(200,Unit.PIXELS);
        
        Span responseResultTitle=new Span("Response Result");
        responseResultTitle.getElement().getStyle().set("text-decoration", "underline");
        String jsonResultsSample="""
        {
            "originalUrl": "https://www.example.com/my-example-page",   
            "easyUrl": "https://link-it.gr/r/abcd",
            "easySuffix": "abcd",
            "name": "My easy link",
            "description": "This is an example of an easy link used for testing purposes",
            "created": "2024-01-15T14:17:18.295+00:00",
            "lastUsed": "2024-02-28T14:42:43.995+00:00",
            "visited": 15,
            "customUrlSuffix": true,
            "expirationDate": "2030-01-01T22:00:00.000+00:00",
            "message": null,
            "active": true
        }
        """;
        Html responseResults=new Html("<pre>"+jsonResultsSample+"</pre>");
        UnorderedList fieldsDoc=new UnorderedList();
        fieldsDoc.add(new ListItem("originalUrl: the original URL that was used for constructing the easy link"));
        fieldsDoc.add(new ListItem("easyUrl: the easy link"));
        fieldsDoc.add(new ListItem("easySuffix: the suffix part of the easy link"));
        fieldsDoc.add(new ListItem("name: the name that was given when easy link was constructed, if it was provided."));
        fieldsDoc.add(new ListItem("description: a short narrative describing the easy link, if it was provided during the construction."));
        fieldsDoc.add(new ListItem("created: a detailed timestamp describing when easy link was created"));
        fieldsDoc.add(new ListItem("lastUsed: a detailed timestamp describing when was the last time this easy link was used. If it was never used this value will be null"));
        fieldsDoc.add(new ListItem("visited: a number indicating how many times this easy link was used"));
        fieldsDoc.add(new ListItem("customUrlSuffix: if true this means that this easy link was defined by the user"));
        fieldsDoc.add(new ListItem("expirationDate: a detailed timestamp indicating when easy link is about to expire. If no expiration date was defined this value will be null"));
        fieldsDoc.add(new ListItem("message: message thrown by the backend service (usually used for errors)"));
        fieldsDoc.add(new ListItem("active: indicates if the easy link can be used. Only expired easy links are inactive"));
        
        Details contentsDocumentationDetails=new Details("JSON Fields description", fieldsDoc);
        contentsDocumentationDetails.setOpened(true);
        contentsDocumentationDetails.addThemeVariants(DetailsVariant.SMALL);
        
        documentationLayout.add(getBadge,configurationSpan,descriptionSpan,
                                requestParametersTitle,requestParamsGrid,
                                responseCodesTitle,responseCodesGrid,
                                responseResultTitle,responseResults, 
                                contentsDocumentationDetails);
        return documentationLayout;
    }
    
    private Span createResponseCodeBadge(int responseCode){
        String theme="";
        switch(responseCode){
            case 200:
                theme="badge success primary";
                break;
            case 400:
            case 404:
                theme="badge error primary";
                break;
            case 500:
                theme="badge contrast primary";
                break;
        }
        Span badge=new Span(String.valueOf(responseCode));
        badge.getElement().getThemeList().add(theme);
        return badge;
    }
}
