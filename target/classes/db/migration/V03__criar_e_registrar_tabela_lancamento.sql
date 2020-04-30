CREATE TABLE lancamento (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(50) NOT NULL,
    data_vencimento DATE,
    data_pagamento DATE,
    valor DECIMAL(10,2) NOT NULL,
    observacao TEXT,
    tipo_lancamento VARCHAR(20) NOT NULL,
    categoria_codigo BIGINT,
    pessoa_codigo BIGINT,
    FOREIGN KEY (categoria_codigo) REFERENCES categoria(codigo),
    FOREIGN KEY (pessoa_codigo) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
