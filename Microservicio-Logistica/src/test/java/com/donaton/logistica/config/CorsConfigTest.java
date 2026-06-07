package com.donaton.logistica.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void corsConfigurationSource_DebeRetornarConfiguracionCorrecta() {
        // Arrange
        CorsConfig corsConfig = new CorsConfig();

        // Act
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        // Creamos un request HTTP simulado a una ruta cualquiera para ver qué reglas CORS se le aplican
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/recursos");
        CorsConfiguration config = source.getCorsConfiguration(request);

        // Assert
        assertNotNull(config, "La configuración CORS no debe ser nula");

        // Verificamos los orígenes, métodos y encabezados permitidos
        assertTrue(config.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(config.getAllowedMethods().contains("*"));
        assertTrue(config.getAllowedHeaders().contains("*"));

        // Verificamos las credenciales
        assertTrue(config.getAllowCredentials());
    }

    @Test
    void corsConfigurationSource_DebeAplicarseATodasLasRutas() {
        // Arrange
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        // Act
        MockHttpServletRequest request1 = new MockHttpServletRequest("GET", "/ruta1");
        MockHttpServletRequest request2 = new MockHttpServletRequest("POST", "/otra/ruta/completamente/distinta");

        // Assert
        // Al haber configurado "/**", cualquier ruta debe retornar nuestra configuración CORS en lugar de null
        assertNotNull(source.getCorsConfiguration(request1));
        assertNotNull(source.getCorsConfiguration(request2));
    }
}