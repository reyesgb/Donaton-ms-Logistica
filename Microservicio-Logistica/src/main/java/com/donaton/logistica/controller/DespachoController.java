package com.donaton.logistica.controller;

import com.donaton.logistica.dto.NecesidadDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.service.DespachoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/despachos")
@CrossOrigin(origins = "*")
public class DespachoController {

    private final DespachoService service;

    public DespachoController(
            DespachoService service
    ) {
        this.service = service;
    }

    @PostMapping
    public Envio despachar(
            @RequestBody NecesidadDTO necesidad
    ) {
        return service.despachar(necesidad);
    }

    @GetMapping
    public List<Envio> listar() {
        return service.listar();
    }

}