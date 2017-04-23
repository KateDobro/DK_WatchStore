CREATE TABLE `users` (
  `id`          int(11)         NOT NULL AUTO_INCREMENT,
  `login`       varchar(45)     DEFAULT NULL,
  `password`    varchar(45)     DEFAULT NULL,
  `role`        char(10)        DEFAULT NULL,
  `date_open`   datetime(6)    NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `date_close`  datetime(6)    NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
);

