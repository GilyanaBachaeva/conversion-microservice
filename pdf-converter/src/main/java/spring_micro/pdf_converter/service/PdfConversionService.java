package spring_micro.pdf_converter.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_micro.pdf_converter.exception.PdfConversionException;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class PdfConversionService {

    public byte[] convertTextToPdf(byte[] textBytes) {
        if (textBytes == null || textBytes.length == 0) {
            throw new PdfConversionException("Input text is empty or null");
        }
        try {
            String text = new String(textBytes, StandardCharsets.UTF_8);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph(text));
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new PdfConversionException("PDF creation failed", e);
        }
    }
}
