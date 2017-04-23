INSERT INTO users (login, password, role, date_open)
VALUES (
    ('root', 'root', 'ADMIN', now())
);

INSERT INTO producers (name, county, date_open)
VALUES (
        ('CASIO', 'Japan', now()),
        ('Восток', 'Россия', now()),
        ('FESTINA', 'Switzerland',now())
);

INSERT INTO watch (quantity, price, trademark, type, id_producer, id_user, date_open, date_close)
VALUES (
        ('10', '99.9', "CASIO", "MECHANIC", "1", "2", now(), NULL ),
        ('15', '199.9', "Восток", "QUARTZ", "2", "2", now(), NULL ),
        ('12', '200.9', "FESTINA", "QUARTZ", "3", "2", now(), NULL )
);