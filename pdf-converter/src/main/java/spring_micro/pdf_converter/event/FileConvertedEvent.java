package spring_micro.pdf_converter.event;

import java.io.Serializable;

public record FileConvertedEvent(String jobId, String minioOutputPath, boolean success) implements Serializable {}