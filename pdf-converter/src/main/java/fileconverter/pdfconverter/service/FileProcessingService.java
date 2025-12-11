package fileconverter.pdfconverter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import fileconverter.flowmanager.event.FileSubmittedEvent;
import fileconverter.pdfconverter.event.FileConvertedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessingService {

    private final MinioService minioService;
    private final PdfConversionService pdfService;
    private final KafkaTemplate<String, FileConvertedEvent> convertedEventKafkaTemplate;


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
            convertedEventKafkaTemplate.send("file.converted", jobId, new FileConvertedEvent(jobId, outputPath, true));
            log.info("Job {} succeeded", jobId);
        } catch (Exception e) {
            log.error("Job {} failed", jobId, e);
            convertedEventKafkaTemplate.send("file.converted", jobId, new FileConvertedEvent(jobId, null, false));
        }
    }
}
