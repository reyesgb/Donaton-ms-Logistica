package com.donaton.logistica.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestTemplateConfigTest {

    @Test
    void restTemplate_DebeRetornarInstanciaNoNula() {
        // Arrange
        RestTemplateConfig config = new RestTemplateConfig();

        // Act
        RestTemplate restTemplate = config.restTemplate();

        // Assert
        assertNotNull(restTemplate, "El Bean de RestTemplate no debe ser nulo");
    }
}