package com.unifecaf.sistemaautomotivo.service;

import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import java.util.List;

public interface MarcaService {

    MarcaResponse criar(MarcaRequest request);

    List<MarcaResponse> listar();

    MarcaResponse buscarPorId(Long id);

    MarcaResponse atualizar(Long id, MarcaRequest request);

    void remover(Long id);
}
