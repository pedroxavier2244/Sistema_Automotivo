-- =====================================================================
-- Dados de exemplo para demonstração (marcas, modelos e veículos).
-- IDs explícitos para manter os relacionamentos determinísticos.
-- =====================================================================

INSERT INTO marca (id, nome) VALUES
    (1, 'Toyota'),
    (2, 'Honda'),
    (3, 'Mercedes-Benz');

INSERT INTO modelo (id, nome, marca_id) VALUES
    (1, 'Corolla', 1),
    (2, 'Hilux',   1),
    (3, 'Civic',   2),
    (4, 'CB 500',  2),
    (5, 'Actros',  3);

-- Tabela base da hierarquia.
INSERT INTO veiculo (id, modelo_id, ano_fabricacao, cor, preco, quilometragem, status) VALUES
    (1, 1, 2022, 'Preto',    120000.00,  15000, 'DISPONIVEL'),
    (2, 3, 2021, 'Branco',   110000.00,  30000, 'DISPONIVEL'),
    (3, 4, 2020, 'Vermelha',  35000.00,   8000, 'DISPONIVEL'),
    (4, 5, 2019, 'Branco',   450000.00, 200000, 'RESERVADO'),
    (5, 2, 2023, 'Prata',    250000.00,   5000, 'DISPONIVEL');

-- Subclasses (PK = FK -> veiculo.id).
INSERT INTO carro (id, numero_portas, tipo_combustivel) VALUES
    (1, 4, 'Flex'),
    (2, 4, 'Gasolina'),
    (5, 4, 'Diesel');

INSERT INTO moto (id, cilindrada) VALUES
    (3, 500);

INSERT INTO caminhao (id, capacidade_carga_kg, numero_eixos) VALUES
    (4, 25000.00, 3);

-- Avança os contadores AUTO_INCREMENT para além dos IDs inseridos manualmente,
-- garantindo que o próximo cadastro via API não colida com os dados de exemplo.
ALTER TABLE marca   AUTO_INCREMENT = 4;
ALTER TABLE modelo  AUTO_INCREMENT = 6;
ALTER TABLE veiculo AUTO_INCREMENT = 6;
