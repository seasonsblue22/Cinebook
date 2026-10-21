-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cinebook
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `booking_seats`
--

DROP TABLE IF EXISTS `booking_seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_seats` (
  `booking_id` int NOT NULL,
  `seat_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`booking_id`,`seat_id`),
  KEY `seat_id` (`seat_id`),
  CONSTRAINT `booking_seats_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `booking_seats_ibfk_2` FOREIGN KEY (`seat_id`) REFERENCES `seats` (`seat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_seats`
--

LOCK TABLES `booking_seats` WRITE;
/*!40000 ALTER TABLE `booking_seats` DISABLE KEYS */;
INSERT INTO `booking_seats` VALUES (1,13,150.00),(1,28,150.00);
/*!40000 ALTER TABLE `booking_seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `booking_id` int NOT NULL AUTO_INCREMENT,
  `show_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `coupon_code` varchar(30) DEFAULT NULL,
  `status` enum('CONFIRMED','CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
  `booked_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`booking_id`),
  KEY `show_id` (`show_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`show_id`) REFERENCES `shows` (`show_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,1,270.00,'CINE10','CANCELLED','2026-10-21 05:31:01');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `seat_id` int NOT NULL AUTO_INCREMENT,
  `show_id` int NOT NULL,
  `row_label` char(1) NOT NULL,
  `seat_no` int NOT NULL,
  `seat_type` enum('REGULAR','PREMIUM','VIP') NOT NULL,
  `is_booked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`seat_id`),
  UNIQUE KEY `show_id` (`show_id`,`row_label`,`seat_no`),
  CONSTRAINT `seats_ibfk_1` FOREIGN KEY (`show_id`) REFERENCES `shows` (`show_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
INSERT INTO `seats` VALUES (1,1,'E',1,'VIP',0),(2,2,'E',1,'VIP',0),(3,3,'E',1,'VIP',0),(4,1,'D',1,'PREMIUM',0),(5,2,'D',1,'PREMIUM',0),(6,3,'D',1,'PREMIUM',0),(7,1,'C',1,'PREMIUM',0),(8,2,'C',1,'PREMIUM',0),(9,3,'C',1,'PREMIUM',0),(10,1,'B',1,'REGULAR',0),(11,2,'B',1,'REGULAR',0),(12,3,'B',1,'REGULAR',0),(13,1,'A',1,'REGULAR',0),(14,2,'A',1,'REGULAR',0),(15,3,'A',1,'REGULAR',0),(16,1,'E',2,'VIP',0),(17,2,'E',2,'VIP',0),(18,3,'E',2,'VIP',0),(19,1,'D',2,'PREMIUM',0),(20,2,'D',2,'PREMIUM',0),(21,3,'D',2,'PREMIUM',0),(22,1,'C',2,'PREMIUM',0),(23,2,'C',2,'PREMIUM',0),(24,3,'C',2,'PREMIUM',0),(25,1,'B',2,'REGULAR',0),(26,2,'B',2,'REGULAR',0),(27,3,'B',2,'REGULAR',0),(28,1,'A',2,'REGULAR',0),(29,2,'A',2,'REGULAR',0),(30,3,'A',2,'REGULAR',0),(31,1,'E',3,'VIP',0),(32,2,'E',3,'VIP',0),(33,3,'E',3,'VIP',0),(34,1,'D',3,'PREMIUM',0),(35,2,'D',3,'PREMIUM',0),(36,3,'D',3,'PREMIUM',0),(37,1,'C',3,'PREMIUM',0),(38,2,'C',3,'PREMIUM',0),(39,3,'C',3,'PREMIUM',0),(40,1,'B',3,'REGULAR',0),(41,2,'B',3,'REGULAR',0),(42,3,'B',3,'REGULAR',0),(43,1,'A',3,'REGULAR',0),(44,2,'A',3,'REGULAR',0),(45,3,'A',3,'REGULAR',0),(46,1,'E',4,'VIP',0),(47,2,'E',4,'VIP',0),(48,3,'E',4,'VIP',0),(49,1,'D',4,'PREMIUM',0),(50,2,'D',4,'PREMIUM',0),(51,3,'D',4,'PREMIUM',0),(52,1,'C',4,'PREMIUM',0),(53,2,'C',4,'PREMIUM',0),(54,3,'C',4,'PREMIUM',0),(55,1,'B',4,'REGULAR',0),(56,2,'B',4,'REGULAR',0),(57,3,'B',4,'REGULAR',0),(58,1,'A',4,'REGULAR',0),(59,2,'A',4,'REGULAR',0),(60,3,'A',4,'REGULAR',0),(61,1,'E',5,'VIP',0),(62,2,'E',5,'VIP',0),(63,3,'E',5,'VIP',0),(64,1,'D',5,'PREMIUM',0),(65,2,'D',5,'PREMIUM',0),(66,3,'D',5,'PREMIUM',0),(67,1,'C',5,'PREMIUM',0),(68,2,'C',5,'PREMIUM',0),(69,3,'C',5,'PREMIUM',0),(70,1,'B',5,'REGULAR',0),(71,2,'B',5,'REGULAR',0),(72,3,'B',5,'REGULAR',0),(73,1,'A',5,'REGULAR',0),(74,2,'A',5,'REGULAR',0),(75,3,'A',5,'REGULAR',0),(76,1,'E',6,'VIP',0),(77,2,'E',6,'VIP',0),(78,3,'E',6,'VIP',0),(79,1,'D',6,'PREMIUM',0),(80,2,'D',6,'PREMIUM',0),(81,3,'D',6,'PREMIUM',0),(82,1,'C',6,'PREMIUM',0),(83,2,'C',6,'PREMIUM',0),(84,3,'C',6,'PREMIUM',0),(85,1,'B',6,'REGULAR',0),(86,2,'B',6,'REGULAR',0),(87,3,'B',6,'REGULAR',0),(88,1,'A',6,'REGULAR',0),(89,2,'A',6,'REGULAR',0),(90,3,'A',6,'REGULAR',0),(91,1,'E',7,'VIP',0),(92,2,'E',7,'VIP',0),(93,3,'E',7,'VIP',0),(94,1,'D',7,'PREMIUM',0),(95,2,'D',7,'PREMIUM',0),(96,3,'D',7,'PREMIUM',0),(97,1,'C',7,'PREMIUM',0),(98,2,'C',7,'PREMIUM',0),(99,3,'C',7,'PREMIUM',0),(100,1,'B',7,'REGULAR',0),(101,2,'B',7,'REGULAR',0),(102,3,'B',7,'REGULAR',0),(103,1,'A',7,'REGULAR',0),(104,2,'A',7,'REGULAR',0),(105,3,'A',7,'REGULAR',0),(106,1,'E',8,'VIP',0),(107,2,'E',8,'VIP',0),(108,3,'E',8,'VIP',0),(109,1,'D',8,'PREMIUM',0),(110,2,'D',8,'PREMIUM',0),(111,3,'D',8,'PREMIUM',0),(112,1,'C',8,'PREMIUM',0),(113,2,'C',8,'PREMIUM',0),(114,3,'C',8,'PREMIUM',0),(115,1,'B',8,'REGULAR',0),(116,2,'B',8,'REGULAR',0),(117,3,'B',8,'REGULAR',0),(118,1,'A',8,'REGULAR',0),(119,2,'A',8,'REGULAR',0),(120,3,'A',8,'REGULAR',0);
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shows`
--

DROP TABLE IF EXISTS `shows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shows` (
  `show_id` int NOT NULL AUTO_INCREMENT,
  `movie_name` varchar(100) NOT NULL,
  `show_time` varchar(30) NOT NULL,
  PRIMARY KEY (`show_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shows`
--

LOCK TABLES `shows` WRITE;
/*!40000 ALTER TABLE `shows` DISABLE KEYS */;
INSERT INTO `shows` VALUES (1,'Interstellar','10:00 AM'),(2,'Inception','2:00 PM'),(3,'The Dark Knight','7:30 PM');
/*!40000 ALTER TABLE `shows` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-21 12:42:01
