
package spring_micro.flow_manager.event;

import java.io.Serializable;

public record FileConvertedEvent(String jobId, String minioOutputPath, boolean success) implements Serializable {}