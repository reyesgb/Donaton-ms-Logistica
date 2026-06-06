package com.donaton.logistica.service;

import com.donaton.logistica.dto.DonacionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecepcionDonacionService {

    private final RestTemplate restTemplate;
    private final RecursoService recursoService;

    public RecepcionDonacionService(
            RestTemplate restTemplate,
            RecursoService recursoService
    ) {
        this.restTemplate = restTemplate;
        this.recursoService = recursoService;
    }

    public void recepcionar(Long idDonacion) {
        DonacionDTO donacion =
                restTemplate.getForObject(
                        "http://localhost:8080/donaciones/" + idDonacion,
                        DonacionDTO.class
                );
        if (donacion == null) {
            throw new RuntimeException("Donación no encontrada");
        }

        recursoService.agregarStock(
                donacion.getCategoria(),
                donacion.getCantidad()
        );
    }
}