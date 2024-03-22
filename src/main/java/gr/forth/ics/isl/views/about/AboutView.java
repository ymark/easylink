package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.views.MainLayout;

@PageTitle("EasyLink")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        Image img = new Image("images/easylink.jpg", "EasyLink");
        img.setWidth("250px");
        add(img);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "justify");
    }

}
