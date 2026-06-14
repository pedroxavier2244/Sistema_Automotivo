package com.unifecaf.sistemaautomotivo.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.unifecaf.sistemaautomotivo.domain.Carro;
import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.Moto;
import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.CaminhaoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.CarroRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.MotoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class VeiculoMapperTest {

    private final VeiculoMapper mapper = new VeiculoMapper();
    private final Modelo modelo = new Modelo("Corolla", new Marca("Toyota"));

    private void preencherComuns(com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoRequest r) {
        r.setModeloId(1L);
        r.setAnoFabricacao(2022);
        r.setCor("Preto");
        r.setPreco(new BigDecimal("120000"));
        r.setQuilometragem(15000);
        r.setStatus(StatusVeiculo.DISPONIVEL);
    }

    @Test
    void carroRequestViraCarro() {
        CarroRequest req = new CarroRequest();
        preencherComuns(req);
        req.setNumeroPortas(4);
        req.setTipoCombustivel("Flex");

        Veiculo veiculo = mapper.toEntity(req, modelo);

        assertThat(veiculo).isInstanceOf(Carro.class);
        assertThat(((Carro) veiculo).getNumeroPortas()).isEqualTo(4);
        assertThat(veiculo.tipo()).isEqualTo("CARRO");
    }

    @Test
    void motoRequestViraMoto() {
        MotoRequest req = new MotoRequest();
        preencherComuns(req);
        req.setCilindrada(500);

        Veiculo veiculo = mapper.toEntity(req, modelo);

        assertThat(veiculo).isInstanceOf(Moto.class);
        assertThat(((Moto) veiculo).getCilindrada()).isEqualTo(500);
    }

    @Test
    void caminhaoRequestViraCaminhao() {
        CaminhaoRequest req = new CaminhaoRequest();
        preencherComuns(req);
        req.setCapacidadeCargaKg(new BigDecimal("25000"));
        req.setNumeroEixos(3);

        Veiculo veiculo = mapper.toEntity(req, modelo);

        assertThat(veiculo.tipo()).isEqualTo("CAMINHAO");
    }

    @Test
    void respostaDeCarroPreencheSoCamposDeCarro() {
        Carro carro = new Carro(modelo, 2022, "Preto", new BigDecimal("120000"),
                15000, StatusVeiculo.DISPONIVEL, 4, "Flex");

        VeiculoResponse response = mapper.toResponse(carro);

        assertThat(response.tipo()).isEqualTo("CARRO");
        assertThat(response.numeroPortas()).isEqualTo(4);
        assertThat(response.marcaNome()).isEqualTo("Toyota");
        assertThat(response.cilindrada()).isNull();
        assertThat(response.capacidadeCargaKg()).isNull();
    }
}
