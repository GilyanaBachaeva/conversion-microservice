package fileconverter.flowmanager.client;

import fileconverter.flowmanager.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "subscription-service", path = "/api/subscriptions")
public interface SubscriptionClient {

    @GetMapping("/{userLogin}")
    SubscriptionDto getSubscription(@PathVariable String userLogin);
}
