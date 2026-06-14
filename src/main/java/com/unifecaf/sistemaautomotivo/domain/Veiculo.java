package com.unifecaf.sistemaautomotivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "veiculo")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    @Column(name = "ano_fabricacao", nullable = false)
    private int anoFabricacao;

    @Column(nullable = false, length = 40)
    private String cor;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private int quilometragem;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private StatusVeiculo status;

    protected Veiculo() {
    }

    protected Veiculo(Modelo modelo, int anoFabricacao, String cor, BigDecimal preco,
                      int quilometragem, StatusVeiculo status) {
        this.modelo = modelo;
        this.anoFabricacao = anoFabricacao;
        this.cor = cor;
        this.preco = preco;
        this.quilometragem = quilometragem;
        this.status = status;
    }

    public abstract String tipo();

    public Long getId() {
        return id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public boolean isImutavel() {
        return status != null && status.isFinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Veiculo outro)) {
            return false;
        }
        return id != null && id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
