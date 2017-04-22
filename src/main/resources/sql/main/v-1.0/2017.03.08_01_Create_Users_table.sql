--changeset dk:CREATE_TABLE_users
    CREATE TABLE users
    (
        id                  bigint auto_increment NOT NULL, -- // уникальный идентификатор
        login               character varying NOT NULL,     -- // логин
        date_open           timestamp NOT NULL,             -- // дата создания
        date_close          timestamp,                      -- // дата удаления
        password            character varying NOT NULL,     -- // пароль
        role                character varying NOT NULL,     -- // роль в системе
        CONSTRAINT pk_users PRIMARY KEY (id)
    );

    INSERT INTO users (date_open, login, role, password)
        VALUES(
        {ts '2017-04-07 12:00:00.00'}, 'root', 'ROLE_ADMIN',
            '$2a$10$LijUmixpYL0i9rRvwXrnX.heUijboQzE3PsoCrxuJANIDVX28FNjS'
        );

--rollback DROP TABLE users;