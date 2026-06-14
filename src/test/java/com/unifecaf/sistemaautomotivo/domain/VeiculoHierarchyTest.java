package com.unifecaf.sistemaautomotivo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class VeiculoHierarchyTest {

    @Autowired
    private TestEntityManager entityManager;

    private Modelo persistirModelo(String marcaNome, String modeloNome) {
        Marca marca = new Marca(marcaNome);
        entityManager.persist(marca);
        Modelo modelo = new Modelo(modeloNome, marca);
        entityManager.persist(modelo);
        return modelo;
    }

    @Test
    void devePersistirCamposDaBaseEDaSubclasse() {
        Modelo corolla = persistirModelo("Toyota", "Corolla");
        Carro carro = new Carro(corolla, 2022, "Preto", new BigDecimal("120000.00"),
                15000, StatusVeiculo.DISPONIVEL, 4, "Flex");

        Long id = entityManager.persistAndGetId(carro, Long.class);
        entityManager.flush();
        entityManager.clear();

        Carro recarregado = entityManager.find(Carro.class, id);
        assertThat(recarregado.getAnoFabricacao()).isEqualTo(2022);
        assertThat(recarregado.getPreco()).isEqualByComparingTo("120000.00");
        assertThat(recarregado.getStatus()).isEqualTo(StatusVeiculo.DISPONIVEL);
        assertThat(recarregado.getNumeroPortas()).isEqualTo(4);
        assertThat(recarregado.getTipoCombustivel()).isEqualTo("Flex");
        assertThat(recarregado.getModelo().getMarca().getNome()).isEqualTo("Toyota");
    }

    @Test
    void consultaPolimorficaDeveRetornarSubtiposCorretos() {
        Modelo corolla = persistirModelo("Toyota", "Corolla");
        Modelo cb500 = persistirModelo("Honda", "CB 500");
        Modelo actros = persistirModelo("Mercedes", "Actros");

        entityManager.persist(new Carro(corolla, 2022, "Preto",
                new BigDecimal("120000"), 15000, StatusVeiculo.DISPONIVEL, 4, "Flex"));
        entityManager.persist(new Moto(cb500, 2021, "Vermelha",
                new BigDecimal("35000"), 8000, StatusVeiculo.DISPONIVEL, 500));
        entityManager.persist(new Caminhao(actros, 2020, "Branco",
                new BigDecimal("450000"), 120000, StatusVeiculo.RESERVADO,
                new BigDecimal("25000"), 3));
        entityManager.flush();
        entityManager.clear();

        List<Veiculo> veiculos = entityManager.getEntityManager()
                .createQuery("select v from Veiculo v", Veiculo.class)
                .getResultList();

        assertThat(veiculos).hasSize(3);
        assertThat(veiculos).anyMatch(v -> v instanceof Carro);
        assertThat(veiculos).anyMatch(v -> v instanceof Moto);
        assertThat(veiculos).anyMatch(v -> v instanceof Caminhao);
        assertThat(veiculos).extracting(Veiculo::tipo)
                .containsExactlyInAnyOrder("CARRO", "MOTO", "CAMINHAO");
    }
}
