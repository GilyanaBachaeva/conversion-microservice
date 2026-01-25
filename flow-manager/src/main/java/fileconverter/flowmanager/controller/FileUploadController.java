package fileconverter.flowmanager.controller;

import feign.FeignException;
import fileconverter.flowmanager.client.SubscriptionClient;
import fileconverter.flowmanager.dto.SubscriptionDto;
import fileconverter.flowmanager.dto.SubscriptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import fileconverter.flowmanager.model.UploadResponse;
import fileconverter.flowmanager.service.FileUploadService;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final SubscriptionClient subscriptionClient;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestHeader(value = "X-User-Login", required = false) String userLogin,
            @RequestParam("file") MultipartFile file) {

        if (userLogin == null || userLogin.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User login is required");
        }

        try {
            SubscriptionDto subscription = subscriptionClient.getSubscription(userLogin);

            // Проверка активности подписки
            if (!subscription.isActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Subscription is not active");
            }

            // Проверка размера файла в зависимости от типа подписки
            if (subscription.getType() == SubscriptionType.FREE && file.getSize() > 100_000_000L) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                        "Free users can upload files up to 100 MB");
            }

            // Дополнительные проверки для других типов подписок, если нужно
            if (subscription.getType() == SubscriptionType.PREMIUM && file.getSize() > 1_000_000_000L) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                        "Premium users can upload files up to 1 GB");
            }

            String jobId = fileUploadService.saveFileAndPublish(file, userLogin, subscription);
            return ResponseEntity.ok(new UploadResponse(jobId));

        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User subscription not found");
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Subscription service is unavailable");
        }
    }
}
