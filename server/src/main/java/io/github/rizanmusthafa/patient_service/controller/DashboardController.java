package io.github.rizanmusthafa.patient_service.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class DashboardController {

    /**
     * Serves index.html for /dashboard route
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Resource> dashboard() throws IOException {
        Resource resource = new ClassPathResource("static/dashboard/index.html");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }

    /**
     * Serves index.html for /dashboard/ route
     */
    @GetMapping("/dashboard/")
    public ResponseEntity<Resource> dashboardWithSlash() throws IOException {
        Resource resource = new ClassPathResource("static/dashboard/index.html");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }
}
