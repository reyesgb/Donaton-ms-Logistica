package com.donaton.logistica.controller;

import com.donaton.logistica.dto.StockDTO;
import com.donaton.logistica.model.Recurso;
import com.donaton.logistica.service.RecursoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recursos")
@CrossOrigin(origins = "*")
public class RecursoController {

    private final RecursoService service;

    public RecursoController(RecursoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recurso> listar() {
        return service.listar();
    }

    @PostMapping("/ingresar")
    public Recurso ingresarStock(@RequestBody StockDTO dto) {

        return service.agregarStock(
                dto.getCategoria(),
                dto.getCantidad()
        );
    }
}