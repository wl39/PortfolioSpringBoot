package com.wl39.portfolio.subscription;

import com.wl39.portfolio.user.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/subscriptions")
@RequiredArgsConstructor
public class StudentServiceSubscriptionController {
    private final StudentServiceSubscriptionService studentServiceSubscriptionService;

    @PostMapping
    public ResponseEntity<?> postSubscription(@RequestBody @Valid SubscriptionRequest subscriptionRequest) {
        return studentServiceSubscriptionService.postSubscription(subscriptionRequest);
    }

    @PostMapping(path = "/multiples")
    public ResponseEntity<?> postSubscriptions(@RequestBody List<@Valid SubscriptionRequest> subscriptionRequests) {
        return studentServiceSubscriptionService.postSubscription(subscriptionRequests);
    }

    @GetMapping
    public ResponseEntity<?> getSubscriptions(@RequestParam String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equalsIgnoreCase(name)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        return studentServiceSubscriptionService.getSubscriptions(name);
    }
}
