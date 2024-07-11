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
        methodsAccordion.add("GET QR code of easy link",createGetQrComponent());
        methodsAccordion.add("FIND an easy link",createFindComponent());
        methodsAccordion.add("GET all visits of an easy link",createGetVisitsComponent());
        methodsAccordion.add("CREATE an easy link",createCreateComponent());
        methodsAccordion.close();
        
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
    
    private Component createGetQrComponent(){
        VerticalLayout documentationLayout=new VerticalLayout();
        Span getBadge=new Span("GET method");
        getBadge.getElement().getThemeList().add("badge contrast primary");
        
        Span configurationSpan=new Span("/qr/{easy_suffix}");
        configurationSpan.getElement().getStyle().set("font-family", "'Courier New', monospace");
        
        Span descriptionSpan=new Span("This method allows you to retrieve and download the QR code of an easy link in the form of a PNG image");
        
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
        responseCodesList.add(new ApiResponseCodeDetails(200, "The easy link was found and its QR code is available in PNG format (image/png)"));
        responseCodesList.add(new ApiResponseCodeDetails(404, "The easy link was not found"));
        Grid<ApiResponseCodeDetails> responseCodesGrid=new Grid<>();
        responseCodesGrid.addComponentColumn(report -> createResponseCodeBadge(report.getResponceCode())).setHeader("Response code").setAutoWidth(true);
        responseCodesGrid.addColumn(ApiResponseCodeDetails::getResponseCondition).setHeader("Condition").setAutoWidth(true);
        responseCodesGrid.setItems(responseCodesList);
        responseCodesGrid.setHeight(200,Unit.PIXELS);
        
        documentationLayout.add(getBadge,configurationSpan,descriptionSpan,
                                requestParametersTitle,requestParamsGrid,
                                responseCodesTitle,responseCodesGrid);
        return documentationLayout;
    }
    
    private Component createFindComponent(){
        VerticalLayout documentationLayout=new VerticalLayout();
        Span getBadge=new Span("GET method");
        getBadge.getElement().getThemeList().add("badge contrast primary");
        
        Span configurationSpan=new Span("/find");
        configurationSpan.getElement().getStyle().set("font-family", "'Courier New', monospace");
        
        Span descriptionSpan=new Span("This method allows you to search for an easy link and get its details usinf either the original URL"
                                     +", the easy URL or their combinations. These URLs are provided as a JSON object as shown below.");
        
        Span requestBodyTitle=new Span("Request Body");
        requestBodyTitle.getElement().getStyle().set("text-decoration", "underline");
        String jsonRequestBodySample="""
        {
            "originalUrl": "https://www.example.com/my-example-page",   
            "easyUrl": "https://link-it.gr/r/abcd",
        }
        """;
        Html requestBodyFields=new Html("<pre>"+jsonRequestBodySample+"</pre>");
        UnorderedList requestFieldsDoc=new UnorderedList();
        requestFieldsDoc.add(new ListItem("originalUrl: the original URL that was used for constructing the easy link, or null (for not searching using that)"));
        requestFieldsDoc.add(new ListItem("easyUrl: the easy link, or null for not searching using that)"));
        Details requestBodyFieldsDocumentationDetails=new Details("JSON Fields description", requestFieldsDoc);
        requestBodyFieldsDocumentationDetails.setOpened(true);
        requestBodyFieldsDocumentationDetails.addThemeVariants(DetailsVariant.SMALL);
        
        Span responseCodesTitle=new Span("Response Codes");
        responseCodesTitle.getElement().getStyle().set("text-decoration", "underline");
        List<ApiResponseCodeDetails> responseCodesList=new ArrayList<>();
        responseCodesList.add(new ApiResponseCodeDetails(200, "The easy link was found and its details are provided in JSON format"));
        responseCodesList.add(new ApiResponseCodeDetails(400, "The provided json object did not contain any value (neither for originalUrl, nor for easyUrl)"));
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
                                requestBodyTitle,requestBodyFields,requestBodyFieldsDocumentationDetails,
                                responseCodesTitle,responseCodesGrid,
                                responseResultTitle,responseResults, 
                                contentsDocumentationDetails);
        return documentationLayout;
    }
    
    private Component createGetVisitsComponent(){
        VerticalLayout documentationLayout=new VerticalLayout();
        Span getBadge=new Span("GET method");
        getBadge.getElement().getThemeList().add("badge contrast primary");
        
        Span configurationSpan=new Span("/visit/{easy_suffix}");
        configurationSpan.getElement().getStyle().set("font-family", "'Courier New', monospace");
        
        Span descriptionSpan=new Span("This method allows you to get the details of the visits of your easy link. "
                                     +"More specifically, it returns all the detailed timstamps indicating when easy link was used.");
        
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
        responseCodesList.add(new ApiResponseCodeDetails(200, "The easy link was found and the timestamps of its visits are returned"));
        responseCodesList.add(new ApiResponseCodeDetails(404, "The easy link was not found"));
        Grid<ApiResponseCodeDetails> responseCodesGrid=new Grid<>();
        responseCodesGrid.addComponentColumn(report -> createResponseCodeBadge(report.getResponceCode())).setHeader("Response code").setAutoWidth(true);
        responseCodesGrid.addColumn(ApiResponseCodeDetails::getResponseCondition).setHeader("Condition").setAutoWidth(true);
        responseCodesGrid.setItems(responseCodesList);
        responseCodesGrid.setHeight(200,Unit.PIXELS);
        
        Span responseResultTitle=new Span("Response Result");
        responseResultTitle.getElement().getStyle().set("text-decoration", "underline");
        String jsonResultsSample="""
        [
            {
                "easySuffix": "abcd",
                "visitDate": "2024-01-15T14:17:18.295+00:00",
            },
            {
                "easySuffix": "abcd",
                "visitDate": "2024-02-28T14:42:43.995+00:00",
            },
        ]
        """;
        Html responseResults=new Html("<pre>"+jsonResultsSample+"</pre>");
        UnorderedList fieldsDoc=new UnorderedList();
        fieldsDoc.add(new ListItem("easySuffix: the suffix part of the easy link"));
        fieldsDoc.add(new ListItem("visitDate: a detailed timestamp describing when this easy link was used."));
        
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
    
    private Component createCreateComponent(){
        VerticalLayout documentationLayout=new VerticalLayout();
        Span postBadge=new Span("POST method");
        postBadge.getElement().getThemeList().add("badge contrast primary");
        
        Span configurationSpan=new Span("/easy");
        configurationSpan.getElement().getStyle().set("font-family", "'Courier New', monospace");
        
        Span descriptionSpan=new Span("This method allows you to create an easy link by providing the original URL, and its optional "
                                     +"details (i.e. name, suffix, expiration date, etc.) in the form of a JSON object as shown below.");
        
        Span requestBodyTitle=new Span("Request Body");
        requestBodyTitle.getElement().getStyle().set("text-decoration", "underline");
        String jsonRequestBodySample="""
        {
            "originalUrl": "https://www.example.com/my-example-page",   
            "easySuffix": "abcd",
            "name": "My easy link",
            "description": "This is an example of an easy link used for testing purposes",
            "expirationDate": "2030-01-01",
        }
        """;
        Html requestBodyFields=new Html("<pre>"+jsonRequestBodySample+"</pre>");
        UnorderedList requestFieldsDoc=new UnorderedList();
        requestFieldsDoc.add(new ListItem("originalUrl: the original URL that will be used for constructing the easy link (mandatory field)"));
        requestFieldsDoc.add(new ListItem("easySuffix: a custom suffix for the easy link (optional field)"));
        requestFieldsDoc.add(new ListItem("name: a name for the easy link (optional field)"));
        requestFieldsDoc.add(new ListItem("description: a narrative desribing the easy link (optional field)"));
        requestFieldsDoc.add(new ListItem("expirationDate: the date (or timestamp) of expiration of the easy link (optional field)"));
        Details requestBodyFieldsDocumentationDetails=new Details("JSON Fields description", requestFieldsDoc);
        requestBodyFieldsDocumentationDetails.setOpened(true);
        requestBodyFieldsDocumentationDetails.addThemeVariants(DetailsVariant.SMALL);
        
        Span responseCodesTitle=new Span("Response Codes");
        responseCodesTitle.getElement().getStyle().set("text-decoration", "underline");
        List<ApiResponseCodeDetails> responseCodesList=new ArrayList<>();
        responseCodesList.add(new ApiResponseCodeDetails(200, "The easy link was created its details are provided in JSON format"));
        responseCodesList.add(new ApiResponseCodeDetails(400, "Either the original URL is missing from the request body, or there are errors with request body. "
                                                             +"The message field of the returned JSON provide error details"));
        responseCodesList.add(new ApiResponseCodeDetails(500, "An error occured while constructing and persisting the easy link"));
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
            "expirationDate": "2030-01-01T00:00:00.000+00:00",
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
        
        documentationLayout.add(postBadge,configurationSpan,descriptionSpan,
                                requestBodyTitle,requestBodyFields,requestBodyFieldsDocumentationDetails,
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
            case 500:
                theme="badge error primary";
                break;
            default:
                theme="badge contrast primary";
                break;
        }
        Span badge=new Span(String.valueOf(responseCode));
        badge.getElement().getThemeList().add(theme);
        return badge;
    }
}
