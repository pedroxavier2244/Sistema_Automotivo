package com.unifecaf.sistemaautomotivo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.unifecaf.sistemaautomotivo.domain.Caminhao;
import com.unifecaf.sistemaautomotivo.domain.Carro;
import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.Moto;
import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import com.unifecaf.sistemaautomotivo.specification.VeiculoSpecifications;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class VeiculoSpecificationTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private VeiculoRepository repository;

    private Modelo corolla;
    private Modelo civic;
    private Modelo cb500;
    private Modelo actros;

    @BeforeEach
    void seed() {
        Marca toyota = em.persist(new Marca("Toyota"));
        Marca honda = em.persist(new Marca("Honda"));
        Marca mercedes = em.persist(new Marca("Mercedes-Benz"));
        corolla = em.persist(new Modelo("Corolla", toyota));
        civic = em.persist(new Modelo("Civic", honda));
        cb500 = em.persist(new Modelo("CB 500", honda));
        actros = em.persist(new Modelo("Actros", mercedes));

        em.persist(new Carro(corolla, 2022, "Preto", new BigDecimal("120000"), 15000,
                StatusVeiculo.DISPONIVEL, 4, "Flex"));
        em.persist(new Carro(civic, 2021, "Branco", new BigDecimal("110000"), 30000,
                StatusVeiculo.VENDIDO, 4, "Gasolina"));
        em.persist(new Moto(cb500, 2020, "Vermelha", new BigDecimal("35000"), 8000,
                StatusVeiculo.DISPONIVEL, 500));
        em.persist(new Caminhao(actros, 2019, "Branco", new BigDecimal("450000"), 200000,
                StatusVeiculo.RESERVADO, new BigDecimal("25000"), 3));
        em.flush();
        em.clear();
    }

    @Test
    void semFiltrosRetornaTodos() {
        List<Veiculo> r = repository.findAll(
                VeiculoSpecifications.comFiltros(null, null, null, null, null, null, null));
        assertThat(r).hasSize(4);
    }

    @Test
    void filtraPorFaixaDePreco() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                null, null, new BigDecimal("100000"), new BigDecimal("130000"),
                null, null, null));
        assertThat(r).hasSize(2)
                .allMatch(v -> v.getPreco().compareTo(new BigDecimal("100000")) >= 0
                        && v.getPreco().compareTo(new BigDecimal("130000")) <= 0);
    }

    @Test
    void filtraPorStatus() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                null, null, null, null, null, StatusVeiculo.DISPONIVEL, null));
        assertThat(r).hasSize(2)
                .allMatch(v -> v.getStatus() == StatusVeiculo.DISPONIVEL);
    }

    @Test
    void filtraPorTipo() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                null, null, null, null, null, null, "CARRO"));
        assertThat(r).hasSize(2).allMatch(v -> v instanceof Carro);
    }

    @Test
    void filtraPorMarcaIgnoreCase() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                "honda", null, null, null, null, null, null));
        assertThat(r).hasSize(2);
    }

    @Test
    void filtraPorModeloEAno() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                null, "corolla", null, null, 2022, null, null));
        assertThat(r).hasSize(1);
        assertThat(r.get(0)).isInstanceOf(Carro.class);
    }

    @Test
    void combinaFiltros() {
        List<Veiculo> r = repository.findAll(VeiculoSpecifications.comFiltros(
                null, null, null, new BigDecimal("200000"), null,
                StatusVeiculo.DISPONIVEL, "CARRO"));

        assertThat(r).hasSize(1).allMatch(v -> v instanceof Carro
                && v.getStatus() == StatusVeiculo.DISPONIVEL);
    }
}
