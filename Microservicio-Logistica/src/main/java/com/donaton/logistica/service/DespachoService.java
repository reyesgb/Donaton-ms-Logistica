package com.donaton.logistica.service;

import com.donaton.logistica.dto.DespachoDTO;
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

    public Envio despachar(DespachoDTO despacho) {

        // 1. Evitamos problemas de mayúsculas/minúsculas de la categoría que viene del front
        String categoriaNormalizada = despacho.getCategoria().toUpperCase().trim();
        System.out.println("Categoria normalizada recibida: " + categoriaNormalizada);

        // 2. Buscamos el stock en la BD de Logística usando la categoría en mayúsculas
        Recurso stock = recursoService.buscarPorCategoria(categoriaNormalizada);

        if (stock == null) {
            throw new RuntimeException(
                    "No existe stock para la categoría: " + categoriaNormalizada
            );
        }

        // 3. Validamos usando la cantidad del DTO (Integer)
        if (stock.getCantidadDisponible() < despacho.getCantidad()) {
            throw new RuntimeException(
                    "Stock insuficiente para " + categoriaNormalizada
            );
        }

        // 4. Descontamos el stock
        recursoService.descontarStock(
                categoriaNormalizada,
                despacho.getCantidad()
        );

        // 5. Creamos el registro del Envío/Despacho para la base de datos
        Envio envio = new Envio();

        envio.setNecesidadId(
                despacho.getNecesidadId() // Usamos el nombre exacto de tu DTO
        );

        envio.setCategoria(
                categoriaNormalizada
        );

        envio.setCantidadDespachada(
                despacho.getCantidad()
        );

        envio.setDestino(
                despacho.getDestino()
        );

        envio.setFecha(
                LocalDate.now()
        );

        envio = envioService.guardar(envio);

        // 6. Notificamos al microservicio de Necesidades
        if (despacho.getNecesidadId() != null) {
            necesidadClient.completarNecesidad(
                    despacho.getNecesidadId()
            );
        }

        return envio;
    }
    public List<Envio> listar(){
    return envioService.listar();}

}