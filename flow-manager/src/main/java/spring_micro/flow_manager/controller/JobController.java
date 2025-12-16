// controller/JobController.java
package spring_micro.flow_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_micro.flow_manager.exception.JobNotFoundException;
import spring_micro.flow_manager.model.ConversionJob;
import spring_micro.flow_manager.model.ConversionStatus;
import spring_micro.flow_manager.service.MinioService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class JobController {

    private final MinioService minioService;
    private final spring_micro.flow_manager.repository.ConversionJobRepository jobRepository;

    @GetMapping("/status/{jobId}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable String jobId) {
        UUID id = UUID.fromString(jobId);
        ConversionJob job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        return ResponseEntity.ok(new StatusResponse(job.getStatus()));
    }

    @GetMapping("/download/{jobId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String jobId) throws Exception {
        UUID id = UUID.fromString(jobId);
        ConversionJob job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getStatus() != ConversionStatus.SUCCESS) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(minioService.download(job.getMinioOutputPath()));
        String filename = "converted_" + jobId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(resource);
    }

    public record StatusResponse(ConversionStatus status) {}
}
