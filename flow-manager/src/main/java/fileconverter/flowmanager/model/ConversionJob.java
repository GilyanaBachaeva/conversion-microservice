package fileconverter.flowmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String originalFileName;
    private String minioInputPath;
    private String minioOutputPath;

    @Enumerated(EnumType.STRING)
    private ConversionStatus status;

    @CreationTimestamp
    private Instant createdAt;
}
