package com.unifecaf.sistemaautomotivo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaRequest;
import com.unifecaf.sistemaautomotivo.dto.marca.MarcaResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.mapper.MarcaMapper;
import com.unifecaf.sistemaautomotivo.repository.MarcaRepository;
import com.unifecaf.sistemaautomotivo.repository.ModeloRepository;
import com.unifecaf.sistemaautomotivo.service.impl.MarcaServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;
    @Mock
    private ModeloRepository modeloRepository;
    @Mock
    private MarcaMapper mapper;

    @InjectMocks
    private MarcaServiceImpl service;

    @Test
    void criarComNomeDuplicadoLancaBusinessException() {
        when(marcaRepository.existsByNomeIgnoreCase("Toyota")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(new MarcaRequest("Toyota")))
                .isInstanceOf(BusinessException.class);
        verify(marcaRepository, never()).save(any());
    }

    @Test
    void criarComNomeNovoPersisteERetornaResponse() {
        MarcaRequest request = new MarcaRequest("Fiat");
        Marca entidade = new Marca("Fiat");
        when(marcaRepository.existsByNomeIgnoreCase("Fiat")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entidade);
        when(marcaRepository.save(entidade)).thenReturn(entidade);
        when(mapper.toResponse(entidade)).thenReturn(new MarcaResponse(1L, "Fiat"));

        MarcaResponse response = service.criar(request);

        assertThat(response.nome()).isEqualTo("Fiat");
        verify(marcaRepository).save(entidade);
    }

    @Test
    void buscarPorIdInexistenteLancaResourceNotFound() {
        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void removerMarcaComModelosLancaBusinessException() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(new Marca("Toyota")));
        when(modeloRepository.existsByMarcaId(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.remover(1L))
                .isInstanceOf(BusinessException.class);
        verify(marcaRepository, never()).delete(any());
    }

    @Test
    void removerMarcaSemModelosApaga() {
        Marca marca = new Marca("Toyota");
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(modeloRepository.existsByMarcaId(1L)).thenReturn(false);

        service.remover(1L);

        verify(marcaRepository).delete(marca);
    }
}
