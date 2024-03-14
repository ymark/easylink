package gr.forth.ics.isl.views.create;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import gr.forth.ics.isl.views.MainLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@PageTitle("Create")
@Route(value = "create", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class CreateView extends VerticalLayout {
    private TextArea originalUrlTextArea=new TextArea("Original URL");
    private TextField nameTextField=new TextField("Name");
    private TextArea descriptionTextArea=new TextArea("Description");
    private Button createButton=new Button("Create",new Icon(VaadinIcon.EDIT));
    private Button resetButton=new Button("Reset",new Icon(VaadinIcon.CLOSE));
    private VerticalLayout resultsPanelLayout=new VerticalLayout();
    private static final Logger log= LogManager.getLogger(CreateView.class);
    @Autowired
    private UrlResourceService urlResourceService;

    public CreateView() {
        setSpacing(false);
        add(createForm());
        add(new Hr());
        add(resultsPanelLayout);
    }

    private Component createForm(){
        FormLayout formLayout=new FormLayout();
        formLayout.add(originalUrlTextArea);
        formLayout.add(nameTextField);
        formLayout.add(descriptionTextArea);
        formLayout.add(createButton,resetButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        formLayout.setColspan(originalUrlTextArea,2);
        formLayout.setColspan(nameTextField,2);
        formLayout.setColspan(descriptionTextArea,2);

        updateFieldsVisibility();

        this.resetButton.addClickListener(e -> {
            this.originalUrlTextArea.clear();
            this.nameTextField.clear();
            this.descriptionTextArea.clear();
        });
        this.createButton.addClickListener(e-> checkAndCreate());

        return formLayout;
    }

    private void updateFieldsVisibility(){
        originalUrlTextArea.setRequired(true);
        originalUrlTextArea.setTooltipText("This field is used for adding the original URL that will be shortened. This field is mandatory");
//        originalUrlTextArea.setPattern("^https?://(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,}(?:/[^\\\\s]*)?$");

        nameTextField.setRequired(false);
        nameTextField.setTooltipText("A short name for the URL to be shortened. This field is optional");

        descriptionTextArea.setRequired(false);
        descriptionTextArea.setTooltipText("A description (longer than the short name) for the URL to be shortened. This field is optional");
    }

    private void checkAndCreate(){
        System.out.println("check and create");
        if(this.originalUrlTextArea.isEmpty()){
            notifyForEmptyFields();
        }
        Optional<UrlResource> optionalRetrievedUrlResource=urlResourceService.findByUrl(this.originalUrlTextArea.getValue());
        if(optionalRetrievedUrlResource.isPresent()){
            log.debug("The given URL already exists '{}'",this.originalUrlTextArea.getValue());
            notifyForExistingUrl();
            this.updateResultsPanel(optionalRetrievedUrlResource.get());
        }else{
            UrlResource newUrlResource=new UrlResource();
            newUrlResource.setOriginalUrl(this.originalUrlTextArea.getValue());
            newUrlResource.setName((this.nameTextField.isEmpty())?"-":this.nameTextField.getValue());
            newUrlResource.setDescription((this.descriptionTextArea.isEmpty())?"-":this.descriptionTextArea.getValue());
            newUrlResource.setCreated(Calendar.getInstance().getTime());
            newUrlResource.setVisited(0);
            newUrlResource.shortenUrl();

            log.debug("Create a new UrlResource {}",newUrlResource.toString());
            UrlResource createdResource=urlResourceService.update(newUrlResource);
            //to show a success notification and the resource in results panel
        }
    }

    private void updateResultsPanel(UrlResource urlResource){
        this.resultsPanelLayout.removeAll();
        this.resultsPanelLayout.setHeight("600px");
                
        TextField shTextField=new TextField("Short URL");
        TextArea orTextArea=new TextArea("Original URL");
        TextField nmTextField=new TextField("Name");
        TextArea dsTextArea=new TextArea("Description");
        DatePicker crDate=new DatePicker("Created on");
        DatePicker luDate=new DatePicker("Last Used");
        TextField vsTextField=new TextField("Visits");

        shTextField.setValue(urlResource.getShortUrl());
        shTextField.setReadOnly(true);
        orTextArea.setValue(urlResource.getOriginalUrl());
        orTextArea.setReadOnly(true);
        nmTextField.setValue(urlResource.getName());
        nmTextField.setReadOnly(true);
        dsTextArea.setValue(urlResource.getDescription());
        dsTextArea.setReadOnly(true);
        crDate.setValue(urlResource.getCreatedDateLocal());
        crDate.setReadOnly(true);
        if(urlResource.getLastUsed()!=null){
            luDate.setValue(urlResource.getLastUsedDateLocal());
        }
        luDate.setReadOnly(true);
        vsTextField.setValue(String.valueOf(urlResource.getVisited()));
        vsTextField.setReadOnly(true);

        FormLayout detailsFormLayout=new FormLayout();
        detailsFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",3));
        detailsFormLayout.setColspan(shTextField,3);
        detailsFormLayout.setColspan(orTextArea,3);
        detailsFormLayout.setColspan(nmTextField,3);
        detailsFormLayout.setColspan(dsTextArea,3);
        detailsFormLayout.add(shTextField,orTextArea,nmTextField,dsTextArea,crDate,luDate,vsTextField);


        this.resultsPanelLayout.add(detailsFormLayout);
    }

    private void notifyForEmptyFields(){
        Notification errorNotificiation=new Notification();
        errorNotificiation.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div text=new Div(new Text("The field Original URL is empty"));
        Button closeButton=new Button(new Icon("lumo","cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> {
            errorNotificiation.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        errorNotificiation.add(layout);
        errorNotificiation.open();
    }

    private void notifyForExistingUrl(){
        Notification warningNotificiation=new Notification();
        warningNotificiation.addThemeVariants(NotificationVariant.LUMO_WARNING);
        Div text=new Div(new Text("The given URL alreday exists"));
        Button closeButton=new Button(new Icon("lumo","cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> {
            warningNotificiation.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        warningNotificiation.add(layout);
        warningNotificiation.open();
    }
}
