-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema foodchain
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema foodchain
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `foodchain` DEFAULT CHARACTER SET utf8 ;
USE `foodchain` ;

-- -----------------------------------------------------
-- Table `foodchain`.`producers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`producers` (
  `producer_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `ethereum_account` VARCHAR(100) NULL,
  `url` VARCHAR(45) NULL,
  `license_number` VARCHAR(45) NULL,
  PRIMARY KEY (`producer_id`),
  INDEX `producer_id` (`producer_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`qr_codes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`qr_codes` (
  `qr_code_id` VARCHAR(100) NOT NULL,
  `previous_qr_code_id` VARCHAR(100) NULL,
  `producer_id` INT UNSIGNED NOT NULL,
  `latitude` DECIMAL(10,8) NULL,
  `longitude` DECIMAL(11,8) NULL,
  `date` DATETIME NULL,
  PRIMARY KEY (`qr_code_id`),
  INDEX `producer_id_idx` (`producer_id` ASC),
  INDEX `qr_code_id` (`qr_code_id` ASC),
  INDEX `qr_code_id_idx` (`previous_qr_code_id` ASC),
  CONSTRAINT `producer_id`
    FOREIGN KEY (`producer_id`)
    REFERENCES `foodchain`.`producers` (`producer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `qr_code_id`
    FOREIGN KEY (`previous_qr_code_id`)
    REFERENCES `foodchain`.`qr_codes` (`qr_code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`actions` (
  `action_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`action_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`producers_actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`producers_actions` (
  `producer_id` INT UNSIGNED NOT NULL,
  `action_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`producer_id`, `action_id`),
  INDEX `action_id_idx` (`action_id` ASC),
  CONSTRAINT `action_id`
    FOREIGN KEY (`action_id`)
    REFERENCES `foodchain`.`actions` (`action_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `producer_id`
    FOREIGN KEY (`producer_id`)
    REFERENCES `foodchain`.`producers` (`producer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`certificates` (
  `certificate_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`certificate_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`producers_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`producers_certificates` (
  `producer_id` INT UNSIGNED NOT NULL,
  `certificate_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`producer_id`, `certificate_id`),
  INDEX `certificate_id_idx` (`certificate_id` ASC),
  CONSTRAINT `certificate_id`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `foodchain`.`certificates` (`certificate_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `producer_id`
    FOREIGN KEY (`producer_id`)
    REFERENCES `foodchain`.`producers` (`producer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`qr_code_actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`qr_code_actions` (
  `qr_code_id` VARCHAR(100) NOT NULL,
  `action_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`qr_code_id`, `action_id`),
  INDEX `action_id_idx` (`action_id` ASC),
  CONSTRAINT `qr_code_id`
    FOREIGN KEY (`qr_code_id`)
    REFERENCES `foodchain`.`qr_codes` (`qr_code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `action_id`
    FOREIGN KEY (`action_id`)
    REFERENCES `foodchain`.`actions` (`action_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `foodchain`.`qr_code_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `foodchain`.`qr_code_certificates` (
  `qr_code_id` VARCHAR(100) NOT NULL,
  `certificate_id` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`qr_code_id`, `certificate_id`),
  INDEX `certificate_id_idx` (`certificate_id` ASC),
  CONSTRAINT `certificate_id`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `foodchain`.`certificates` (`certificate_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `qr_code_id`
    FOREIGN KEY (`qr_code_id`)
    REFERENCES `foodchain`.`qr_codes` (`qr_code_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
