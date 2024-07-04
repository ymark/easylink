package gr.forth.ics.isl.views.about;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gr.forth.ics.isl.views.MainLayout;

@PageTitle("EasyLink")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        addLogo();
        addDescription();
    }
    
    private void addLogo(){
        Image img = new Image("images/easylink.jpg", "EasyLink");
        img.setWidth("250px");
        add(img);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "justify");
    }
    
    private void addDescription(){
        H1 aboutHeader=new H1("About EasyLink");
        Div contentsDiv=new Div("Welcome to EasyLink, your go-to solution for creating shorter, more manageable URLs! "
                + "Whether you're sharing links for business, education, or personal use, EasyLink makes the process seamless and efficient. "
                + "Our platform offers a suite of features designed to simplify and enhance your link-sharing experience.");
        H2 keyFearuresHeader=new H2("Key Features:");
        ListItem keyFeature1=new ListItem("Easily convert long and unwieldy URLs into concise, easy-to-share links. Perfect for social media, emails, and other communication channels.");
        ListItem keyFeature2=new ListItem("For every link you create, EasyLink automatically generates a unique QR code. This makes it easy to share links in print materials, presentations, or anywhere a QR code can be scanned");
        ListItem keyFeature3=new ListItem("Gain valuable insights into your link's performance. Track metrics such as the number of clicks, geographic location of visitors, referral sources, and more. Use this data to understand your audience and optimize your link-sharing strategy.");
        ListItem keyFeature4=new ListItem("Custom URLs.");
        ListItem keyFeature5=new ListItem("Bookmarklet");
        
        H2 whyChooseHeader=new H2("Why Choose EasyLink:");
        ListItem whyChooseItem1=new ListItem("Our intuitive design ensures that anyone can start shortening URLs and generating QR codes in seconds, with no technical expertise required.");
        ListItem whyChooseItem2=new ListItem("EasyLink is built with robust technology to ensure your links are always accessible and secure. We prioritize your privacy and the safety of your data.");
        ListItem whyChooseItem3=new ListItem("Tailor your shortened URLs to reflect your brand or campaign. Create custom aliases to make your links even more memorable and professional.");
        
        H2 getStartedHeader=new H2("Get Started");
        Div getStartedDiv=new Div("Join the EasyLink community today and take the hassle out of link sharing. Whether you're a marketer, educator, small business owner, or just someone who loves sharing content, EasyLink is here to make your digital life easier.\n"
                +"\n" + "Thank you for choosing EasyLink!");
        
        H2 questionsAndFeedbackHeader=new H2("Questions and Feedback");
        Div questionsAndFeedbackDiv=new Div("Feel free to ask questions and provide your feedback at X.");
        
        add(aboutHeader,contentsDiv,
                keyFearuresHeader,keyFeature1,keyFeature2,keyFeature3,keyFeature4,keyFeature5,
                whyChooseHeader,whyChooseItem1,whyChooseItem2,whyChooseItem3,
                getStartedHeader,getStartedDiv,
                questionsAndFeedbackHeader,questionsAndFeedbackDiv);
    }
}
