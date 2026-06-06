package com.donaton.logistica.service;

import com.donaton.logistica.dto.NecesidadDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NecesidadClientService {

    private final RestTemplate restTemplate;

    public NecesidadClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<NecesidadDTO> obtenerNecesidadesActivas() {

        NecesidadDTO[] respuesta =
                restTemplate.getForObject(
                        "http://localhost:8083/necesidades/activas",
                        NecesidadDTO[].class
                );

        return Arrays.asList(respuesta);
    }

    public void completarNecesidad(Long id) {

        restTemplate.put(
                "http://localhost:8083/necesidades/"
                        + id +
                        "/estado?estado=COMPLETADA",
                null
        );
    }
}