package com.ecommerce.eureka.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceStatusControllerTest {

    private final ServiceStatusController controller = new ServiceStatusController();

    @Test
    void shouldReturnUpStatus() {
        var response = controller.status("EUREKASERVICE");

        assertEquals("EUREKASERVICE", response.service());
        assertEquals("UP", response.status());
        assertNotNull(response.timestamp());
    }
}
