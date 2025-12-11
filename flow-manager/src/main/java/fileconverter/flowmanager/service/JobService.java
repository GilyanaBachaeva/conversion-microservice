package fileconverter.flowmanager.service;

import fileconverter.flowmanager.exception.JobNotFoundException;
import fileconverter.flowmanager.model.ConversionJob;
import fileconverter.flowmanager.model.ConversionStatus;
import fileconverter.flowmanager.repository.ConversionJobRepository;
import fileconverter.flowmanager.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final ConversionJobRepository jobRepository;
    private final MinioService minioService;

    public ConversionStatus getStatus(String jobId) {
        UUID id = UUID.fromString(jobId);
        ConversionJob job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(jobId));
        return job.getStatus();
    }

    public InputStreamResource downloadPdf(String jobId) throws Exception {
        UUID id = UUID.fromString(jobId);
        ConversionJob job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getStatus() != ConversionStatus.SUCCESS) {
            throw new IllegalStateException("Job is not ready");
        }
        return new InputStreamResource(minioService.download(job.getMinioOutputPath()));
    }
}
