package application.dak.DAK.iText;

import application.dak.DAK.backend.common.models.Package;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PDFGenerator {

    public PDFGenerator() {
        fontHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        boldBodyFont = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
        bodyFont = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
    }

    private final Font fontHeader;
    private final Font boldBodyFont;
    private final Font bodyFont;
    private PdfWriter writer;

    public boolean createPackageReport(String filename, Package pack){
        try{
            Document document = new Document(PageSize.A6.rotate(), 20, 20, 30, 30);
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            setHeader(document, pack.getTrackingId());
            setSubHeader(document, pack.getStartDate());
            setBodyContent(document, pack);
            setLogo(document);
            setBarcode(document, pack.getTrackingId());
            document.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setBarcode(Document document, String code) throws DocumentException {
        PdfContentByte contentByte = writer.getDirectContent();
        Barcode128 barcode = new Barcode128();
        barcode.setCode(code);
        barcode.setCodeType(Barcode.CODE128);
        Image codeImg = barcode.createImageWithBarcode(contentByte, null, null);
        codeImg.scalePercent(100);
        codeImg.setSpacingBefore(150f);
        codeImg.setAlignment(Element.ALIGN_CENTER);
        document.add(codeImg);
    }

    private void setHeader(Document document, String trackingId) throws DocumentException {
        Paragraph header = new Paragraph("Package: " + trackingId, fontHeader);
        header.setSpacingBefore(500f);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
    }

    private void setSubHeader(Document document, Date startDate) throws DocumentException {
        Paragraph subHeader = new Paragraph("Expedition: " + startDate.toString(), boldBodyFont);
        subHeader.setAlignment(Element.ALIGN_CENTER);
        subHeader.setSpacingAfter(72f);
        document.add(subHeader);
    }

    private void setBodyContent(Document document, Package pack) throws DocumentException {
        document.add(new Paragraph("Price: $" + pack.getPrice(), bodyFont));
        document.add(new Paragraph("Weight: " + pack.getWeight() + " KG", bodyFont));
        document.add(new Paragraph("Address: " + pack.getAddress(), bodyFont));
    }

    private void setLogo(Document document) throws DocumentException, IOException {
        Image logo = Image.getInstance("src/main/resources/META-INF/resources/icons/icon.png");
        logo.scaleAbsolute(50f, 50f);
        logo.scaleToFit(60f, 60f);
        logo.setAbsolutePosition(15, 260);
        logo.setSpacingAfter(100f);
        document.add(logo);
    }
}
