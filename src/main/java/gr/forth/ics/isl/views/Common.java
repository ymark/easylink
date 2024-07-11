package gr.forth.ics.isl.views;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import gr.forth.ics.isl.data.UrlResource;
import org.vaadin.olli.FileDownloadWrapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Common {
    private static final Logger log=Logger.getLogger(Common.class.getName());

    public static void updateResultsPanel(VerticalLayout resultsPanelLayout, UrlResource urlResource){
        resultsPanelLayout.removeAll();
        resultsPanelLayout.setHeight("600px");

        H3 easyUrlComponent=new H3();
        easyUrlComponent.getElement().getStyle().set("white-space", "nowrap");
        TextArea orTextArea=new TextArea("Original URL");
        TextField nmTextField=new TextField("Name");
        TextArea dsTextArea=new TextArea("Description");
        DatePicker crDate=new DatePicker("Created on");
        DatePicker luDate=new DatePicker("Last Used");
        DatePicker exDate=new DatePicker("Expiration Date");
        TextField vsTextField=new TextField("Visits");

        Button copyUrlButton=new Button("Copy",VaadinIcon.COPY.create());
        copyUrlButton.setTooltipText("Copy to clipboard");
        copyUrlButton.addClickListener(e ->
                {
                    UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0);", easyUrlComponent.getText());
                    Notification.show("Value copied to clipboard",4000,Notification.Position.TOP_END);
                }
        );
//        copyUrlButton.setMaxWidth("35px");

        Button downloadQRButton=new Button("Download",VaadinIcon.DOWNLOAD.create());
        downloadQRButton.setTooltipText("Download QR Image");
        FileDownloadWrapper downloadQrWrapper=new FileDownloadWrapper(new StreamResource(urlResource.getQrFilename(), ()-> new ByteArrayInputStream(urlResource.getQrCode())));
        downloadQrWrapper.wrapComponent(downloadQRButton);
//        downloadQRButton.setMaxWidth("35px");

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
        crDate.setLocale(new Locale("el","GR"));
        if(urlResource.getLastUsed()!=null){
            luDate.setValue(urlResource.getLastUsedDateLocal());
        }
        luDate.setReadOnly(true);
        luDate.setLocale(new Locale("el","GR"));
        if(urlResource.getExpirationDate()!=null){
            exDate.setValue(urlResource.getExpirationDateLocal());
        }
        exDate.setReadOnly(true);
        exDate.setLocale(new Locale("el","GR"));
        vsTextField.setValue(String.valueOf(urlResource.getVisited()));
        vsTextField.setReadOnly(true);

        Span constructionStatus=new Span(createIcon(VaadinIcon.EDIT),new Span((urlResource.isCustomUrlSuffix())?"Custom":"Automatic"));
        constructionStatus.getElement().getThemeList().add("badge contrast");

        VerticalLayout qrLayout=retrieveComponentWithQR(urlResource);

        HorizontalLayout easyUrlLayout=new HorizontalLayout();
        VerticalLayout actionButtonsLayout=new VerticalLayout();
        actionButtonsLayout.add(copyUrlButton,downloadQrWrapper);
        easyUrlLayout.add(easyUrlComponent,qrLayout,actionButtonsLayout);
        easyUrlLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        easyUrlLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        FormLayout detailsFormLayout=new FormLayout();
        detailsFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",3));
        detailsFormLayout.setColspan(easyUrlLayout,3);
        detailsFormLayout.setColspan(orTextArea,3);
        detailsFormLayout.setColspan(nmTextField,3);
        detailsFormLayout.setColspan(dsTextArea,3);
        detailsFormLayout.add(easyUrlLayout,orTextArea,nmTextField,dsTextArea,crDate,luDate,vsTextField,exDate,constructionStatus);

        resultsPanelLayout.add(detailsFormLayout);
    }

    private static Icon createIcon(VaadinIcon vaadinIcon){
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs)");
        return icon;
    }

    public static VerticalLayout retrieveComponentWithQR(UrlResource urlResource) {
        VerticalLayout qrVerticalLayout=new VerticalLayout();

        StreamResource imageResource=new StreamResource(urlResource.getQrFilename(), () -> new ByteArrayInputStream(urlResource.getQrCode()));
        Image image=new Image(imageResource,"QR code for "+urlResource.getEasyUrl());
        image.setHeight("100px");
        qrVerticalLayout.add(image);

        return qrVerticalLayout;
    }

    public static BufferedImage createQrImage(String url) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 500, 500,com.google.common.collect.ImmutableMap.of(com.google.zxing.EncodeHintType.MARGIN,0));
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static void triggerNotification(String message, NotificationVariant variant){
        Notification notification =Notification.show(message,5000,Notification.Position.TOP_END);
        notification.addThemeVariants(variant);
    }
}
