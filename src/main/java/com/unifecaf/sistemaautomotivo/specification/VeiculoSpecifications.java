package com.unifecaf.sistemaautomotivo.specification;

import com.unifecaf.sistemaautomotivo.domain.Caminhao;
import com.unifecaf.sistemaautomotivo.domain.Carro;
import com.unifecaf.sistemaautomotivo.domain.Marca;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.Moto;
import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class VeiculoSpecifications {

    private static final Map<String, Class<? extends Veiculo>> TIPOS = Map.of(
            "CARRO", Carro.class,
            "MOTO", Moto.class,
            "CAMINHAO", Caminhao.class);

    private VeiculoSpecifications() {
    }

    public static Specification<Veiculo> comFiltros(String marca, String modelo,
            BigDecimal precoMin, BigDecimal precoMax, Integer ano,
            StatusVeiculo status, String tipo) {

        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            Join<Veiculo, Modelo> modeloJoin = null;
            if (StringUtils.hasText(marca) || StringUtils.hasText(modelo)) {
                modeloJoin = root.join("modelo");
            }
            if (StringUtils.hasText(modelo)) {
                predicados.add(cb.like(cb.lower(modeloJoin.get("nome")),
                        contains(modelo)));
            }
            if (StringUtils.hasText(marca)) {
                Join<Modelo, Marca> marcaJoin = modeloJoin.join("marca");
                predicados.add(cb.like(cb.lower(marcaJoin.get("nome")),
                        contains(marca)));
            }
            if (precoMin != null) {
                predicados.add(cb.greaterThanOrEqualTo(root.get("preco"), precoMin));
            }
            if (precoMax != null) {
                predicados.add(cb.lessThanOrEqualTo(root.get("preco"), precoMax));
            }
            if (ano != null) {
                predicados.add(cb.equal(root.get("anoFabricacao"), ano));
            }
            if (status != null) {
                predicados.add(cb.equal(root.get("status"), status));
            }
            if (StringUtils.hasText(tipo)) {
                Class<? extends Veiculo> classe = TIPOS.get(tipo.toUpperCase(Locale.ROOT));
                if (classe != null) {
                    predicados.add(cb.equal(root.type(), classe));
                }
            }

            return cb.and(predicados.toArray(Predicate[]::new));
        };
    }

    private static String contains(String valor) {
        return "%" + valor.toLowerCase(Locale.ROOT) + "%";
    }
}
