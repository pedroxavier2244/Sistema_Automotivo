package com.unifecaf.sistemaautomotivo.service.impl;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.mapper.MarcaMapper;
import com.unifecaf.sistemaautomotivo.repository.MarcaRepository;
import com.unifecaf.sistemaautomotivo.repository.ModeloRepository;
import com.unifecaf.sistemaautomotivo.service.MarcaService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final MarcaMapper mapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository, ModeloRepository modeloRepository,
            MarcaMapper mapper) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
        this.mapper = mapper;
    }

    @Override
    public MarcaResponse criar(MarcaRequest request) {
        garantirNomeDisponivel(request.nome());
        Marca salva = marcaRepository.save(mapper.toEntity(request));
        return mapper.toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponse> listar() {
        return marcaRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    @Override
    public MarcaResponse atualizar(Long id, MarcaRequest request) {
        Marca marca = buscarEntidade(id);
        if (!marca.getNome().equalsIgnoreCase(request.nome())) {
            garantirNomeDisponivel(request.nome());
        }
        marca.setNome(request.nome());
        return mapper.toResponse(marca);
    }

    @Override
    public void remover(Long id) {
        Marca marca = buscarEntidade(id);
        if (modeloRepository.existsByMarcaId(id)) {
            throw new BusinessException(
                    "Não é possível remover a marca: existem modelos associados a ela.");
        }
        marcaRepository.delete(marca);
    }

    private Marca buscarEntidade(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.de("Marca", id));
    }

    private void garantirNomeDisponivel(String nome) {
        if (marcaRepository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Já existe uma marca com o nome '" + nome + "'.");
        }
    }
}
