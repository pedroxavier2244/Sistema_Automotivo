package com.unifecaf.sistemaautomotivo.mapper;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import org.springframework.stereotype.Component;

@Component
public class MarcaMapper {

    public Marca toEntity(MarcaRequest request) {
        return new Marca(request.nome());
    }

    public MarcaResponse toResponse(Marca marca) {
        return new MarcaResponse(marca.getId(), marca.getNome());
    }
}
