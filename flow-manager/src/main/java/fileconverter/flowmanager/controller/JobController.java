package fileconverter.flowmanager.controller;

import fileconverter.flowmanager.model.ConversionStatus;
import fileconverter.flowmanager.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/status/{jobId}")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable String jobId) {
        return ResponseEntity.ok(new StatusResponse(jobService.getStatus(jobId)));
    }

    @GetMapping("/download/{jobId}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String jobId) {
        try {
            var resource = jobService.downloadPdf(jobId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted_" + jobId + ".pdf\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(resource);
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public record StatusResponse(ConversionStatus status) {}
}
