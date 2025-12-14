package spring_micro.pdf_converter.exception;

public class PdfConversionException extends RuntimeException {
    public PdfConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfConversionException(String message) {
        super(message);
    }
}
