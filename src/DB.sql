-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.17

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
                         `object_Barcode` bigint(13) NOT NULL,
                         `Author` varchar(45) DEFAULT NULL,
                         `ISBN` varchar(45) DEFAULT NULL,
                         `Subject` varchar(45) DEFAULT NULL,
                         PRIMARY KEY (`object_Barcode`),
                         CONSTRAINT `fk_books_object1` FOREIGN KEY (`object_Barcode`) REFERENCES `object` (`Barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (12345678933,'Andra författarnen','222211222','Uppföljare'),(1112223331234,'Karl \"The man\" Karlsson','321321321','Drama'),(1234567891234,'Han som Författar ','789123456Skrivsson','Drama'),(1234567892222,'Andra författaren','222222222','Uppföljare'),(1234567893333,'Tredje Författaren','333333333','Trilogier'),(1234567894444,'Quattro Författare','444444444','Documentary'),(1888444661234,'IT-Mannen','154154154','IT'),(18887654657897,'Javamannen','345543345','Programmering');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
                             `PersonalNo` bigint(10) NOT NULL,
                             `Name` varchar(45) NOT NULL,
                             `Role` tinyint(4) NOT NULL,
                             `library_libraryID` int(11) NOT NULL,
                             PRIMARY KEY (`PersonalNo`,`library_libraryID`),
                             KEY `fk_employees_library1_idx` (`library_libraryID`),
                             CONSTRAINT `fk_employees_library1` FOREIGN KEY (`library_libraryID`) REFERENCES `library` (`libraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (4503125978,'Netbeans Addedsson',6,1),(6705201346,'Arbetare Bibliotekariesson',2,1),(8404251144,'Anders Testsson',1,1),(8705201346,'Anställd Chefsson',3,1),(9308077849,'Mimmi Smedson',7,1);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `journals`
--

DROP TABLE IF EXISTS `journals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journals` (
                            `object_Barcode` bigint(13) NOT NULL,
                            `ISSN` varchar(45) DEFAULT NULL,
                            `Date` date DEFAULT NULL,
                            PRIMARY KEY (`object_Barcode`),
                            CONSTRAINT `fk_journals_object1` FOREIGN KEY (`object_Barcode`) REFERENCES `object` (`Barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `journals`
--

LOCK TABLES `journals` WRITE;
/*!40000 ALTER TABLE `journals` DISABLE KEYS */;
INSERT INTO `journals` VALUES (3123456781211,'654123987','2020-01-01'),(3234567891234,'123123','2020-05-10'),(3234567891277,'123123444','2020-02-01');
/*!40000 ALTER TABLE `journals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `library` (
                           `libraryID` int(11) NOT NULL,
                           `libraryName` varchar(45) DEFAULT NULL,
                           PRIMARY KEY (`libraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
INSERT INTO `library` VALUES (1,'Det bästa Biblioteket');
/*!40000 ALTER TABLE `library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loan` (
                        `loanID` int(11) NOT NULL AUTO_INCREMENT,
                        `regUser_personalNo` bigint(10) NOT NULL,
                        `object_Barcode` bigint(13) NOT NULL,
                        `LoanDate` date NOT NULL,
                        `ReturnDate` date DEFAULT NULL,
                        PRIMARY KEY (`loanID`),
                        KEY `fk_regUser_has_object_object1_idx` (`object_Barcode`),
                        KEY `fk_regUser_has_object_regUser1_idx` (`regUser_personalNo`),
                        CONSTRAINT `fk_regUser_has_object_object1` FOREIGN KEY (`object_Barcode`) REFERENCES `object` (`Barcode`),
                        CONSTRAINT `fk_regUser_has_object_regUser1` FOREIGN KEY (`regUser_personalNo`) REFERENCES `reguser` (`personalNo`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
                          `object_Barcode` bigint(13) NOT NULL,
                          `Director` varchar(45) DEFAULT NULL,
                          `Genre` varchar(45) DEFAULT NULL,
                          `AgeRestriction` tinyint(4) DEFAULT NULL,
                          `Actor` varchar(45) DEFAULT NULL,
                          `Country` varchar(45) DEFAULT NULL,
                          PRIMARY KEY (`object_Barcode`),
                          CONSTRAINT `fk_movies_object1` FOREIGN KEY (`object_Barcode`) REFERENCES `object` (`Barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (2134567891111,'Gamle Francis','Gangster',15,'Marlon Brando','USA'),(2234567891234,'Quentin Quarantino','Thriller',15,'Brad Pitt','USA'),(24455667891234,'Gamle Francis','Gangster',15,'Marlon Brando','USA');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object`
--

DROP TABLE IF EXISTS `object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `object` (
                          `Barcode` bigint(13) NOT NULL,
                          `Title` varchar(45) NOT NULL,
                          `Loanable` tinyint(4) NOT NULL,
                          `Type` int(11) NOT NULL,
                          `Location` varchar(10) NOT NULL,
                          `library_libraryID` int(11) NOT NULL,
                          PRIMARY KEY (`Barcode`,`library_libraryID`),
                          KEY `fk_object_library1_idx` (`library_libraryID`),
                          CONSTRAINT `fk_object_library1` FOREIGN KEY (`library_libraryID`) REFERENCES `library` (`libraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object`
--

LOCK TABLES `object` WRITE;
/*!40000 ALTER TABLE `object` DISABLE KEYS */;
INSERT INTO `object` VALUES (12345678933,'Denboken 2',1,2,'R2H4',1),(1112223331234,'Boken om systemet',1,2,'R4H1',1),(1234567891234,'Den bästa boken - Ibland',1,1,'R4H4',1),(1234567892222,'Den andra boken 2',1,2,'R2H5',1),(1234567893333,'Den tredje boken',0,4,'R2H6',1),(1234567894444,'Den fjärde boken - yes',1,2,'R6H4',1),(1888444661234,'Boken om IT',1,2,'R7H2',1),(2134567891111,'Gudfadern',1,3,'R1H1',1),(2234567891234,'Den bästa filmen - ibland',1,3,'R3H9',1),(3123456781211,'Nat geo',0,4,'R3H5',1),(3234567891234,'Den bästa tidningen - ibland',0,4,'R5H5',1),(3234567891277,'Den bästa tidningen - annars',0,4,'R5H7',1),(18887654657897,'Den stora boken om java',1,2,'R8H3',1),(24455667891234,'Gudfadern 2',1,3,'R1H5',1);
/*!40000 ALTER TABLE `object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reguser`
--

DROP TABLE IF EXISTS `reguser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reguser` (
                           `personalNo` bigint(12) NOT NULL,
                           `password` varchar(45) DEFAULT NULL,
                           `usertype` tinyint(4) DEFAULT NULL,
                           `Debt` int(11) NOT NULL,
                           `library_libraryID` int(11) NOT NULL,
                           PRIMARY KEY (`personalNo`,`library_libraryID`),
                           KEY `fk_regUser_library1_idx` (`library_libraryID`),
                           CONSTRAINT `fk_regUser_library1` FOREIGN KEY (`library_libraryID`) REFERENCES `library` (`libraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reguser`
--

LOCK TABLES `reguser` WRITE;
/*!40000 ALTER TABLE `reguser` DISABLE KEYS */;
INSERT INTO `reguser` VALUES (101010101,'chef',5,0,1),(1231231231,'+password+',1,0,1),(8404043215,'lol',1,0,1),(8404251234,'Anders1337',1,0,1),(8501011212,'password',1,0,1),(8501224979,'TestPW',1,0,1),(8802021144,'lol',1,0,1),(8811221111,'123',1,0,1),(9308091234,'mimmi',1,0,1);
/*!40000 ALTER TABLE `reguser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
                               `reservationID` int(11) NOT NULL AUTO_INCREMENT,
                               `object_Barcode` bigint(13) NOT NULL,
                               `regUser_personalNo` bigint(10) NOT NULL,
                               PRIMARY KEY (`reservationID`),
                               KEY `fk_object_has_regUser_regUser1_idx` (`regUser_personalNo`),
                               KEY `fk_object_has_regUser_object1_idx` (`object_Barcode`),
                               CONSTRAINT `fk_object_has_regUser_object1` FOREIGN KEY (`object_Barcode`) REFERENCES `object` (`Barcode`),
                               CONSTRAINT `fk_object_has_regUser_regUser1` FOREIGN KEY (`regUser_personalNo`) REFERENCES `reguser` (`personalNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'java2'
--

--
-- Dumping routines for database 'java2'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-22 16:27:27
