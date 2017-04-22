--changeset dk:CREATE_TABLE_producer
CREATE TABLE producer
(
    id          bigint auto_increment NOT NULL, -- // уникальный идентификатор
    name        CHARACTER VARYING     NOT NULL,
    country     CHARACTER VARYING     NOT NULL,
    date_open   timestamp             NOT NULL, -- // дата поступления заявки
    date_close  timestamp,                      -- // дата обработки заявки
    CONSTRAINT pk_producer PRIMARY KEY (id)
);

        --rollback DROP TABLE producer;