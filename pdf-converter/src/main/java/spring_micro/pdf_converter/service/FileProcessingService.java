// src/main/java/spring_micro/pdf_converter/service/FileProcessingService.java

package spring_micro.pdf_converter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring_micro.pdf_converter.event.FileConvertedEvent;
import spring_micro.pdf_converter.event.FileSubmittedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessingService {

    private final MinioService minioService;
    private final PdfConversionService pdfService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "file.submitted", groupId = "pdf-group")
    public void handle(FileSubmittedEvent event) {
        String jobId = event.jobId();
        log.info("Processing job: {}", jobId);

        try {
            // 1. Скачать
            byte[] text = minioService.download(event.minioInputPath());
            // 2. Конвертировать
            byte[] pdf = pdfService.convertTextToPdf(text);
            // 3. Сохранить
            String outputPath = "output/" + jobId + "/result.pdf";
            minioService.upload(pdf, outputPath);
            // 4. Отправить успех
            kafkaTemplate.send("file.converted", jobId, new FileConvertedEvent(jobId, outputPath, true));
            log.info("Job {} succeeded", jobId);
        } catch (Exception e) {
            log.error("Job {} failed", jobId, e);
            kafkaTemplate.send("file.converted", jobId, new FileConvertedEvent(jobId, null, false));
        }
    }
}
