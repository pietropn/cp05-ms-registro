CREATE TABLE tb_registro_meteorologico (
    registro_id        BIGINT        NOT NULL AUTO_INCREMENT,
    estacao_nome       VARCHAR(100)  NOT NULL,
    data_registro      DATE          NOT NULL,
    temperatura        DECIMAL(5,2)  NOT NULL,
    percentual_umidade DECIMAL(5,2)  NOT NULL,
    precipitacao       DECIMAL(7,2)  NOT NULL,
    condicao_climatica VARCHAR(20)   NOT NULL,
    CONSTRAINT pk_registro_meteorologico PRIMARY KEY (registro_id),
    CONSTRAINT ck_registro_umidade CHECK (percentual_umidade BETWEEN 0 AND 100),
    CONSTRAINT ck_registro_precipitacao CHECK (precipitacao >= 0),
    CONSTRAINT ck_registro_condicao CHECK (condicao_climatica IN ('ENSOLARADO', 'NUBLADO', 'CHUVOSO', 'TEMPESTADE'))
);
