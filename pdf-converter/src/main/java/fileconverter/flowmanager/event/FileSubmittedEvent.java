package fileconverter.flowmanager.event;

import java.io.Serializable;

public record FileSubmittedEvent(String jobId, String minioInputPath) implements Serializable {}
