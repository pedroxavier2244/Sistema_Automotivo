package com.unifecaf.sistemaautomotivo.service.impl;

import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.AtualizarVeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoFiltro;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.mapper.VeiculoMapper;
import com.unifecaf.sistemaautomotivo.repository.ModeloRepository;
import com.unifecaf.sistemaautomotivo.repository.VeiculoRepository;
import com.unifecaf.sistemaautomotivo.service.VeiculoService;
import com.unifecaf.sistemaautomotivo.specification.VeiculoSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ModeloRepository modeloRepository;
    private final VeiculoMapper mapper;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository,
            ModeloRepository modeloRepository, VeiculoMapper mapper) {
        this.veiculoRepository = veiculoRepository;
        this.modeloRepository = modeloRepository;
        this.mapper = mapper;
    }

    @Override
    public VeiculoResponse criar(VeiculoRequest request) {
        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> ResourceNotFoundException.de("Modelo", request.getModeloId()));
        Veiculo salvo = veiculoRepository.save(mapper.toEntity(request, modelo));
        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VeiculoResponse> consultar(VeiculoFiltro filtro, Pageable pageable) {
        Specification<Veiculo> spec = VeiculoSpecifications.comFiltros(
                filtro.marca(), filtro.modelo(), filtro.precoMin(), filtro.precoMax(),
                filtro.ano(), filtro.status(), filtro.tipo());
        return veiculoRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VeiculoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Override
    public VeiculoResponse atualizar(Long id, AtualizarVeiculoRequest request) {
        Veiculo veiculo = buscarEntidade(id);
        if (veiculo.isImutavel()) {
            throw new BusinessException(
                    "Veículo com status " + veiculo.getStatus()
                            + " não pode ser alterado.");
        }
        if (request.preco() != null) {
            veiculo.setPreco(request.preco());
        }
        if (request.quilometragem() != null) {
            veiculo.setQuilometragem(request.quilometragem());
        }
        if (request.status() != null) {
            veiculo.setStatus(request.status());
        }
        return mapper.toResponse(veiculo);
    }

    @Override
    public void remover(Long id) {
        veiculoRepository.delete(buscarEntidade(id));
    }

    private Veiculo buscarEntidade(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Veículo", id));
    }
}
