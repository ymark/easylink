package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.HtmlObject;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import gr.forth.ics.isl.views.MainLayout;

@PageTitle("EasyLink")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        addLogo();
        addDescription();
    }
    
    private void addLogo(){
        HorizontalLayout logoLayout=new HorizontalLayout();
        Image img = new Image("images/easylink-150.png", "EasyLink");
        setDefaultHorizontalComponentAlignment(Alignment.AUTO);
        getStyle().set("text-align", "justify");
        logoLayout.add(img,new H1("About EasyLink"));
        logoLayout.setSizeFull();
        logoLayout.setWidthFull();
        logoLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        logoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        
        add(logoLayout);
    }
    
    private void addDescription(){
        Div contentsDiv=new Div("Welcome to EasyLink, your go-to solution for creating shorter, easier, more manageable URLs! "
                +"Whether you're sharing links for business, education, or personal use, EasyLink makes the process seamless and efficient. "
                +"Our platform offers a suite of features designed to simplify and enhance your link-sharing experience "
                +"and provide with various analytics for monitoring your easy links"
                +"Join EasyLink today and take the hassle out of link sharing. "
                +"Whether you're a marketer, educator, small business owner, or just someone who loves sharing content, " 
                +"EasyLink is here to make your digital life easier."
                +"Thank you for choosing EasyLink!");
        
        H2 keyFearuresHeader=new H2("Key Features");
        
        FormLayout featuresFormLayout=new FormLayout();
        
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/shorten-60px.png", "ShortenUrl"), 
                "Shorten URL",
                "<p>EasyLink supports the conversion of long and complex URLs into short, easy-to-share links. "+
                        "It does not matter if your URL is really long or if it contains specially encoded characters, "+
                        "EasyLink will instantly generate a compact link that you can share across social media, messages or emails. "+
                        "It is perfect for streamlining bulky links, improving accessibility, and creating a more user-friendly sharing experience.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/qr-60px.png", "Generate QR"), 
                "Generate QR",
                "<p>For every link you create, EasyLink automatically generates a unique QR code. "+
                        "You can even download your EasyLink QR code and use it as you wish. "+
                        "This makes it easy to share links in print materials, presentations, or anywhere a QR code can be scanned</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/analytics-60px.png", "Analytics"), 
                "Analytics",
                "<p>Gain valuable insights into your link's performance."
                        + " Track metrics such as the number of clicks and the last time your easylink was visited. "
                        + " Use this data to understand your audience and optimize your link-sharing strategy.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/manual-60px.png", "Manual URL suffix"), 
                "Manual URL suffix",
                "<p>If you are not very keen on generating a random EasyLink and prefer something more intelligible, "+
                        "you can assign your own suffix for your EasyLink. "+
                        "Using custom links you can create an easy to remember EasyLink.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/expiration-60px.png", "Expired Easy Links"),
                "Expired Easy Links",
                "<p>Are you concerned about your privacy? Do you want to share a temporary URL with your mates? Do you simply want to control access to your own resources? "+
                        "You can create and share EasyLinks with an expiration date. After the expiration date, these easylink will not resolve. "+
                        "By default easy links will live forever, unless you define an expiration date for them.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/search-60px.png", "Search Easy Links"), 
                "Search Easy Links",
                "<p>Did you forgot your easy link? No worries. "
                        + "Simply search (or try to create an easy link) for your long URL and you'll retrieve your original easy link.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/bookmarklet-60px.png", "One-Click creation"), 
                "One-Click creation",
                "<p>Add the easylink bookmarklet in your favorite browser, and your EasyLinks are just one click away."+
                        "Just drag'n'drop the EasyLink bookmarklet (found on the bottom left of the screen) in your bookmarks bar."+
                        "You can use that bookmarklet to create an EasyLink for any website URL you access with your browser.</p>"));
        featuresFormLayout.add(this.createInfoComponent(new Image("avatars/api-60px.png", "API"), 
                "API",
                "If you want to incorporate EasyLinks in your services or application you can rely on EasyLink API. "
                        + "<p>Simply <b>create</b> and <b>search</b> EasyLinks using a programmatic API.</p>"));
        
        
        featuresFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2),new FormLayout.ResponsiveStep("500px", 3), new FormLayout.ResponsiveStep("1000px", 4));

        
//        H2 whyChooseHeader=new H2("Why Choose EasyLink:");
//        ListItem whyChooseItem1=new ListItem("Our intuitive design ensures that anyone can start shortening URLs and generating QR codes in seconds, with no technical expertise required.");
//        ListItem whyChooseItem2=new ListItem("EasyLink is built with robust technology to ensure your links are always accessible and secure. We prioritize your privacy and the safety of your data.");
//        ListItem whyChooseItem3=new ListItem("Tailor your shortened URLs to reflect your brand or campaign. Create custom aliases to make your links even more memorable and professional.");

        
        H2 questionsAndFeedbackHeader=new H2("Questions and Feedback");
        Div questionsAndFeedbackDiv=new Div(new Text("Feel free to ask questions, or provide your feedback "),new Anchor("https://github.com/ymark/easylink/issues", "here",AnchorTarget.BLANK));
        
                add(
                contentsDiv,
                keyFearuresHeader,
                featuresFormLayout,
//                whyChooseHeader,whyChooseItem1,whyChooseItem2,whyChooseItem3,
//                getStartedHeader,getStartedDiv,
                questionsAndFeedbackHeader,questionsAndFeedbackDiv);
    }
    
    private Component createInfoComponent(Image image, String title, String description){
        VerticalLayout infoComponentLayout=new VerticalLayout();
        Button button=new Button("...");
        button.addThemeVariants(ButtonVariant.LUMO_SMALL);
        ConfirmDialog dialog=new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(new Html(description));
        dialog.setConfirmText("OK");
        
        button.addClickListener(event -> dialog.open());
        
        infoComponentLayout.add(image,
                new H3(title),
                button);
        infoComponentLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return infoComponentLayout;
    }
}
