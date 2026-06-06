package com.donaton.logistica.service;

import com.donaton.logistica.dto.NecesidadDTO;
import com.donaton.logistica.model.Envio;
import com.donaton.logistica.model.Recurso;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DespachoService {

    private final RecursoService recursoService;
    private final EnvioService envioService;
    private final NecesidadClientService necesidadClient;

    public DespachoService(
            RecursoService recursoService,
            EnvioService envioService,
            NecesidadClientService necesidadClient
    ) {
        this.recursoService = recursoService;
        this.envioService = envioService;
        this.necesidadClient = necesidadClient;
    }

    public Envio despachar(
            NecesidadDTO necesidad
    ) {

        Recurso stock = recursoService
                .buscarPorCategoria(
                        necesidad.getCategoria()
                );

        if (stock == null) {
            throw new RuntimeException(
                    "No existe stock para esta categoría"
            );
        }

        if (
                stock.getCantidadDisponible()
                        < necesidad.getCantidadNecesaria()
        ) {

            throw new RuntimeException(
                    "Stock insuficiente"
            );
        }

        recursoService.descontarStock(
                necesidad.getCategoria(),
                necesidad.getCantidadNecesaria()
        );

        Envio envio = new Envio();

        envio.setNecesidadId(
                necesidad.getId()
        );

        envio.setCategoria(
                necesidad.getCategoria()
        );

        envio.setCantidadDespachada(
                necesidad.getCantidadNecesaria()
        );

        envio.setDestino(
                necesidad.getComuna()
        );

        envio.setFecha(
                LocalDate.now()
        );

        envio = envioService.guardar(envio);

        necesidadClient.completarNecesidad(
                necesidad.getId()
        );

        return envio;
    }

    public List<Envio> listar(){
    return envioService.listar();}

}