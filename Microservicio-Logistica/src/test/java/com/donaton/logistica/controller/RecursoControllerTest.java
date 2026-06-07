package com.donaton.logistica.controller;

import com.donaton.logistica.dto.StockDTO;
import com.donaton.logistica.model.Recurso;
import com.donaton.logistica.service.RecursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecursoController.class)
@AutoConfigureMockMvc(addFilters = false) // Clave: desactiva la seguridad para probar solo el Controller
class RecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecursoService recursoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Recurso recursoMock;

    @BeforeEach
    void setUp() {
        // Inicializamos un objeto Recurso simulado para nuestras respuestas
        recursoMock = new Recurso();
        // Nota: Si Recurso tiene un constructor con parámetros o setters obligatorios,
        // puedes agregarlos aquí (ej: recursoMock.setId(1L);)
    }

    @Test
    void listar_DebeRetornarListaDeRecursos_Status200() throws Exception {
        // Arrange (Preparación)
        List<Recurso> listaEsperada = Arrays.asList(recursoMock);
        when(recursoService.listar()).thenReturn(listaEsperada);

        // Act & Assert (Ejecución y Verificación)
        mockMvc.perform(get("/recursos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void ingresarStock_DebeRetornarRecursoActualizado_Status200() throws Exception {
        // Arrange (Preparación)
        // Simulamos el DTO que enviará el cliente
        // Usamos un String JSON directo para no depender de cómo tengas construido tu DTO
        String requestJson = """
                {
                    "categoria": "Agua",
                    "cantidad": 50
                }
                """;

        // Simulamos la respuesta del servicio al recibir CUALQUIER categoría y CUALQUIER cantidad
        // Esto asegura que la prueba no falle por un problema de mapeo estricto de tipos en el mock
        when(recursoService.agregarStock(any(), any())).thenReturn(recursoMock);

        // Act & Assert (Ejecución y Verificación)
        mockMvc.perform(post("/recursos/ingresar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists()); // Verifica que responde un objeto JSON (el recurso)
    }
}