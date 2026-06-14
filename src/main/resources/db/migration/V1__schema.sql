-- =====================================================================
-- Sistema Automotivo - Modelo físico (MySQL 8)
-- Hierarquia de veículos mapeada por herança JPA JOINED:
--   veiculo (base)  ──<  carro | moto | caminhao  (PK = FK -> veiculo.id)
--   marca (1) ──< modelo (N) ──< veiculo (N)
-- =====================================================================

CREATE TABLE marca (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_marca_nome UNIQUE (nome)
) ENGINE = InnoDB;

CREATE TABLE modelo (
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(80) NOT NULL,
    marca_id BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_modelo_marca FOREIGN KEY (marca_id) REFERENCES marca (id)
) ENGINE = InnoDB;

CREATE TABLE veiculo (
    id             BIGINT         NOT NULL AUTO_INCREMENT,
    modelo_id      BIGINT         NOT NULL,
    ano_fabricacao INT            NOT NULL,
    cor            VARCHAR(40)    NOT NULL,
    preco          DECIMAL(12, 2) NOT NULL,
    quilometragem  INT            NOT NULL,
    status         VARCHAR(20)    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY (modelo_id) REFERENCES modelo (id)
) ENGINE = InnoDB;

CREATE TABLE carro (
    id               BIGINT      NOT NULL,
    numero_portas    INT         NOT NULL,
    tipo_combustivel VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_carro_veiculo FOREIGN KEY (id) REFERENCES veiculo (id)
) ENGINE = InnoDB;

CREATE TABLE moto (
    id         BIGINT NOT NULL,
    cilindrada INT    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_moto_veiculo FOREIGN KEY (id) REFERENCES veiculo (id)
) ENGINE = InnoDB;

CREATE TABLE caminhao (
    id                  BIGINT         NOT NULL,
    capacidade_carga_kg DECIMAL(10, 2) NOT NULL,
    numero_eixos        INT            NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_caminhao_veiculo FOREIGN KEY (id) REFERENCES veiculo (id)
) ENGINE = InnoDB;

-- Índices para os filtros e consultas mais frequentes (requisito de performance).
CREATE INDEX idx_modelo_marca   ON modelo (marca_id);
CREATE INDEX idx_veiculo_modelo ON veiculo (modelo_id);
CREATE INDEX idx_veiculo_status ON veiculo (status);
CREATE INDEX idx_veiculo_preco  ON veiculo (preco);
CREATE INDEX idx_veiculo_ano    ON veiculo (ano_fabricacao);
