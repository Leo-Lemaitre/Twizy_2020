-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Twizy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Twizy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Twizy` DEFAULT CHARACTER SET utf8 ;
USE `Twizy` ;

-- -----------------------------------------------------
-- Table `Twizy`.`PanneauxRefs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Twizy`.`PanneauxRefs` (
  `idPanneau` INT NOT NULL AUTO_INCREMENT,
  `nomPanneau` VARCHAR(45) NULL,
  PRIMARY KEY (`idPanneau`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Twizy`.`KeyPoints`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Twizy`.`KeyPoints` (
  `idKeyPoints` INT NOT NULL AUTO_INCREMENT,
  `KeyPoint` INT NULL,
  PRIMARY KEY (`idKeyPoints`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Twizy`.`Relation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Twizy`.`Relation` (
  `KeyPoints_idKey_points` INT NOT NULL,
  `PanneauxRefs_idPanneau` INT NOT NULL,
  INDEX `fk_Relation_KeyPoints_idx` (`KeyPoints_idKey_points` ASC),
  INDEX `fk_Relation_PanneauxRefs1_idx` (`PanneauxRefs_idPanneau` ASC),
  PRIMARY KEY (`KeyPoints_idKey_points`, `PanneauxRefs_idPanneau`),
  CONSTRAINT `fk_Relation_KeyPoints`
    FOREIGN KEY (`KeyPoints_idKey_points`)
    REFERENCES `Twizy`.`KeyPoints` (`idKeyPoints`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Relation_PanneauxRefs1`
    FOREIGN KEY (`PanneauxRefs_idPanneau`)
    REFERENCES `Twizy`.`PanneauxRefs` (`idPanneau`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
