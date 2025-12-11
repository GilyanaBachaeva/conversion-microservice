package fileconverter.flowmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import fileconverter.flowmanager.model.UploadResponse;
import fileconverter.flowmanager.service.FileUploadService;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String jobId = fileUploadService.saveFileAndPublish(file);
        return ResponseEntity.ok(new UploadResponse(jobId));
    }
}
