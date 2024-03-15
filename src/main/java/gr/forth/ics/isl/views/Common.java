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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import gr.forth.ics.isl.data.EntityManager;
import gr.forth.ics.isl.data.UrlResource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class Common {
    private static final Logger log=Logger.getLogger(Common.class.getName());

    public static void updateResultsPanel(VerticalLayout resultsPanelLayout, UrlResource urlResource){
        resultsPanelLayout.removeAll();
        resultsPanelLayout.setHeight("600px");

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
        easyUrlLayout.setAlignItems(FlexComponent.Alignment.END);

        createQrCode(urlResource.getEasyUrl());

        FormLayout detailsFormLayout=new FormLayout();
        detailsFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",3));
        detailsFormLayout.setColspan(easyUrlLayout,3);
        detailsFormLayout.setColspan(orTextArea,3);
        detailsFormLayout.setColspan(nmTextField,3);
        detailsFormLayout.setColspan(dsTextArea,3);
        detailsFormLayout.add(easyUrlLayout,orTextArea,nmTextField,dsTextArea,crDate,luDate,vsTextField);

        resultsPanelLayout.add(detailsFormLayout);
    }

    public static VerticalLayout createQrCode(String url) {
        VerticalLayout qrVerticalLayout=new VerticalLayout();
        try {
            BufferedImage qrImage = createQrImage(url);
            File qrFile = storeQrLocally(qrImage, url.replace(EntityManager.EASY_URL_PREFIX, "") + ".png");
        }catch(WriterException ex){
            log.log(Level.WARNING,"An error occurred while constructing the QR image");
        }catch(IOException ex){
            log.log(Level.WARNING,"An error occurred while storing locally the QR image");
        }
        return qrVerticalLayout;
    }

    private static BufferedImage createQrImage(String url) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static File storeQrLocally(BufferedImage qrImage, String filename) throws IOException {
        Files.createDirectories(Paths.get(EntityManager.QR_FOLDER));    // just in case
        File qrFile=new File(EntityManager.QR_FOLDER+"/"+filename);
        ImageIO.write(qrImage,"PNG",qrFile);
        return qrFile;
    }
}
