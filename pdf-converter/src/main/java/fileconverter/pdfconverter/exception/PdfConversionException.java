package fileconverter.pdfconverter.exception;

public class PdfConversionException extends RuntimeException {
    public PdfConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfConversionException(String message) {
        super(message);
    }
}
