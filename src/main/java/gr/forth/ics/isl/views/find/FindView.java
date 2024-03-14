package gr.forth.ics.isl.views.find;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import gr.forth.ics.isl.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Optional;

@PageTitle("Find")
@Route(value = "find", layout = MainLayout.class)
public class FindView extends VerticalLayout {
    private TextArea urlTextArea=new TextArea("URL");
    private Button createButton=new Button("Search",new Icon(VaadinIcon.SEARCH));
    private Button resetButton=new Button("Reset",new Icon(VaadinIcon.CLOSE));
    private VerticalLayout resultsPanelLayout=new VerticalLayout();
    @Autowired
    private UrlResourceService urlResourceService;

    public FindView() {
        setSpacing(false);
        add(createForm());
        add(new Hr());
        add(resultsPanelLayout);
    }

    private Component createForm(){
        FormLayout formLayout=new FormLayout();
        formLayout.add(urlTextArea);
        formLayout.add(createButton,resetButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        formLayout.setColspan(urlTextArea,2);
        updateFieldsVisibility();

        this.resetButton.addClickListener(e -> {
            this.urlTextArea.clear();
            this.resultsPanelLayout.removeAll();
        });
        this.createButton.addClickListener(e-> searchUrl(this.urlTextArea.getValue()));

        return formLayout;
    }

    private void updateFieldsVisibility(){
        urlTextArea.setRequired(true);
        urlTextArea.setTooltipText("Search if a URL (original or short) already exists");
    }

    private void searchUrl(String url){
        Optional<UrlResource> tryOriginalUrlResource=urlResourceService.findByUrl(url,false);
        if(tryOriginalUrlResource.isPresent()){
            this.updateResultsPanel(tryOriginalUrlResource.get());
        }else{
            Optional<UrlResource> tryShortUrlResource=urlResourceService.findByUrl(url,true);
            if(tryShortUrlResource.isPresent()){
                this.updateResultsPanel(tryShortUrlResource.get());
            }else{
                this.updateResultsPanelNoResults();
            }
        }
    }

    private void updateResultsPanel(UrlResource urlResource){
        this.resultsPanelLayout.removeAll();
        this.resultsPanelLayout.setHeight("600px");

        H2 shUrlComponent=new H2();
        TextArea orTextArea=new TextArea("Original URL");
        TextField nmTextField=new TextField("Name");
        TextArea dsTextArea=new TextArea("Description");
        DatePicker crDate=new DatePicker("Created on");
        DatePicker luDate=new DatePicker("Last Used");
        TextField vsTextField=new TextField("Visits");

        Button copyUrlButton=new Button(VaadinIcon.COPY.create());
        copyUrlButton.addClickListener(e ->
                {
                    UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0);", shUrlComponent.getText());
                    Notification.show("Value copied to clipboard",4000,Notification.Position.TOP_END);
                }
        );
        copyUrlButton.setMaxWidth("25px");

        shUrlComponent.setText(urlResource.getShortUrl());
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

        HorizontalLayout shUrlLayout=new HorizontalLayout();
        shUrlLayout.add(shUrlComponent,copyUrlButton);
        shUrlLayout.setAlignItems(Alignment.END);

        FormLayout detailsFormLayout=new FormLayout();
        detailsFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",3));
        detailsFormLayout.setColspan(shUrlLayout,3);
        detailsFormLayout.setColspan(orTextArea,3);
        detailsFormLayout.setColspan(nmTextField,3);
        detailsFormLayout.setColspan(dsTextArea,3);
        detailsFormLayout.add(shUrlLayout,orTextArea,nmTextField,dsTextArea,crDate,luDate,vsTextField);

        this.resultsPanelLayout.add(detailsFormLayout);
    }

    private void updateResultsPanelNoResults() {
        this.resultsPanelLayout.removeAll();
        this.resultsPanelLayout.setHeight("600px");

        H2 notFoundTitle = new H2();
        notFoundTitle.setText("The URL does not exist");
        Div textDiv=new Div(new Text("You can create a new one"));

        this.resultsPanelLayout.add(notFoundTitle,textDiv);
    }
}
