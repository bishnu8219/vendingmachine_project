CREATE USER 'test'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE vending;
GRANT ALL ON vending.* TO 'test'@'localhost';
USE vending;
CREATE TABLE `sale_event` (
	`sale_id` INT NOT NULL AUTO_INCREMENT,
	`product_id` INT(100) NOT NULL,
	`product_name` VARCHAR(250),
	`product_price` DOUBLE,
	`sale_time` TIMESTAMP DEFAULT now(),
	PRIMARY KEY (`sale_id`)
);