package gr.forth.ics.isl.views.find;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import gr.forth.ics.isl.services.UrlResourceService;
import gr.forth.ics.isl.views.Common;
import gr.forth.ics.isl.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@PageTitle("EasyLink")
@Route(value = "find", layout = MainLayout.class)
public class FindView extends VerticalLayout {
    private TextArea urlTextArea=new TextArea("URL");
    private Button searchButton=new Button("Search",new Icon(VaadinIcon.SEARCH));
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
        formLayout.add(searchButton,resetButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        formLayout.setColspan(urlTextArea,2);
        updateFieldsVisibility();

        this.resetButton.addClickListener(e -> {
            this.urlTextArea.clear();
            this.resultsPanelLayout.removeAll();
        });
        this.searchButton.addClickListener(e-> searchUrl(this.urlTextArea.getValue()));

        return formLayout;
    }

    private void updateFieldsVisibility(){
        urlTextArea.setRequired(true);
        urlTextArea.setMaxHeight("100px");
        urlTextArea.setTooltipText("Search if a URL (original or easy) already exists");
        urlTextArea.setMaxLength(EntityManager.ORIGINAL_URL_MAX_LENGTH);
        urlTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        urlTextArea.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + EntityManager.ORIGINAL_URL_MAX_LENGTH);
        });
    }

    private void searchUrl(String url){
        Optional<UrlResource> tryOriginalUrlResource=urlResourceService.findByUrl(url,false);
        if(tryOriginalUrlResource.isPresent()){
            Common.updateResultsPanel(this.resultsPanelLayout,tryOriginalUrlResource.get());
        }else{
            Optional<UrlResource> tryShortUrlResource=urlResourceService.findByUrl(url,true);
            if(tryShortUrlResource.isPresent()){
                Common.updateResultsPanel(this.resultsPanelLayout,tryShortUrlResource.get());
            }else{
                this.updateResultsPanelNoResults();
            }
        }
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
