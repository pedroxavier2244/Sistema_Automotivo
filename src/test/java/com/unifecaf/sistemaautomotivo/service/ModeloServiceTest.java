package com.unifecaf.sistemaautomotivo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.unifecaf.sistemaautomotivo.service.impl.ModeloServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModeloServiceTest {

    @Mock
    private ModeloRepository modeloRepository;
    @Mock
    private MarcaRepository marcaRepository;
    @Mock
    private VeiculoRepository veiculoRepository;
    @Mock
    private ModeloMapper mapper;

    @InjectMocks
    private ModeloServiceImpl service;

    @Test
    void criarComMarcaInexistenteLancaResourceNotFound() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(new ModeloRequest("Corolla", 1L)))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(modeloRepository, never()).save(any());
    }

    @Test
    void criarComMarcaValidaPersisteERetornaResponse() {
        ModeloRequest request = new ModeloRequest("Corolla", 1L);
        Marca marca = new Marca("Toyota");
        Modelo entidade = new Modelo("Corolla", marca);
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(mapper.toEntity(request, marca)).thenReturn(entidade);
        when(modeloRepository.save(entidade)).thenReturn(entidade);
        when(mapper.toResponse(entidade))
                .thenReturn(new ModeloResponse(1L, "Corolla", 1L, "Toyota"));

        ModeloResponse response = service.criar(request);

        assertThat(response.nome()).isEqualTo("Corolla");
        assertThat(response.marcaNome()).isEqualTo("Toyota");
        verify(modeloRepository).save(entidade);
    }

    @Test
    void removerModeloComVeiculosLancaBusinessException() {
        when(modeloRepository.findById(1L))
                .thenReturn(Optional.of(new Modelo("Corolla", new Marca("Toyota"))));
        when(veiculoRepository.existsByModeloId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.remover(1L))
                .isInstanceOf(BusinessException.class);
        verify(modeloRepository, never()).delete(any());
    }

    @Test
    void removerModeloSemVeiculosApaga() {
        Modelo modelo = new Modelo("Corolla", new Marca("Toyota"));
        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(veiculoRepository.existsByModeloId(1L)).thenReturn(false);

        service.remover(1L);

        verify(modeloRepository).delete(modelo);
    }
}
