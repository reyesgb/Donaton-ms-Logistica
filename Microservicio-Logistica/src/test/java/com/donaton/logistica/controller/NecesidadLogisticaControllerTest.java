package com.donaton.logistica.controller;

import com.donaton.logistica.dto.NecesidadDTO;
import com.donaton.logistica.service.NecesidadClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NecesidadLogisticaController.class)
class NecesidadLogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NecesidadClientService service;

    @Test
    @WithMockUser
    void listar_DebeRetornarListaDeNecesidadesDesdeElServicio() throws Exception {
        // Arrange
        NecesidadDTO n1 = new NecesidadDTO();
        n1.setComuna("Santiago");

        NecesidadDTO n2 = new NecesidadDTO();
        n2.setComuna("Providencia");

        List<NecesidadDTO> lista = Arrays.asList(n1, n2);

        when(service.obtenerNecesidadesActivas()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/necesidades/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].comuna").value("Santiago"))
                .andExpect(jsonPath("$[1].comuna").value("Providencia"));

        verify(service, times(1)).obtenerNecesidadesActivas();
    }
}