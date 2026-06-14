package com.unifecaf.sistemaautomotivo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unifecaf.sistemaautomotivo.domain.Carro;
import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.AtualizarVeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.CarroRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoFiltro;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import com.unifecaf.sistemaautomotivo.exception.ResourceNotFoundException;
import com.unifecaf.sistemaautomotivo.mapper.VeiculoMapper;
import com.unifecaf.sistemaautomotivo.repository.ModeloRepository;
import com.unifecaf.sistemaautomotivo.repository.VeiculoRepository;
import com.unifecaf.sistemaautomotivo.service.impl.VeiculoServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;
    @Mock
    private ModeloRepository modeloRepository;
    @Mock
    private VeiculoMapper mapper;

    @InjectMocks
    private VeiculoServiceImpl service;

    private final Modelo modelo = new Modelo("Corolla", new Marca("Toyota"));

    private CarroRequest carroRequest() {
        CarroRequest req = new CarroRequest();
        req.setModeloId(1L);
        req.setAnoFabricacao(2022);
        req.setCor("Preto");
        req.setPreco(new BigDecimal("120000"));
        req.setQuilometragem(15000);
        req.setStatus(StatusVeiculo.DISPONIVEL);
        req.setNumeroPortas(4);
        req.setTipoCombustivel("Flex");
        return req;
    }

    @Test
    void criarComModeloInexistenteLancaResourceNotFound() {
        when(modeloRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(carroRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void criarComModeloValidoPersiste() {
        CarroRequest req = carroRequest();
        Carro entidade = new Carro(modelo, 2022, "Preto", new BigDecimal("120000"),
                15000, StatusVeiculo.DISPONIVEL, 4, "Flex");
        when(modeloRepository.findById(1L)).thenReturn(Optional.of(modelo));
        when(mapper.toEntity(req, modelo)).thenReturn(entidade);
        when(veiculoRepository.save(entidade)).thenReturn(entidade);
        when(mapper.toResponse(entidade)).thenReturn(respostaFake());

        VeiculoResponse response = service.criar(req);

        assertThat(response).isNotNull();
        verify(veiculoRepository).save(entidade);
    }

    @Test
    void atualizarVeiculoVendidoLancaBusinessException() {
        Carro vendido = new Carro(modelo, 2022, "Preto", new BigDecimal("120000"),
                15000, StatusVeiculo.VENDIDO, 4, "Flex");
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(vendido));

        assertThatThrownBy(() -> service.atualizar(1L,
                new AtualizarVeiculoRequest(new BigDecimal("100000"), null, null)))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void atualizarVeiculoDisponivelAplicaCamposInformados() {
        Carro disponivel = new Carro(modelo, 2022, "Preto", new BigDecimal("120000"),
                15000, StatusVeiculo.DISPONIVEL, 4, "Flex");
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(disponivel));
        when(mapper.toResponse(disponivel)).thenReturn(respostaFake());

        service.atualizar(1L, new AtualizarVeiculoRequest(new BigDecimal("99000"), 20000,
                StatusVeiculo.RESERVADO));

        assertThat(disponivel.getPreco()).isEqualByComparingTo("99000");
        assertThat(disponivel.getQuilometragem()).isEqualTo(20000);
        assertThat(disponivel.getStatus()).isEqualTo(StatusVeiculo.RESERVADO);
    }

    @Test
    void consultarAplicaSpecificationEPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);
        Carro carro = new Carro(modelo, 2022, "Preto", new BigDecimal("120000"),
                15000, StatusVeiculo.DISPONIVEL, 4, "Flex");
        Page<Veiculo> page = new PageImpl<>(List.of(carro));
        when(veiculoRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.toResponse(carro)).thenReturn(respostaFake());

        Page<VeiculoResponse> resultado = service.consultar(
                new VeiculoFiltro(null, null, null, null, null, null, null), pageable);

        assertThat(resultado.getTotalElements()).isEqualTo(1);
    }

    private VeiculoResponse respostaFake() {
        return new VeiculoResponse(1L, "CARRO", 1L, "Corolla", 1L, "Toyota",
                2022, "Preto", new BigDecimal("120000"), 15000, StatusVeiculo.DISPONIVEL,
                4, "Flex", null, null, null);
    }
}
