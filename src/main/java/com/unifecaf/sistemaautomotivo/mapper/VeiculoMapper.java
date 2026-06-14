package com.unifecaf.sistemaautomotivo.mapper;

import com.unifecaf.sistemaautomotivo.domain.Caminhao;
import com.unifecaf.sistemaautomotivo.domain.Carro;
import com.unifecaf.sistemaautomotivo.domain.Modelo;
import com.unifecaf.sistemaautomotivo.domain.Moto;
import com.unifecaf.sistemaautomotivo.domain.Veiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.CaminhaoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.CarroRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.MotoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import com.unifecaf.sistemaautomotivo.exception.BusinessException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public Veiculo toEntity(VeiculoRequest request, Modelo modelo) {
        if (request instanceof CarroRequest carro) {
            return new Carro(modelo, carro.getAnoFabricacao(), carro.getCor(), carro.getPreco(),
                    carro.getQuilometragem(), carro.getStatus(),
                    carro.getNumeroPortas(), carro.getTipoCombustivel());
        }
        if (request instanceof MotoRequest moto) {
            return new Moto(modelo, moto.getAnoFabricacao(), moto.getCor(), moto.getPreco(),
                    moto.getQuilometragem(), moto.getStatus(), moto.getCilindrada());
        }
        if (request instanceof CaminhaoRequest caminhao) {
            return new Caminhao(modelo, caminhao.getAnoFabricacao(), caminhao.getCor(),
                    caminhao.getPreco(), caminhao.getQuilometragem(), caminhao.getStatus(),
                    caminhao.getCapacidadeCargaKg(), caminhao.getNumeroEixos());
        }
        throw new BusinessException("Tipo de veículo não suportado.");
    }

    public VeiculoResponse toResponse(Veiculo v) {
        Integer numeroPortas = null;
        String tipoCombustivel = null;
        Integer cilindrada = null;
        BigDecimal capacidadeCargaKg = null;
        Integer numeroEixos = null;

        if (v instanceof Carro carro) {
            numeroPortas = carro.getNumeroPortas();
            tipoCombustivel = carro.getTipoCombustivel();
        } else if (v instanceof Moto moto) {
            cilindrada = moto.getCilindrada();
        } else if (v instanceof Caminhao caminhao) {
            capacidadeCargaKg = caminhao.getCapacidadeCargaKg();
            numeroEixos = caminhao.getNumeroEixos();
        }

        Modelo modelo = v.getModelo();
        return new VeiculoResponse(
                v.getId(), v.tipo(),
                modelo.getId(), modelo.getNome(),
                modelo.getMarca().getId(), modelo.getMarca().getNome(),
                v.getAnoFabricacao(), v.getCor(), v.getPreco(),
                v.getQuilometragem(), v.getStatus(),
                numeroPortas, tipoCombustivel, cilindrada,
                capacidadeCargaKg, numeroEixos);
    }
}
