package com.donaton.logistica.service;

import com.donaton.logistica.model.Envio;
import com.donaton.logistica.repository.EnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioService {

    private final EnvioRepository repository;

    public EnvioService(EnvioRepository repository) {
        this.repository = repository;
    }

    public Envio guardar(Envio envio) {
        return repository.save(envio);
    }

    public List<Envio> listar() {
        return repository.findAll();
    }
}