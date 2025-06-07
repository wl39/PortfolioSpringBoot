package com.wl39.portfolio.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ResponseEntity<?> postService(String name) {
        com.wl39.portfolio.services.Service service = new com.wl39.portfolio.services.Service();

        System.out.println(name);
        service.setName(name);

        return ResponseEntity.ok(serviceRepository.save(service));
    }

    public ResponseEntity<?> getServices() {
        List<String> services = new ArrayList<>();

        for (com.wl39.portfolio.services.Service service : serviceRepository.findAll()) {
            services.add(service.getName());
        }
        return ResponseEntity.ok(services);
    }
}
