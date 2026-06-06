package com.donaton.logistica.controller;

import com.donaton.logistica.dto.NecesidadDTO;
import com.donaton.logistica.service.NecesidadClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/necesidades")
@CrossOrigin(origins = "*")
public class NecesidadLogisticaController {

    private final NecesidadClientService service;

    public NecesidadLogisticaController(
            NecesidadClientService service
    ) {
        this.service = service;
    }

    @GetMapping("/activas")
    public List<NecesidadDTO> listar() {

        return service.obtenerNecesidadesActivas();
    }

}