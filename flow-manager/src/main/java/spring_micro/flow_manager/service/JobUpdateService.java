package spring_micro.flow_manager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_micro.flow_manager.event.FileConvertedEvent;
import spring_micro.flow_manager.model.ConversionJob;
import spring_micro.flow_manager.model.ConversionStatus;
import spring_micro.flow_manager.repository.ConversionJobRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobUpdateService {

    private final ConversionJobRepository jobRepository;

    @KafkaListener(topics = "file.converted", groupId = "flow-manager-group")
    @Transactional
    public void handleFileConverted(FileConvertedEvent event) {
        String jobIdStr = event.jobId();
        UUID jobId = UUID.fromString(jobIdStr);

        ConversionJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobIdStr));

        if (event.success()) {
            job.setStatus(ConversionStatus.SUCCESS);
            job.setMinioOutputPath(event.minioOutputPath());
        } else {
            job.setStatus(ConversionStatus.ERROR);
        }

        jobRepository.save(job);
        log.info("Job {} updated to status: {}", jobId, job.getStatus());
    }
}
