CREATE TABLE `producers` (
  `id`          bigint     NOT NULL AUTO_INCREMENT,
  `name`        varchar(45) DEFAULT NULL,
  `country`     varchar(45) DEFAULT NULL,
  `date_open`   datetime(6) NOT NULL,
  `date_close`  datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);
