package gr.forth.ics.isl.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.views.about.AboutView;
import gr.forth.ics.isl.views.about.ApiView;
import gr.forth.ics.isl.views.create.CreateView;
import gr.forth.ics.isl.views.find.FindView;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("EasyLink");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Create", CreateView.class, LineAwesomeIcon.LINK_SOLID.create()));
        nav.addItem(new SideNavItem("Find", FindView.class, LineAwesomeIcon.SEARCH_SOLID.create()));
        nav.addItem(new SideNavItem("API", ApiView.class, LineAwesomeIcon.COGS_SOLID.create()));
        nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.INFO_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        Div bookmarkDiv=new Div();
        Anchor bookmarkAnchor=new Anchor();
        bookmarkAnchor.setHref("javascript:(function() {var currentUrl = window.location.href; window.open(\""+ EntityManager.APP_URL +"create?url=\"+currentUrl, \"_blank\");})();");
        bookmarkAnchor.setTarget(AnchorTarget.BLANK);
        bookmarkAnchor.setText("EasyLink");
        bookmarkAnchor.setTitle("Drag this link to the bookmarks bar of your browser");
        bookmarkDiv.add(bookmarkAnchor);
        layout.add(LineAwesomeIcon.BOOKMARK_SOLID.create());
        layout.add(bookmarkDiv);
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
