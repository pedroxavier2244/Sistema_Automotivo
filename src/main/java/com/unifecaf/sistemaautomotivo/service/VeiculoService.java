package com.unifecaf.sistemaautomotivo.service;

import com.unifecaf.sistemaautomotivo.dto.veiculo.AtualizarVeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoFiltro;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VeiculoService {

    VeiculoResponse criar(VeiculoRequest request);

    Page<VeiculoResponse> consultar(VeiculoFiltro filtro, Pageable pageable);

    VeiculoResponse buscarPorId(Long id);

    VeiculoResponse atualizar(Long id, AtualizarVeiculoRequest request);

    void remover(Long id);
}
