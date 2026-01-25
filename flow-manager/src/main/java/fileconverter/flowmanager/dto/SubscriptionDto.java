package fileconverter.flowmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private String userLogin;
    private SubscriptionType type;
    private LocalDateTime expiryDate;

    public boolean isActive() {
        return expiryDate != null && expiryDate.isAfter(LocalDateTime.now());
    }
}
