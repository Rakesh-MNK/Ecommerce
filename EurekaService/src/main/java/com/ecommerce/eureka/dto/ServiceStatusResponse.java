package com.ecommerce.eureka.dto;

import java.time.Instant;

public record ServiceStatusResponse(
        String service,
        String status,
        String message,
        Instant timestamp
) {
}
