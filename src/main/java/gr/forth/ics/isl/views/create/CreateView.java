package gr.forth.ics.isl.views.create;

import com.google.zxing.WriterException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import gr.forth.ics.isl.views.Common;
import gr.forth.ics.isl.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@PageTitle("EasyLink")
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
    private Accordion extraFieldsAccordion=new Accordion();
    private TextField customUrlSuffixField=new TextField();
    private DatePicker expirationDateField=new DatePicker("Expiration Date");
    private static final Logger log=Logger.getLogger(String.valueOf(CreateView.class));
    @Autowired
    private UrlResourceService urlResourceService;

    public CreateView() {

        setSpacing(false);
        add(createBetaTestingComponent());
        add(createForm());
        add(new Hr());
        add(resultsPanelLayout);
    }
    
    private Component createBetaTestingComponent(){
        HorizontalLayout logoLayout=new HorizontalLayout();
        Image img = new Image("images/beta-testing.png", "Beta version for testing");
        img.setWidth("70px");;
        H3 h3=new H3("EasyLink is under development. DO NOT USE for production purposes");
        h3.addClassName("custom-text-color");
        logoLayout.add(img,h3);
        logoLayout.setSizeFull();
        logoLayout.setWidthFull();
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return logoLayout;
    }

    private Component createForm(){
        FormLayout mainFormLayout=new FormLayout();
        mainFormLayout.add(originalUrlTextArea);
        mainFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        mainFormLayout.setColspan(originalUrlTextArea,2);

        FormLayout subFormLayout=new FormLayout();
        subFormLayout.add(nameTextField);
        subFormLayout.add(descriptionTextArea);
        subFormLayout.add(new Div(new H3(EntityManager.EASY_URL_PREFIX)));
        subFormLayout.add(customUrlSuffixField);
        subFormLayout.add(expirationDateField);
        subFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        subFormLayout.setColspan(originalUrlTextArea,2);
        subFormLayout.setColspan(nameTextField,2);
        subFormLayout.setColspan(descriptionTextArea,2);

        FormLayout buttonsFormLayout=new FormLayout();
        buttonsFormLayout.add(createButton,resetButton);

        updateFieldsVisibility();

        extraFieldsAccordion.add("More options",subFormLayout);
        extraFieldsAccordion.setWidthFull();

        this.resetButton.addClickListener(e -> {
            this.originalUrlTextArea.clear();
            this.nameTextField.clear();
            this.descriptionTextArea.clear();
            this.resultsPanelLayout.removeAll();
        });
        this.createButton.addClickListener(e-> checkAndCreate());

        return new VerticalLayout(mainFormLayout,extraFieldsAccordion,buttonsFormLayout);
    }

    private void updateFieldsVisibility(){
        originalUrlTextArea.setRequired(true);
        originalUrlTextArea.setMaxHeight("100px");
        originalUrlTextArea.setTooltipText("[MANDATORY] This field is used for adding the original URL that will be used for constructing the easy URL.");
        originalUrlTextArea.setMaxLength(EntityManager.ORIGINAL_URL_MAX_LENGTH);
        originalUrlTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        originalUrlTextArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + EntityManager.ORIGINAL_URL_MAX_LENGTH);
        });
//        originalUrlTextArea.setPattern("^https?://(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,}(?:/[^\\\\s]*)?$");

        nameTextField.setRequired(false);
        nameTextField.setTooltipText("[OPTIONAL] A short name for the URL");
        nameTextField.setMaxLength(EntityManager.NAME_MAX_LENGTH);
        nameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        nameTextField.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + EntityManager.NAME_MAX_LENGTH);
        });

        descriptionTextArea.setRequired(false);
        descriptionTextArea.setMaxHeight("100px");
        descriptionTextArea.setTooltipText("[OPTIONAL] A description for the URL");
        descriptionTextArea.setMaxLength(EntityManager.DESCRIPTION_MAX_LENGTH);
        descriptionTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionTextArea.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + EntityManager.DESCRIPTION_MAX_LENGTH);
        });

        extraFieldsAccordion.close();

        customUrlSuffixField.setMinLength(EntityManager.CUSTOM_URL_MIN_LENGTH);
        customUrlSuffixField.setMaxLength(EntityManager.CUSTOM_URL_MAX_LENGTH);
        customUrlSuffixField.setTooltipText("[OPTIONAL] Add your own easy link suffix");
        customUrlSuffixField.setValueChangeMode(ValueChangeMode.EAGER);
        customUrlSuffixField.addValueChangeListener(e-> {
            e.getSource().setHelperText(e.getValue().length()+"/"+EntityManager.CUSTOM_URL_MAX_LENGTH);
        });

        expirationDateField.setMin(LocalDate.now(ZoneId.systemDefault()));
        expirationDateField.setLocale(new Locale("el","GR"));
        expirationDateField.setTooltipText("[OPTIONAL] Set an expiration date for your easy link");

        checkForProvidedOriginalUrl();
    }

    private void checkForProvidedOriginalUrl(){
        UI.getCurrent().getPage().fetchCurrentURL(currentUrl -> {
            String queryPart=currentUrl.getQuery();
            if(queryPart!=null && !queryPart.isBlank() && queryPart.startsWith("url=")){
                this.originalUrlTextArea.setValue(queryPart.replace("url=",""));
            }
        });
    }

    private void checkAndCreate(){
        if(this.originalUrlTextArea.isEmpty()){
            Common.triggerNotification("The field Original URL is empty",NotificationVariant.LUMO_ERROR);
        }else {
            Optional<UrlResource> optionalRetrievedUrlResource = urlResourceService.findByUrl(this.originalUrlTextArea.getValue(), false);
            if (optionalRetrievedUrlResource.isPresent()) {
                Common.triggerNotification("The given URL already exists", NotificationVariant.LUMO_WARNING);
                Common.updateResultsPanel(this.resultsPanelLayout, optionalRetrievedUrlResource.get());
            } else {
                if (!this.customUrlSuffixField.getValue().isBlank()) {
                    optionalRetrievedUrlResource = urlResourceService.findByUrl(EntityManager.EASY_URL_PREFIX+this.customUrlSuffixField.getValue(), true);
                    if(optionalRetrievedUrlResource.isPresent()){
                        Common.triggerNotification("The given easy URL suffix already exists",NotificationVariant.LUMO_WARNING);
                    }else{
                        createEasyLink(this.customUrlSuffixField.getValue());
                    }
                } else {
                    createEasyLink(null);
                }
            }
        }
    }

    private void createEasyLink(String customUrlSuffix){
        try {
            UrlResource newUrlResource = new UrlResource(this.originalUrlTextArea.getValue(), customUrlSuffix);
            while(urlResourceService.findByUrl(newUrlResource.getEasyUrl(), true).isPresent()){
                log.info("The easy URL has already been assigned. Generating a new one");
                newUrlResource = new UrlResource(this.originalUrlTextArea.getValue(), customUrlSuffix);
            }
            newUrlResource.setName((this.nameTextField.isEmpty()) ? "-" : this.nameTextField.getValue());
            newUrlResource.setDescription((this.descriptionTextArea.isEmpty()) ? "-" : this.descriptionTextArea.getValue());
            newUrlResource.setCreated(Calendar.getInstance().getTime());
            newUrlResource.setVisited(0);
            if(!this.expirationDateField.isEmpty()){
                newUrlResource.setExpirationDate(Date.from(this.expirationDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            UrlResource createdResource = urlResourceService.update(newUrlResource);
            Common.updateResultsPanel(this.resultsPanelLayout, createdResource);
            Common.triggerNotification("Successfully created easy URL", NotificationVariant.LUMO_SUCCESS);
            log.log(Level.INFO, "Successfully created easy URL '{}'", newUrlResource.getEasyUrl());
        } catch (IOException | WriterException ex) {
            log.log(Level.SEVERE, "An error occured while creating QR code of an easy URL", ex);
        }
    }
}
