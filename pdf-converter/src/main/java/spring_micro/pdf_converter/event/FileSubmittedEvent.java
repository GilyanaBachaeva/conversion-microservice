package spring_micro.pdf_converter.event;

import java.io.Serializable;

public record FileSubmittedEvent(String jobId, String minioInputPath) implements Serializable {}
