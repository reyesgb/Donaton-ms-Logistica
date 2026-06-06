package com.donaton.logistica.controller;

import com.donaton.logistica.service.RecepcionDonacionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistica")
@CrossOrigin(origins = "*")
public class LogisticaController {

    private final RecepcionDonacionService service;

    public LogisticaController(
            RecepcionDonacionService service
    ) {
        this.service = service;
    }

    @PostMapping("/recepcionar/{idDonacion}")
    public String recepcionar(
            @PathVariable Long idDonacion
    ) {

        service.recepcionar(idDonacion);

        return "Donación agregada al stock";
    }
}
