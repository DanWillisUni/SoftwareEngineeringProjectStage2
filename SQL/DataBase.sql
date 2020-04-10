CREATE DATABASE  IF NOT EXISTS `softwareengineering` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `softwareengineering`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: softwareengineering
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `diet`
--

DROP TABLE IF EXISTS `diet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diet` (
  `idDiet` int NOT NULL,
  `idUser` int NOT NULL,
  `idMeal` int NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`idDiet`),
  KEY `idMeal_idx` (`idMeal`),
  KEY `idUserinDiet` (`idUser`),
  CONSTRAINT `idMealinDiet` FOREIGN KEY (`idMeal`) REFERENCES `meal` (`idMeal`),
  CONSTRAINT `idUserinDiet` FOREIGN KEY (`idUser`) REFERENCES `personalinfo` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diet`
--

LOCK TABLES `diet` WRITE;
/*!40000 ALTER TABLE `diet` DISABLE KEYS */;
INSERT INTO `diet` VALUES (0,0,0,'2020-03-18'),(1,0,1,'2020-03-18'),(2,0,2,'2020-03-18'),(3,0,3,'2020-03-18'),(4,0,4,'2020-03-18'),(5,0,5,'2020-03-18'),(6,0,6,'2020-03-18');
/*!40000 ALTER TABLE `diet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercise`
--

DROP TABLE IF EXISTS `exercise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise` (
  `idExerciseType` int NOT NULL,
  `calsPerMinute` decimal(10,0) NOT NULL,
  `exerciseName` varchar(45) NOT NULL,
  PRIMARY KEY (`idExerciseType`),
  UNIQUE KEY `idExerciseType_UNIQUE` (`idExerciseType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise`
--

LOCK TABLES `exercise` WRITE;
/*!40000 ALTER TABLE `exercise` DISABLE KEYS */;
INSERT INTO `exercise` VALUES (0,0,'other'),(1,11,'swimming'),(2,12,'tennis'),(3,13,'running'),(4,14,'skipping'),(5,15,'rowing'),(6,8,'stair machine'),(7,10,'stationary cycling'),(8,6,'dancing'),(9,12,'boxing'),(10,14,'skipping'),(11,11,'rock climbing'),(12,12,'cycling (quick)'),(13,9,'golf'),(14,10,'hiking'),(15,14,'horseriding'),(16,6,'walking'),(17,12,'ice skating'),(18,13,'kayaking'),(19,11,'spin cycle'),(20,10,'volleyball'),(21,8,'weightlifting');
/*!40000 ALTER TABLE `exercise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exerciselink`
--

DROP TABLE IF EXISTS `exerciselink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exerciselink` (
  `idLink` int NOT NULL,
  `idUser` int NOT NULL,
  `idExerciseSession` int NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`idLink`),
  KEY `idUserinLink` (`idUser`),
  KEY `idsessioninLink` (`idExerciseSession`),
  CONSTRAINT `idsessioninLink` FOREIGN KEY (`idExerciseSession`) REFERENCES `exercisesession` (`idExerciseSession`),
  CONSTRAINT `idUserinLink` FOREIGN KEY (`idUser`) REFERENCES `personalinfo` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exerciselink`
--

LOCK TABLES `exerciselink` WRITE;
/*!40000 ALTER TABLE `exerciselink` DISABLE KEYS */;
INSERT INTO `exerciselink` VALUES (0,0,0,'2020-03-18'),(1,0,1,'2020-03-18'),(2,0,2,'2020-03-18');
/*!40000 ALTER TABLE `exerciselink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercisesession`
--

DROP TABLE IF EXISTS `exercisesession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercisesession` (
  `idExerciseSession` int NOT NULL,
  `durationMinutes` decimal(10,0) DEFAULT NULL,
  `idExerciseType` int NOT NULL,
  `caloriesBurned` int DEFAULT NULL,
  PRIMARY KEY (`idExerciseSession`),
  UNIQUE KEY `idExcerciseSession_UNIQUE` (`idExerciseSession`),
  CONSTRAINT `idExerciseTypeinExerciseSession` FOREIGN KEY (`idExerciseSession`) REFERENCES `exercise` (`idExerciseType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercisesession`
--

LOCK TABLES `exercisesession` WRITE;
/*!40000 ALTER TABLE `exercisesession` DISABLE KEYS */;
INSERT INTO `exercisesession` VALUES (0,20,9,200),(1,30,13,270),(2,60,0,500);
/*!40000 ALTER TABLE `exercisesession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `foods`
--

DROP TABLE IF EXISTS `foods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `foods` (
  `idFood` int NOT NULL,
  `foodName` varchar(45) NOT NULL,
  `amountOfCalories` int NOT NULL,
  `portionSize` decimal(10,0) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idFood`),
  UNIQUE KEY `idFood_UNIQUE` (`idFood`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `foods`
--

LOCK TABLES `foods` WRITE;
/*!40000 ALTER TABLE `foods` DISABLE KEYS */;
INSERT INTO `foods` VALUES (0,'apple',95,182),(1,'banana',105,118),(2,'orange',47,100),(3,'strawberry',33,100),(4,'grapes',67,100),(5,'donut',402,100),(6,'beer pint',182,568),(7,'wine small glass',87,120),(8,'coffee 220ml',16,220),(9,'tea 270ml',29,270),(10,'orange juice 200ml',88,200),(11,'big mac',492,215),(12,'kfc fries 100g',294,100),(13,'pizza slice',263,135),(14,'poppadum 12g',49,12),(15,'korma 300g',498,300),(16,'brazil nuts 28g',193,28),(17,'almonds 28g',171,28),(18,'bagel 85g',216,85),(19,'hot cross bun 70g',205,70),(20,'scone 70g',225,70),(21,'pitta bread 25g',147,25),(22,'corn flakes 45g',167,45),(23,'weetabix 38g',139,38),(24,'porridge 45g',166,45),(25,'chicken 1 breast',132,100),(26,'1 pork sausage',73,24),(27,'fillet steak 1oz',54,28),(28,'ham 30g',35,30),(29,'egg fried rice 200g',250,200),(30,'mars bar 65g',294,65),(31,'jelly babies 1 sweet',20,6),(32,'popcorn 100g',405,100),(33,'milky way 26g',177,26),(34,'ready salted crisps 28g',132,28),(35,'cream egg 39g',180,39),(36,'mini eggs 50g',250,50),(37,'butter 10g',74,10),(38,'medium egg',84,57),(39,'whole milk 30ml',20,30),(40,'potato wedges 135g',279,135),(41,'melon 100g',30,100),(42,'garlic bread 84g',94,84),(43,'mexican flatbread 185g',263,185),(44,'egg mayo sandwich 1 pack',253,1),(45,'chicken fajita wrap 1 pack',263,185),(46,'ham and cheese panini 223g',557,223),(47,'chips 100g',253,100),(48,'peas 60g',32,60),(49,'carrots 60g',13,60),(50,'jacket potato medium',245,180);
/*!40000 ALTER TABLE `foods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goallink`
--

DROP TABLE IF EXISTS `goallink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goallink` (
  `idUser` int NOT NULL,
  `idGoalWeight` int NOT NULL,
  PRIMARY KEY (`idGoalWeight`,`idUser`),
  KEY `idGoalWeightinGoalLink` (`idUser`),
  CONSTRAINT `idGoalWeightinGoalLink` FOREIGN KEY (`idUser`) REFERENCES `goalweight` (`idGoalWeight`),
  CONSTRAINT `idUserinGoalLink` FOREIGN KEY (`idUser`) REFERENCES `personalinfo` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goallink`
--

LOCK TABLES `goallink` WRITE;
/*!40000 ALTER TABLE `goallink` DISABLE KEYS */;
INSERT INTO `goallink` VALUES (0,1),(0,2);
/*!40000 ALTER TABLE `goallink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goalweight`
--

DROP TABLE IF EXISTS `goalweight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goalweight` (
  `idGoalWeight` int NOT NULL,
  `weightGoal` decimal(10,0) NOT NULL,
  `dateSet` date DEFAULT NULL,
  `targetDate` date NOT NULL,
  PRIMARY KEY (`idGoalWeight`),
  UNIQUE KEY `idGoalWeight_UNIQUE` (`idGoalWeight`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goalweight`
--

LOCK TABLES `goalweight` WRITE;
/*!40000 ALTER TABLE `goalweight` DISABLE KEYS */;
INSERT INTO `goalweight` VALUES (0,100,'2020-03-18','2020-03-24'),(1,80,'2020-03-18','2020-03-31'),(2,85,'2020-03-18','2020-03-25');
/*!40000 ALTER TABLE `goalweight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meal`
--

DROP TABLE IF EXISTS `meal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal` (
  `idMeal` int NOT NULL,
  `idFood` int NOT NULL,
  `quantity` decimal(10,0) NOT NULL DEFAULT '1',
  `mealCategory` varchar(45) NOT NULL,
  PRIMARY KEY (`idMeal`),
  UNIQUE KEY `idMeal_UNIQUE` (`idMeal`),
  KEY `idFood_idx` (`idFood`),
  CONSTRAINT `idFoodinMeal` FOREIGN KEY (`idFood`) REFERENCES `foods` (`idFood`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meal`
--

LOCK TABLES `meal` WRITE;
/*!40000 ALTER TABLE `meal` DISABLE KEYS */;
INSERT INTO `meal` VALUES (0,1,2,'Snack'),(1,11,2,'Lunch'),(2,26,3,'Breakfast'),(3,17,2,'Snack'),(4,29,3,'Dinner'),(5,46,2,'Lunch'),(6,30,1,'Snack');
/*!40000 ALTER TABLE `meal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personalinfo`
--

DROP TABLE IF EXISTS `personalinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personalinfo` (
  `idUser` int NOT NULL,
  `forename` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(60) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DOB` date NOT NULL,
  `height` decimal(10,0) NOT NULL,
  `gender` varchar(1) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personalinfo`
--

LOCK TABLES `personalinfo` WRITE;
/*!40000 ALTER TABLE `personalinfo` DISABLE KEYS */;
INSERT INTO `personalinfo` VALUES (0,'Dan','Willis','danwillis@gmail.com','DaNWIllis1','pass','2000-03-16',200,'M');
/*!40000 ALTER TABLE `personalinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weighttracking`
--

DROP TABLE IF EXISTS `weighttracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weighttracking` (
  `idUser` int NOT NULL,
  `date` date NOT NULL,
  `weight` decimal(10,0) NOT NULL,
  PRIMARY KEY (`idUser`,`date`),
  CONSTRAINT `idUserinWeightTracking` FOREIGN KEY (`idUser`) REFERENCES `personalinfo` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weighttracking`
--

LOCK TABLES `weighttracking` WRITE;
/*!40000 ALTER TABLE `weighttracking` DISABLE KEYS */;
INSERT INTO `weighttracking` VALUES (0,'2020-03-18',90);
/*!40000 ALTER TABLE `weighttracking` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-18 15:44:23
