package com.donaton.logistica.service;

import com.donaton.logistica.model.Recurso;
import com.donaton.logistica.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    private final RecursoRepository repository;

    public RecursoService(RecursoRepository repository) {
        this.repository = repository;
    }

    public Recurso agregarStock(String categoria, Integer cantidad) {

        Recurso recurso = repository
                .findByCategoriaIgnoreCase(categoria)
                .orElse(new Recurso());

        recurso.setCategoria(categoria);

        if (recurso.getCantidadDisponible() == null) {
            recurso.setCantidadDisponible(cantidad);
        } else {
            recurso.setCantidadDisponible(
                    recurso.getCantidadDisponible() + cantidad
            );
        }

        return repository.save(recurso);
    }

    public Recurso descontarStock(
            String categoria,
            Integer cantidad
    ) {

        Recurso recurso = repository
                .findByCategoriaIgnoreCase(categoria)
                .orElseThrow(() ->
                        new RuntimeException("No existe stock para la categoria"));

        if (recurso.getCantidadDisponible() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        recurso.setCantidadDisponible(
                recurso.getCantidadDisponible() - cantidad
        );

        return repository.save(recurso);
    }


    public Recurso buscarPorCategoria(
            String categoria
    ) {

        return repository
                .findByCategoriaIgnoreCase(categoria)
                .orElse(null);
    }

    public List<Recurso> listar() {
        return repository.findAll();
    }
}