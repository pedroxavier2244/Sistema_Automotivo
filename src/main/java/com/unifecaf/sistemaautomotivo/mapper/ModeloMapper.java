package com.unifecaf.sistemaautomotivo.mapper;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloRequest;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import org.springframework.stereotype.Component;

@Component
public class ModeloMapper {

    public Modelo toEntity(ModeloRequest request, Marca marca) {
        return new Modelo(request.nome(), marca);
    }

    public ModeloResponse toResponse(Modelo modelo) {
        Marca marca = modelo.getMarca();
        return new ModeloResponse(modelo.getId(), modelo.getNome(),
                marca.getId(), marca.getNome());
    }
}
