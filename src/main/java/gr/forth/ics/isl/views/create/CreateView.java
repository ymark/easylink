package gr.forth.ics.isl.views.create;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
import gr.forth.ics.isl.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.html.parser.Entity;
import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger log=Logger.getLogger(String.valueOf(CreateView.class));
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
            this.resultsPanelLayout.removeAll();
        });
        this.createButton.addClickListener(e-> checkAndCreate());

        return formLayout;
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
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + EntityManager.NAME_MAX_LENGTH);
        });

        descriptionTextArea.setRequired(false);
        descriptionTextArea.setMaxHeight("100px");
        descriptionTextArea.setTooltipText("[OPTIONAL] A description for the URL");
        descriptionTextArea.setMaxLength(EntityManager.DESCRIPTION_MAX_LENGTH);
        descriptionTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionTextArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + EntityManager.DESCRIPTION_MAX_LENGTH);
        });

    }

    private void checkAndCreate(){
        System.out.println("check and create");
        if(this.originalUrlTextArea.isEmpty()){
            notifyMessage("The field Original URL is empty",NotificationVariant.LUMO_ERROR);
        }
        Optional<UrlResource> optionalRetrievedUrlResource=urlResourceService.findByUrl(this.originalUrlTextArea.getValue(),false);
        if(optionalRetrievedUrlResource.isPresent()){
            notifyMessage("The given URL already exists",NotificationVariant.LUMO_WARNING);
            this.updateResultsPanel(optionalRetrievedUrlResource.get());
        }else{
            UrlResource newUrlResource=new UrlResource(this.originalUrlTextArea.getValue());
            newUrlResource.setName((this.nameTextField.isEmpty())?"-":this.nameTextField.getValue());
            newUrlResource.setDescription((this.descriptionTextArea.isEmpty())?"-":this.descriptionTextArea.getValue());
            newUrlResource.setCreated(Calendar.getInstance().getTime());
            newUrlResource.setVisited(0);
            UrlResource createdResource=urlResourceService.update(newUrlResource);
            this.updateResultsPanel(createdResource);
            this.notifyMessage("Successfully easy URL",NotificationVariant.LUMO_SUCCESS);
            log.log(Level.INFO,"Successfully created easy URL '{}'",newUrlResource.getEasyUrl());
        }
    }

    private void updateResultsPanel(UrlResource urlResource){
        this.resultsPanelLayout.removeAll();
        this.resultsPanelLayout.setHeight("600px");

        H2 easyUrlComponent=new H2();
        TextArea orTextArea=new TextArea("Original URL");
        TextField nmTextField=new TextField("Name");
        TextArea dsTextArea=new TextArea("Description");
        DatePicker crDate=new DatePicker("Created on");
        DatePicker luDate=new DatePicker("Last Used");
        TextField vsTextField=new TextField("Visits");

        Button copyUrlButton=new Button(VaadinIcon.COPY.create());
        copyUrlButton.addClickListener(e ->
                {
                    UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0);", easyUrlComponent.getText());
                    Notification.show("Value copied to clipboard",4000,Notification.Position.TOP_END);
                }
        );
        copyUrlButton.setMaxWidth("25px");

        easyUrlComponent.setText(urlResource.getEasyUrl());
        orTextArea.setValue(urlResource.getOriginalUrl());
        orTextArea.setReadOnly(true);
        orTextArea.setMaxHeight("100px");
        nmTextField.setValue(urlResource.getName());
        nmTextField.setReadOnly(true);
        dsTextArea.setValue(urlResource.getDescription());
        dsTextArea.setReadOnly(true);
        dsTextArea.setMaxHeight("100px");
        crDate.setValue(urlResource.getCreatedDateLocal());
        crDate.setReadOnly(true);
        if(urlResource.getLastUsed()!=null){
            luDate.setValue(urlResource.getLastUsedDateLocal());
        }
        luDate.setReadOnly(true);
        vsTextField.setValue(String.valueOf(urlResource.getVisited()));
        vsTextField.setReadOnly(true);

        HorizontalLayout easyUrlLayout=new HorizontalLayout();
        easyUrlLayout.add(easyUrlComponent,copyUrlButton);
        easyUrlLayout.setAlignItems(Alignment.END);

        FormLayout detailsFormLayout=new FormLayout();
        detailsFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",3));
        detailsFormLayout.setColspan(easyUrlLayout,3);
        detailsFormLayout.setColspan(orTextArea,3);
        detailsFormLayout.setColspan(nmTextField,3);
        detailsFormLayout.setColspan(dsTextArea,3);
        detailsFormLayout.add(easyUrlLayout,orTextArea,nmTextField,dsTextArea,crDate,luDate,vsTextField);

        this.resultsPanelLayout.add(detailsFormLayout);
    }

    private void notifyMessage(String message, NotificationVariant nVariant){
        Notification notification=new Notification();
        notification.addThemeVariants(nVariant);
        Div text=new Div(new Text(message));
        Button closeButton=new Button(new Icon("lumo","cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> {
            notification.close();
        });
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        notification.add(layout);
        notification.open();
    }
}
