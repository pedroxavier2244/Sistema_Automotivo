package com.unifecaf.sistemaautomotivo.service.impl;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloRequest;
import com.unifecaf.sistemaautomotivo.dto.modelo.ModeloResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.mapper.ModeloMapper;
import com.unifecaf.sistemaautomotivo.repository.MarcaRepository;
import com.unifecaf.sistemaautomotivo.repository.ModeloRepository;
import com.unifecaf.sistemaautomotivo.repository.VeiculoRepository;
import com.unifecaf.sistemaautomotivo.service.ModeloService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModeloServiceImpl implements ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final VeiculoRepository veiculoRepository;
    private final ModeloMapper mapper;

    public ModeloServiceImpl(ModeloRepository modeloRepository, MarcaRepository marcaRepository,
            VeiculoRepository veiculoRepository, ModeloMapper mapper) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
        this.veiculoRepository = veiculoRepository;
        this.mapper = mapper;
    }

    @Override
    public ModeloResponse criar(ModeloRequest request) {
        Marca marca = buscarMarca(request.marcaId());
        Modelo salvo = modeloRepository.save(mapper.toEntity(request, marca));
        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModeloResponse> listar(Long marcaId) {
        List<Modelo> modelos = (marcaId == null)
                ? modeloRepository.findAll()
                : modeloRepository.findByMarcaId(marcaId);
        return modelos.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ModeloResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Override
    public ModeloResponse atualizar(Long id, ModeloRequest request) {
        Modelo modelo = buscarEntidade(id);
        modelo.setNome(request.nome());
        modelo.setMarca(buscarMarca(request.marcaId()));
        return mapper.toResponse(modelo);
    }

    @Override
    public void remover(Long id) {
        Modelo modelo = buscarEntidade(id);
        if (veiculoRepository.existsByModeloId(id)) {
            throw new BusinessException(
                    "Não é possível remover o modelo: existem veículos associados a ele.");
        }
        modeloRepository.delete(modelo);
    }

    private Modelo buscarEntidade(Long id) {
        return modeloRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Modelo", id));
    }

    private Marca buscarMarca(Long marcaId) {
        return marcaRepository.findById(marcaId)
                .orElseThrow(() -> ResourceNotFoundException.de("Marca", marcaId));
    }
}
