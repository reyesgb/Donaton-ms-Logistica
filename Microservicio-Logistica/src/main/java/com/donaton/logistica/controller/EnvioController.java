package com.donaton.logistica.controller;

import com.donaton.logistica.dto.DespachoDTO;
import com.donaton.logistica.dto.EnvioDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/logistica")
@CrossOrigin(origins = "*")
public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    @PostMapping
    public Envio crear(@Valid @RequestBody EnvioDTO dto) {

        Envio envio = new Envio();

        envio.setNecesidadId(dto.getNecesidadId());
        envio.setCategoria(dto.getCategoria());
        envio.setCantidadDespachada(dto.getCantidadDespachada());
        envio.setDestino(dto.getDestino());
        envio.setFecha(LocalDate.now());

        return service.guardar(envio);
    }

    @GetMapping
    public List<Envio> listar() {
        return service.listar();
    }

}