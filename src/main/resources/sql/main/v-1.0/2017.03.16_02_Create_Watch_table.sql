--changeset dk:CREATE_TABLE_watch

CREATE TABLE watch
(
    id                  bigint auto_increment NOT NULL,                -- // уникальный идентификатор
    trademark           character varying NOT NULL,     -- //
    type                character varying NOT NULL,     -- //
    quantity            int NOT NULL,                   -- //
    price               real NOT NULL,                  -- //
    date_open           timestamp NOT NULL,             -- // дата создания
    date_close          timestamp,                       -- //
    CONSTRAINT pk_watch PRIMARY KEY (id)
);

    --rollback DROP TABLE watch;