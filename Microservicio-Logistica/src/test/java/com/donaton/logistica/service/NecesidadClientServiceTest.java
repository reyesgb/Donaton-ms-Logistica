package com.donaton.logistica.service;

import com.donaton.logistica.dto.NecesidadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NecesidadClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NecesidadClientService necesidadClientService;

    private NecesidadDTO[] mockArray;

    @BeforeEach
    void setUp() {
        // Preparamos un arreglo simulado de respuestas
        NecesidadDTO necesidad1 = new NecesidadDTO();
        NecesidadDTO necesidad2 = new NecesidadDTO();
        mockArray = new NecesidadDTO[]{necesidad1, necesidad2};
    }

    @Test
    void obtenerNecesidadesActivas_DebeRetornarListaDeNecesidades() {
        // Arrange
        String urlEsperada = "http://localhost:8083/necesidades/activas";
        when(restTemplate.getForObject(urlEsperada, NecesidadDTO[].class))
                .thenReturn(mockArray);

        // Act
        List<NecesidadDTO> resultado = necesidadClientService.obtenerNecesidadesActivas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(restTemplate, times(1)).getForObject(urlEsperada, NecesidadDTO[].class);
    }

    @Test
    void completarNecesidad_DebeEjecutarPutConUrlCorrecta() {
        // Arrange
        Long idNecesidad = 5L;
        String urlEsperada = "http://localhost:8083/necesidades/5/estado?estado=COMPLETADA";

        // Act
        necesidadClientService.completarNecesidad(idNecesidad);

        // Assert
        // Como el método devuelve void, la aserción consiste en verificar que restTemplate.put
        // fue llamado exactamente 1 vez con los parámetros precisos.
        verify(restTemplate, times(1)).put(urlEsperada, null);
    }
}