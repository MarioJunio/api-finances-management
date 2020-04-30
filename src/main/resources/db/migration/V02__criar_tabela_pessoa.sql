CREATE TABLE pessoa (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    ativo TINYINT NOT NULL,
    logradouro VARCHAR(50),
    numero INTEGER,
    complemento VARCHAR(300),
    bairro VARCHAR(50),
    cep VARCHAR(10),
    cidade VARCHAR(50),
    estado CHAR(2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Mario Junio', true, 'Rua Duarte da Costa', 1575, 'Proximo ao Santorini', 'Jardim Zenith', '38500-000', 'Monte Carmelo', 'MG');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Alexandra Monteiro Caroline Braga', true, 'Rua G', 43, 'Casa da esquina', 'Bairro do Carmo', '38500-000', 'Monte Carmelo', 'MG');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Nadir loja', true, 'Rua Rui Barbosa', 183, 'Rossana Aviamentos', 'Centro', '38500-000', 'Joviania', 'GO');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Joao Silva', true, 'Av Belo Horizonte', 321, 'Próximo a auto escola', 'Batuque', '38500-000', 'Rio de Janeiro', 'RJ');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Henrique dos Anjos', true, 'Rua das Mangas', 1821, 'Próximo a Torre', 'Vila Dourada', '38500-000', 'Salvador', 'BA');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('José Roberto', true, 'Rua das Cervejas', 421, 'Condominio', 'Umuarama', '38513-020', 'Uberlandia', 'MG');

INSERT INTO pessoa(nome, ativo, logradouro, numero, complemento, bairro, cep, cidade, estado)
VALUES ('Camila Rodrigues', true, 'Rua das laranjeiras', 131, 'Casa branca', 'Centro', '31230-000', 'Caldas Novas', 'GO');