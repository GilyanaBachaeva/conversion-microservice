package spring_micro.flow_manager.model;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "conversion_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionJob {

    @Id
    private UUID id;

    private String originalFileName;
    private String minioInputPath;
    private String minioOutputPath;

    @Enumerated(EnumType.STRING)
    private ConversionStatus status;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = createdAt == null ? Instant.now() : createdAt;
        id = id == null ? UUID.randomUUID() : id;
    }
}
