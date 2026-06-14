package com.unifecaf.sistemaautomotivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "carro")
public class Carro extends Veiculo {

    @Column(name = "numero_portas", nullable = false)
    private int numeroPortas;

    @Column(name = "tipo_combustivel", nullable = false, length = 30)
    private String tipoCombustivel;

    protected Carro() {
    }

    public Carro(Modelo modelo, int anoFabricacao, String cor, BigDecimal preco,
                 int quilometragem, StatusVeiculo status, int numeroPortas, String tipoCombustivel) {
        super(modelo, anoFabricacao, cor, preco, quilometragem, status);
        this.numeroPortas = numeroPortas;
        this.tipoCombustivel = tipoCombustivel;
    }

    @Override
    public String tipo() {
        return "CARRO";
    }

    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
}
