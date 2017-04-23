CREATE TABLE watch (
  `id`          bigint(11)      NOT NULL AUTO_INCREMENT,
  `quantity`    int(11)         DEFAULT NULL,
  `price`       double(10,0)    DEFAULT NULL,
  `trademark`   varchar(45)     DEFAULT NULL,
  `type`        char(10)        DEFAULT NULL,
  `id_producer` bigint(11)      DEFAULT NULL,
  `id_user`     bigint(11)      DEFAULT NULL,
  `date_open`   datetime(6)     NOT NULL,
  `date_close`  datetime(6)     DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);

