package com.unifecaf.sistemaautomotivo.service;

import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloRequest;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import java.util.List;

public interface ModeloService {

    ModeloResponse criar(ModeloRequest request);

    List<ModeloResponse> listar(Long marcaId);

    ModeloResponse buscarPorId(Long id);

    ModeloResponse atualizar(Long id, ModeloRequest request);

    void remover(Long id);
}
