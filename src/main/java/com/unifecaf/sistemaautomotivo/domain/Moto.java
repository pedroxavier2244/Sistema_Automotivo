package com.unifecaf.sistemaautomotivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "moto")
public class Moto extends Veiculo {

    @Column(nullable = false)
    private int cilindrada;

    protected Moto() {
    }

    public Moto(Modelo modelo, int anoFabricacao, String cor, BigDecimal preco,
                int quilometragem, StatusVeiculo status, int cilindrada) {
        super(modelo, anoFabricacao, cor, preco, quilometragem, status);
        this.cilindrada = cilindrada;
    }

    @Override
    public String tipo() {
        return "MOTO";
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
}
