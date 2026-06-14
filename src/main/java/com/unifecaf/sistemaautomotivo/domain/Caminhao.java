package com.unifecaf.sistemaautomotivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "caminhao")
public class Caminhao extends Veiculo {

    @Column(name = "capacidade_carga_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidadeCargaKg;

    @Column(name = "numero_eixos", nullable = false)
    private int numeroEixos;

    protected Caminhao() {
    }

    public Caminhao(Modelo modelo, int anoFabricacao, String cor, BigDecimal preco,
                    int quilometragem, StatusVeiculo status,
                    BigDecimal capacidadeCargaKg, int numeroEixos) {
        super(modelo, anoFabricacao, cor, preco, quilometragem, status);
        this.capacidadeCargaKg = capacidadeCargaKg;
        this.numeroEixos = numeroEixos;
    }

    @Override
    public String tipo() {
        return "CAMINHAO";
    }

    public BigDecimal getCapacidadeCargaKg() {
        return capacidadeCargaKg;
    }

    public void setCapacidadeCargaKg(BigDecimal capacidadeCargaKg) {
        this.capacidadeCargaKg = capacidadeCargaKg;
    }

    public int getNumeroEixos() {
        return numeroEixos;
    }

    public void setNumeroEixos(int numeroEixos) {
        this.numeroEixos = numeroEixos;
    }
}
