package Managefolio.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    
    @GetMapping("/status")
    public String status() {
        return "OK - Application is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}