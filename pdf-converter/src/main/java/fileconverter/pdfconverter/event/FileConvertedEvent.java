package fileconverter.pdfconverter.event;

import java.io.Serializable;

public record FileConvertedEvent(String jobId, String minioOutputPath, boolean success) implements Serializable {}