CREATE TABLE IF NOT EXISTS `softwareengineering`.`PersonalInfo` (
  `idUser` INT NOT NULL,
  `forename` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` NVARCHAR(20) NOT NULL,
  `DOB` DATE NOT NULL,
  `height` DECIMAL(10) NOT NULL,
  `gender` VARCHAR(1) NOT NULL,
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);  
CREATE TABLE IF NOT EXISTS `softwareengineering`.`GoalWeight` (
  `idGoalWeight` INT NOT NULL,
  `weightGoal` DECIMAL(10) NOT NULL,
  `dateSet` DATE NULL,
  `targetDate` DATE NOT NULL,
  PRIMARY KEY (`idGoalWeight`),
  UNIQUE INDEX `idGoalWeight_UNIQUE` (`idGoalWeight` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`GoalLink` (
  `idUser` INT NOT NULL,
  `idGoalWeight` INT NOT NULL,  
  PRIMARY KEY (`idGoalWeight`,`idUser`),
  CONSTRAINT `idUserinGoalLink`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idGoalWeightinGoalLink`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`goalweight` (`idGoalWeight`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Exercise` (
  `idExerciseType` INT NOT NULL,
  `calsPerMinute` DECIMAL(10) NOT NULL,
  `exerciseName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idExerciseType`),
  UNIQUE INDEX `idExerciseType_UNIQUE` (`idExerciseType` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`ExerciseSession` (
  `idExerciseSession` INT NOT NULL,
  `durationMinutes` DECIMAL(10) NULL,
  `idExerciseType` INT NOT NULL,
  `caloriesBurned` INT NULL,
  PRIMARY KEY (`idExerciseSession`),
  UNIQUE INDEX `idExcerciseSession_UNIQUE` (`idExerciseSession` ASC) VISIBLE,
  CONSTRAINT `idExerciseTypeinExerciseSession`
    FOREIGN KEY (`idExerciseSession`)
    REFERENCES `softwareengineering`.`Exercise` (`idExerciseType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`ExerciseLink` (
  `idLink` INT NOT NULL,
  `idUser` INT NOT NULL,
  `idExerciseSession` INT NOT NULL,  
  `date` DATE NOT NULL,
  PRIMARY KEY (`idLink`),
  CONSTRAINT `idUserinLink`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idsessioninLink`
    FOREIGN KEY (`idExerciseSession`)
    REFERENCES `softwareengineering`.`exercisesession` (`idExerciseSession`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Foods` (
  `idFood` INT NOT NULL,
  `foodName` VARCHAR(45) NOT NULL,
  `amountOfCalories` INT NOT NULL,
  `portionSize` DECIMAL(10) NOT NULL DEFAULT 1,
  PRIMARY KEY (`idFood`),
  UNIQUE INDEX `idFood_UNIQUE` (`idFood` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Meal` (
  `idMeal` INT NOT NULL,
  `idFood` INT NOT NULL,
  `quantity` DECIMAL(10) NOT NULL DEFAULT 1,
  `mealCategory` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idMeal`),
  UNIQUE INDEX `idMeal_UNIQUE` (`idMeal` ASC) VISIBLE,
  INDEX `idFood_idx` (`idFood` ASC) VISIBLE,
  CONSTRAINT `idFoodinMeal`
    FOREIGN KEY (`idFood`)
    REFERENCES `softwareengineering`.`Foods` (`idFood`)
    ON DELETE NO ACTION
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
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMealinDiet`
    FOREIGN KEY (`idMeal`)
    REFERENCES `softwareengineering`.`Meal` (`idMeal`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`WeightTracking` (
  `idUser` INT NOT NULL,
  `date` DATE NOT NULL,
  `weight` DECIMAL(10) NOT NULL,
  PRIMARY KEY (`idUser`, `date`),
  CONSTRAINT `idUserinWeightTracking`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
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

insert into softwareengineering.foods values(0,'apple',95,182);
insert into softwareengineering.foods values(1,'banana',105,118);
insert into softwareengineering.foods values(2,'orange',47,100);
insert into softwareengineering.foods values(3,'strawberry',33,100);
insert into softwareengineering.foods values(4,'grapes',67,100);
insert into softwareengineering.foods values(5,'donut',402,100);
insert into softwareengineering.foods values(6,'beer pint',182,568);
insert into softwareengineering.foods values(7,'wine small glass',87,120);
insert into softwareengineering.foods values(8,'coffee 220ml',16,220);
insert into softwareengineering.foods values(9,'tea 270ml',29,270);
insert into softwareengineering.foods values(10,'orange juice 200ml',88,200);
insert into softwareengineering.foods values(11,'big mac',492,215);
insert into softwareengineering.foods values(12,'kfc fries 100g',294,100);
insert into softwareengineering.foods values(13,'pizza slice',263,135);
insert into softwareengineering.foods values(14,'poppadum 12g',49,12);
insert into softwareengineering.foods values(15,'korma 300g',498,300);
insert into softwareengineering.foods values(16,'brazil nuts 28g',193,28);
insert into softwareengineering.foods values(17,'almonds 28g',171,28);
insert into softwareengineering.foods values(18,'bagel 85g',216,85);
insert into softwareengineering.foods values(19,'hot cross bun 70g',205,70);
insert into softwareengineering.foods values(20,'scone 70g',225,70);
insert into softwareengineering.foods values(21,'pitta bread 25g',147,25);
insert into softwareengineering.foods values(22,'corn flakes 45g',167,45);
insert into softwareengineering.foods values(23,'weetabix 38g',139,38);
insert into softwareengineering.foods values(24,'porridge 45g',166,45);
insert into softwareengineering.foods values(25,'chicken 1 breast',132,100);
insert into softwareengineering.foods values(26,'1 pork sausage',73,24);
insert into softwareengineering.foods values(27,'fillet steak 1oz',54,28);
insert into softwareengineering.foods values(28,'ham 30g',35,30);
insert into softwareengineering.foods values(29,'egg fried rice 200g',250,200);
insert into softwareengineering.foods values(30,'mars bar 65g',294,65);
insert into softwareengineering.foods values(31,'jelly babies 1 sweet',20,6);
insert into softwareengineering.foods values(32,'popcorn 100g',405,100);
insert into softwareengineering.foods values(33,'milky way 26g',177,26);
insert into softwareengineering.foods values(34,'ready salted crisps 28g',132,28);
insert into softwareengineering.foods values(35,'cream egg 39g',180,39);
insert into softwareengineering.foods values(36,'mini eggs 50g',250,50);
insert into softwareengineering.foods values(37,'butter 10g',74,10);
insert into softwareengineering.foods values(38,'medium egg',84,57);
insert into softwareengineering.foods values(39,'whole milk 30ml',20,30);
insert into softwareengineering.foods values(40,'potato wedges 135g',279,135);
insert into softwareengineering.foods values(41,'melon 100g',30,100);
insert into softwareengineering.foods values(42,'garlic bread 84g',94,84);
insert into softwareengineering.foods values(43,'mexican flatbread 185g',263,185);
insert into softwareengineering.foods values(44,'egg mayo sandwich 1 pack',253,1);
insert into softwareengineering.foods values(45,'chicken fajita wrap 1 pack',263,185);
insert into softwareengineering.foods values(46,'ham and cheese panini 223g',557,223);
insert into softwareengineering.foods values(47,'chips 100g',253,100);
insert into softwareengineering.foods values(48,'peas 60g',32,60);
insert into softwareengineering.foods values(49,'carrots 60g',13,60);
insert into softwareengineering.foods values(50,'jacket potato medium',245,180);