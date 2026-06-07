package com.donaton.logistica.service;

import com.donaton.logistica.dto.DonacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecepcionDonacionServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RecursoService recursoService;

    @InjectMocks
    private RecepcionDonacionService recepcionDonacionService;

    private DonacionDTO donacionMock;

    @BeforeEach
    void setUp() {
        donacionMock = new DonacionDTO();
        donacionMock.setCategoria("MEDICAMENTOS");
        donacionMock.setCantidad(150);
    }

    @Test
    void recepcionar_DebeAgregarStock_CuandoDonacionExiste() {
        // Arrange
        Long idDonacion = 10L;
        String urlEsperada = "http://localhost:8080/donaciones/10";

        // Simulamos que el RestTemplate encuentra la donación en el otro microservicio
        when(restTemplate.getForObject(urlEsperada, DonacionDTO.class)).thenReturn(donacionMock);

        // Act
        recepcionDonacionService.recepcionar(idDonacion);

        // Assert
        // 1. Verificamos que se hizo la llamada HTTP correcta
        verify(restTemplate, times(1)).getForObject(urlEsperada, DonacionDTO.class);
        // 2. Verificamos que se actualizó el stock con los datos extraídos del DTO
        verify(recursoService, times(1)).agregarStock("MEDICAMENTOS", 150);
    }

    @Test
    void recepcionar_DebeLanzarExcepcion_CuandoDonacionEsNull() {
        // Arrange
        Long idDonacion = 99L;
        String urlEsperada = "http://localhost:8080/donaciones/99";

        // Simulamos que la donación no existe (RestTemplate devuelve null)
        when(restTemplate.getForObject(urlEsperada, DonacionDTO.class)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recepcionDonacionService.recepcionar(idDonacion);
        });

        // Verificamos el mensaje de la excepción
        assertEquals("Donación no encontrada", exception.getMessage());

        // Verificamos que se intentó buscar
        verify(restTemplate, times(1)).getForObject(urlEsperada, DonacionDTO.class);

        // MUY IMPORTANTE: Verificamos que el proceso se detuvo y NUNCA se sumó stock
        verify(recursoService, never()).agregarStock(anyString(), anyInt());
    }
}