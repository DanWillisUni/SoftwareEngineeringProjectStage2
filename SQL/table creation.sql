CREATE TABLE IF NOT EXISTS `softwareengineering`.`User` (
  `idUser` INT NOT NULL,
  `forename` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` NVARCHAR(20) NOT NULL,
  `DOB` DATE NOT NULL,
  `height` SMALLINT,
  `gender` VARCHAR(1) NOT NULL,
  `weight` SMALLINT,
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);  
CREATE TABLE IF NOT EXISTS `softwareengineering`.`GoalWeight` (
  `idGoalWeight` INT NOT NULL,
  `idUser` INT NOT NULL,
  `weightGoal` SMALLINT NOT NULL,
  `dateSet` DATE NULL,
  `targetDate` DATE NOT NULL,
  `toLoose` TINYINT NOT NULL,
  PRIMARY KEY (`idGoalWeight`),
  UNIQUE INDEX `idGoalWeight_UNIQUE` (`idGoalWeight` ASC) VISIBLE,
  CONSTRAINT `idUserinGoal`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Exercise` (
  `idExerciseType` INT NOT NULL,
  `calsPerMinute` DECIMAL(10) NOT NULL,
  `exerciseName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idExerciseType`),
  UNIQUE INDEX `idExerciseType_UNIQUE` (`idExerciseType` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`ExerciseSession` (
  `idExerciseSession` INT NOT NULL,
  `durationMinutes` SMALLINT NULL,
  `idExerciseType` INT NOT NULL,
  `caloriesBurned` SMALLINT NULL,
  PRIMARY KEY (`idExerciseSession`),
  UNIQUE INDEX `idExcerciseSession_UNIQUE` (`idExerciseSession` ASC) VISIBLE,
  CONSTRAINT `idExerciseTypeinExerciseSession`
    FOREIGN KEY (`idExerciseSession`)
    REFERENCES `softwareengineering`.`Exercise` (`idExerciseType`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`ExerciseLink` (
  `idLink` INT NOT NULL,
  `idUser` INT NOT NULL,
  `idExerciseSession` INT NOT NULL,  
  `date` DATE NOT NULL,
  PRIMARY KEY (`idLink`),
  CONSTRAINT `idUserinLink`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `idsessioninLink`
    FOREIGN KEY (`idExerciseSession`)
    REFERENCES `softwareengineering`.`exercisesession` (`idExerciseSession`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Foods` (
  `idFood` INT NOT NULL,
  `foodName` VARCHAR(45) NOT NULL,
  `amountOfCalories` SMALLINT NOT NULL,
  `description` VARCHAR(250) NOT NULL DEFAULT '',
  PRIMARY KEY (`idFood`),
  UNIQUE INDEX `idFood_UNIQUE` (`idFood` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Meal` (
  `idMeal` INT NOT NULL,
  `idFood` INT NOT NULL,
  `quantity` SMALLINT NOT NULL DEFAULT 1,
  `mealCategory` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idMeal`),
  UNIQUE INDEX `idMeal_UNIQUE` (`idMeal` ASC) VISIBLE,
  INDEX `idFood_idx` (`idFood` ASC) VISIBLE,
  CONSTRAINT `idFoodinMeal`
    FOREIGN KEY (`idFood`)
    REFERENCES `softwareengineering`.`Foods` (`idFood`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Diet` (
  `idDiet` INT NOT NULL,
  `idUser` INT NOT NULL,
  `idMeal` INT NOT NULL,  
  `date` DATE NOT NULL,
  PRIMARY KEY (`idDiet`),
  INDEX `idMeal_idx` (`idMeal` ASC) VISIBLE,
  CONSTRAINT `idUserinDiet`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `idMealinDiet`
    FOREIGN KEY (`idMeal`)
    REFERENCES `softwareengineering`.`Meal` (`idMeal`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`WeightTracking` (
  `idWeightTracking` INT NOT NULL,
  `idUser` INT NOT NULL,
  `date` DATE NOT NULL,
  `weight` DECIMAL(10) NOT NULL,
  PRIMARY KEY (`idWeightTracking`),
  UNIQUE INDEX `idWeightTracking_UNIQUE` (`idWeightTracking` ASC) VISIBLE,
  CONSTRAINT `idUserinWeightTracking`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`User` (`idUser`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

insert into softwareengineering.exercise values(0,0,'other');
insert into softwareengineering.exercise values(1,11,'swimming');
insert into softwareengineering.exercise values(2,12,'tennis');
insert into softwareengineering.exercise values(3,13,'running');
insert into softwareengineering.exercise values(4,14,'skipping');
insert into softwareengineering.exercise values(5,15,'rowing');
insert into softwareengineering.exercise values(6,8,'stair machine');
insert into softwareengineering.exercise values(7,10,'stationary cycling');
insert into softwareengineering.exercise values(8,6,'dancing');
insert into softwareengineering.exercise values(9,12,'boxing');
insert into softwareengineering.exercise values(10,14,'skipping');
insert into softwareengineering.exercise values(11,11,'rock climbing');
insert into softwareengineering.exercise values(12,12,'cycling (quick)');
insert into softwareengineering.exercise values(13,9,'golf');
insert into softwareengineering.exercise values(14,10,'hiking');
insert into softwareengineering.exercise values(15,14,'horseriding');
insert into softwareengineering.exercise values(16,6,'walking');
insert into softwareengineering.exercise values(17,12,'ice skating');
insert into softwareengineering.exercise values(18,13,'kayaking');
insert into softwareengineering.exercise values(19,11,'spin cycle');
insert into softwareengineering.exercise values(20,10,'volleyball');
insert into softwareengineering.exercise values(21,8,'weightlifting');

insert into softwareengineering.foods values(0,'apple',95,'');
insert into softwareengineering.foods values(1,'banana',105,'');
insert into softwareengineering.foods values(2,'orange',47,'');
insert into softwareengineering.foods values(3,'strawberry',33,'');
insert into softwareengineering.foods values(4,'grapes',67,'');
insert into softwareengineering.foods values(5,'donut',402,'');
insert into softwareengineering.foods values(6,'beer pint',182,'');
insert into softwareengineering.foods values(7,'wine small glass',87,'');
insert into softwareengineering.foods values(8,'coffee 220ml',16,'');
insert into softwareengineering.foods values(9,'tea 270ml',29,'');
insert into softwareengineering.foods values(10,'orange juice 200ml',88,'');
insert into softwareengineering.foods values(11,'big mac',492,'');
insert into softwareengineering.foods values(12,'kfc fries 100g',294,'');
insert into softwareengineering.foods values(13,'pizza slice',263,'');
insert into softwareengineering.foods values(14,'poppadum 12g',49,'');
insert into softwareengineering.foods values(15,'korma 300g',498,'');
insert into softwareengineering.foods values(16,'brazil nuts 28g',193,'');
insert into softwareengineering.foods values(17,'almonds 28g',171,'');
insert into softwareengineering.foods values(18,'bagel 85g',216,'');
insert into softwareengineering.foods values(19,'hot cross bun 70g',205,'');
insert into softwareengineering.foods values(20,'scone 70g',225,'');
insert into softwareengineering.foods values(21,'pitta bread 25g',147,'');
insert into softwareengineering.foods values(22,'corn flakes 45g',167,'');
insert into softwareengineering.foods values(23,'weetabix 38g',139,'');
insert into softwareengineering.foods values(24,'porridge 45g',166,'');
insert into softwareengineering.foods values(25,'chicken 1 breast',132,'');
insert into softwareengineering.foods values(26,'1 pork sausage',73,'');
insert into softwareengineering.foods values(27,'fillet steak 1oz',54,'');
insert into softwareengineering.foods values(28,'ham 30g',35,'');
insert into softwareengineering.foods values(29,'egg fried rice 200g',250,'');
insert into softwareengineering.foods values(30,'mars bar 65g',294,'');
insert into softwareengineering.foods values(31,'jelly babies 1 sweet',20,'');
insert into softwareengineering.foods values(32,'popcorn 100g',405,'');
insert into softwareengineering.foods values(33,'milky way 26g',177,'');
insert into softwareengineering.foods values(34,'ready salted crisps 28g',132,'');
insert into softwareengineering.foods values(35,'cream egg 39g',180,'');
insert into softwareengineering.foods values(36,'mini eggs 50g',250,'');
insert into softwareengineering.foods values(37,'butter 10g',74,'');
insert into softwareengineering.foods values(38,'medium egg',84,'');
insert into softwareengineering.foods values(39,'whole milk 30ml',20,'');
insert into softwareengineering.foods values(40,'potato wedges 135g',279,'');
insert into softwareengineering.foods values(41,'melon 100g',30,'');
insert into softwareengineering.foods values(42,'garlic bread 84g',94,'');
insert into softwareengineering.foods values(43,'mexican flatbread 185g',263,'');
insert into softwareengineering.foods values(44,'egg mayo sandwich 1 pack',253,'');
insert into softwareengineering.foods values(45,'chicken fajita wrap 1 pack',263,'');
insert into softwareengineering.foods values(46,'ham and cheese panini 223g',557,'');
insert into softwareengineering.foods values(47,'chips 100g',253,'');
insert into softwareengineering.foods values(48,'peas 60g',32,'');
insert into softwareengineering.foods values(49,'carrots 60g',13,'');
insert into softwareengineering.foods values(50,'jacket potato medium',245,'');