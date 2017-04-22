
        INSERT INTO producer (date_open, name, country, date_close) VALUES
            ({ts '2017-04-07 12:00:00.00'}, 'producer_1', 'country_1', null),
            ({ts '2017-04-07 12:00:00.00'}, 'producer_11', 'country_11', {ts '2017-04-07 12:00:00.00'}),
            ({ts '2017-04-07 12:00:00.00'}, 'producer_2', 'country_2', null) ;

        INSERT INTO watch (date_open, trademark, type, quantity, price, date_close) VALUES
            ({ts '2017-04-07 12:00:00.00'}, 'trademark_1', 'MECHANIC', 1, 100.0, null),
            ({ts '2017-04-07 12:00:00.00'}, 'trademark_1', 'MECHANIC', 1, 100.0, {ts '2017-04-07 12:00:00.00'}),
            ({ts '2017-04-07 12:00:00.00'}, 'trademark_2', 'QUARTZ', 2, 200.0, null) ;