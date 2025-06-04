package com.wl39.portfolio.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<?> postService(@RequestBody String name) {
        return serviceService.postService(name);
    }

    @GetMapping
    public ResponseEntity<?> getServices() {
        return serviceService.getServices();
    }
}
