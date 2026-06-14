package com.unifecaf.sistemaautomotivo.controller;

import com.unifecaf.sistemaautomotivo.domain.StatusVeiculo;
import com.unifecaf.sistemaautomotivo.dto.veiculo.AtualizarVeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoFiltro;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoRequest;
import com.unifecaf.sistemaautomotivo.dto.veiculo.VeiculoResponse;
import com.unifecaf.sistemaautomotivo.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/veiculos")
@Tag(name = "Veículos", description = "Gestão do estoque de veículos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra um veículo (carro, moto ou caminhão)")
    public ResponseEntity<VeiculoResponse> criar(@Valid @RequestBody VeiculoRequest request) {
        VeiculoResponse criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/veiculos/" + criado.id())).body(criado);
    }

    @GetMapping
    @Operation(summary = "Consulta veículos com filtros e paginação")
    public Page<VeiculoResponse> consultar(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) StatusVeiculo status,
            @RequestParam(required = false) String tipo,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        VeiculoFiltro filtro = new VeiculoFiltro(marca, modelo, precoMin, precoMax, ano, status, tipo);
        return service.consultar(filtro, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um veículo por id")
    public VeiculoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza preço, quilometragem e/ou status de um veículo")
    public VeiculoResponse atualizar(@PathVariable Long id,
            @Valid @RequestBody AtualizarVeiculoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um veículo do estoque")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
