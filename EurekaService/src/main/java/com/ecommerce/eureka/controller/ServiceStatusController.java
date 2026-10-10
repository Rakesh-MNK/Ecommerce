package com.ecommerce.eureka.controller;

import com.ecommerce.eureka.dto.ServiceStatusResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/eureka")
@Validated
public class ServiceStatusController {

    @GetMapping("/status")
    public ServiceStatusResponse status(
            @RequestParam(defaultValue = "EUREKASERVICE")
            @NotBlank(message = "service must not be blank") String service) {
        return new ServiceStatusResponse(
                service,
                "UP",
                "Eureka Server is running locally",
                Instant.now()
        );
    }
}
