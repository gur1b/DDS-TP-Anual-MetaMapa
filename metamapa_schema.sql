-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: core
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Current Database: `core`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `core` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `core`;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Violencia'),(2,'Robo'),(3,'Estafa'),(4,'Accidente');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coleccion`
--

DROP TABLE IF EXISTS `coleccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coleccion` (
  `id_coleccion` int NOT NULL AUTO_INCREMENT,
  `algoritmoConsenso` varchar(255) DEFAULT NULL,
  `descripcionColeccion` varchar(255) DEFAULT NULL,
  `identificadorHandle` varchar(255) DEFAULT NULL,
  `modoDeNavegacion` int DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_coleccion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coleccion`
--

LOCK TABLES `coleccion` WRITE;
/*!40000 ALTER TABLE `coleccion` DISABLE KEYS */;
INSERT INTO `coleccion` VALUES (1,NULL,'Hechos violentos ocurridos en horas de noche y madrugada',NULL,1,'Violencia Nocturna'),(2,NULL,'Todos los robos',NULL,1,'Robos'),(3,NULL,'Estafas llevadas a cabo durante el año 2025',NULL,1,'Estafas del año 2025');
/*!40000 ALTER TABLE `coleccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coleccion_criterio`
--

DROP TABLE IF EXISTS `coleccion_criterio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coleccion_criterio` (
  `coleccion_id` int NOT NULL,
  `criterio_id` int NOT NULL,
  KEY `FKov8fdgj0awpwm3ummnd5odan9` (`criterio_id`),
  KEY `FKalewesawlb1pckvv9usyqlj6k` (`coleccion_id`),
  CONSTRAINT `FKalewesawlb1pckvv9usyqlj6k` FOREIGN KEY (`coleccion_id`) REFERENCES `coleccion` (`id_coleccion`),
  CONSTRAINT `FKov8fdgj0awpwm3ummnd5odan9` FOREIGN KEY (`criterio_id`) REFERENCES `criterio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coleccion_criterio`
--

LOCK TABLES `coleccion_criterio` WRITE;
/*!40000 ALTER TABLE `coleccion_criterio` DISABLE KEYS */;
INSERT INTO `coleccion_criterio` VALUES (1,1),(1,2),(2,3),(3,4),(3,5);
/*!40000 ALTER TABLE `coleccion_criterio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coleccion_fuente`
--

DROP TABLE IF EXISTS `coleccion_fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coleccion_fuente` (
  `id_coleccion` int NOT NULL,
  `id_fuente` int NOT NULL,
  KEY `FKir84m9g4x09qkwwcd5esdpjbm` (`id_fuente`),
  KEY `FKm6wavjk4jqygfrbfgdoewv64h` (`id_coleccion`),
  CONSTRAINT `FKir84m9g4x09qkwwcd5esdpjbm` FOREIGN KEY (`id_fuente`) REFERENCES `fuente` (`id_fuente`),
  CONSTRAINT `FKm6wavjk4jqygfrbfgdoewv64h` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coleccion_fuente`
--

LOCK TABLES `coleccion_fuente` WRITE;
/*!40000 ALTER TABLE `coleccion_fuente` DISABLE KEYS */;
INSERT INTO `coleccion_fuente` VALUES (1,2),(1,3),(2,1),(2,2),(2,3),(2,4),(3,1),(3,2),(3,3),(3,4);
/*!40000 ALTER TABLE `coleccion_fuente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coleccion_hecho`
--

DROP TABLE IF EXISTS `coleccion_hecho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coleccion_hecho` (
  `id_coleccion` int NOT NULL,
  `id_hecho` int NOT NULL,
  KEY `FKe1vgrc4nsa5pkin4ddu2e6k02` (`id_hecho`),
  KEY `FKlbww0ao3fcbb1bxgae8besm95` (`id_coleccion`),
  CONSTRAINT `FKe1vgrc4nsa5pkin4ddu2e6k02` FOREIGN KEY (`id_hecho`) REFERENCES `hecho` (`id_hecho`),
  CONSTRAINT `FKlbww0ao3fcbb1bxgae8besm95` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coleccion_hecho`
--

LOCK TABLES `coleccion_hecho` WRITE;
/*!40000 ALTER TABLE `coleccion_hecho` DISABLE KEYS */;
INSERT INTO `coleccion_hecho` VALUES (1,2),(1,3),(1,10),(1,11),(1,14),(1,17),(1,20),(1,27),(1,31),(1,38),(1,41),(1,45),(1,46),(2,5),(2,8),(2,13),(2,16),(2,19),(2,21),(2,22),(2,24),(2,25),(2,29),(2,32),(2,36),(2,40),(2,44),(3,23),(3,26),(3,30),(3,35),(3,39),(3,43);
/*!40000 ALTER TABLE `coleccion_hecho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contribuyente`
--

DROP TABLE IF EXISTS `contribuyente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contribuyente` (
  `id_contribuyente` int NOT NULL AUTO_INCREMENT,
  `fecha_nacimiento` date DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_contribuyente`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contribuyente`
--

LOCK TABLES `contribuyente` WRITE;
/*!40000 ALTER TABLE `contribuyente` DISABLE KEYS */;
INSERT INTO `contribuyente` VALUES (1,NULL,NULL,'Roberto Sosa'),(2,NULL,NULL,'Juan Rodríguez'),(3,NULL,NULL,'María López'),(4,NULL,NULL,'Carlos Medina'),(5,NULL,NULL,'Daniel Pérez'),(6,NULL,NULL,'Luciana Fernández'),(7,NULL,'yahoo@gmail.com',NULL),(8,NULL,'daniel.perez@gmail.com',NULL),(9,NULL,'luciana.fernandez@yahoo.com',NULL);
/*!40000 ALTER TABLE `contribuyente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coordenadas`
--

DROP TABLE IF EXISTS `coordenadas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coordenadas` (
  `id_ubicacion` int NOT NULL AUTO_INCREMENT,
  `latitud` double DEFAULT NULL,
  `longitud` double DEFAULT NULL,
  PRIMARY KEY (`id_ubicacion`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coordenadas`
--

LOCK TABLES `coordenadas` WRITE;
/*!40000 ALTER TABLE `coordenadas` DISABLE KEYS */;
INSERT INTO `coordenadas` VALUES (1,-31.4201,-64.1888),(2,-32.9442,-60.6505),(3,-34.6145,-68.3301),(4,-38.9516,-68.0591),(5,-26.8241,-65.2226),(6,-34.9214,-57.9544),(7,-25.2637,-57.5759),(8,-27.4514,-58.9867),(9,-24.7859,-65.4117),(10,-38.7167,-62.2651),(11,-32.8895,-68.8458),(12,-31.5375,-68.5364),(13,-43.3002,-65.1023),(14,-45.8641,-67.4966),(15,-40.8135,-62.9967),(16,-51.623,-69.2168),(17,-34.1708,-58.959),(18,-27.3621,-55.9006),(19,-32.4075,-63.2436),(20,-33.295,-66.3356),(21,-38.951,-57.956),(22,-31.6333,-60.7),(23,-24.1858,-65.2995),(24,-53.7875,-67.709),(25,-31.73,-60.53);
/*!40000 ALTER TABLE `coordenadas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `criterio`
--

DROP TABLE IF EXISTS `criterio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `criterio` (
  `tipo_criterio` varchar(30) NOT NULL,
  `id` int NOT NULL,
  `horaFin` time DEFAULT NULL,
  `horaInicio` time DEFAULT NULL,
  `palabraClave` varchar(255) DEFAULT NULL,
  `fechaFin` date DEFAULT NULL,
  `fechaInicio` date DEFAULT NULL,
  `etiqueta_id_etiqueta` int DEFAULT NULL,
  `coordenadas_id_coordenadas` int DEFAULT NULL,
  `categoria_id_categoria` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpm9qpaehvogay6y8ctltdke1u` (`etiqueta_id_etiqueta`),
  KEY `FKpqivuwrog7rkvnj5ph2la7v7q` (`coordenadas_id_coordenadas`),
  KEY `FK1b2v251knim07c7rx9teerpbx` (`categoria_id_categoria`),
  CONSTRAINT `FK1b2v251knim07c7rx9teerpbx` FOREIGN KEY (`categoria_id_categoria`) REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `FKpm9qpaehvogay6y8ctltdke1u` FOREIGN KEY (`etiqueta_id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`),
  CONSTRAINT `FKpqivuwrog7rkvnj5ph2la7v7q` FOREIGN KEY (`coordenadas_id_coordenadas`) REFERENCES `coordenadas` (`id_ubicacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `criterio`
--

LOCK TABLES `criterio` WRITE;
/*!40000 ALTER TABLE `criterio` DISABLE KEYS */;
INSERT INTO `criterio` VALUES ('HORA_SUCESO',1,'06:00:00','18:00:00',NULL,NULL,NULL,NULL,NULL,NULL),('CATEGORIA',2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1),('CATEGORIA',3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2),('CATEGORIA',4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,3),('FECHA_SUCESO',5,NULL,NULL,NULL,'2026-01-01','2024-12-31',NULL,NULL,NULL);
/*!40000 ALTER TABLE `criterio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etiqueta`
--

DROP TABLE IF EXISTS `etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etiqueta` (
  `id_etiqueta` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_etiqueta`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etiqueta`
--

LOCK TABLES `etiqueta` WRITE;
/*!40000 ALTER TABLE `etiqueta` DISABLE KEYS */;
INSERT INTO `etiqueta` VALUES (1,'transito'),(2,'heridos'),(3,'noche'),(4,'boliche'),(5,'comercio'),(6,'robo'),(7,'tarde'),(8,'moto'),(9,'violencia'),(10,'mañana'),(11,'calle'),(12,'amenaza'),(13,'bar'),(14,'bicicleta'),(15,'semaforo'),(16,'auto'),(17,'familia'),(18,'madrugada'),(19,'esquina'),(20,'lluvia'),(21,'taxi'),(22,'supermercado'),(23,'celular'),(24,'online'),(25,'redes'),(26,'fraude'),(27,'mochila'),(28,'alquiler'),(29,'vecinos'),(30,'billetera'),(31,'tarjeta'),(32,'plaza'),(33,'colectivo'),(34,'sorteo'),(35,'vereda'),(36,'peaton'),(37,'venta'),(38,'telefono'),(39,'engaño'),(40,'local');
/*!40000 ALTER TABLE `etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fuente`
--

DROP TABLE IF EXISTS `fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente` (
  `id_fuente` int NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `strategy_tipo_conexion` varchar(255) DEFAULT NULL,
  `tipo_fuente` int DEFAULT NULL,
  PRIMARY KEY (`id_fuente`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente`
--

LOCK TABLES `fuente` WRITE;
/*!40000 ALTER TABLE `fuente` DISABLE KEYS */;
INSERT INTO `fuente` VALUES (1,NULL,'Hechos Reportados','DINAMICA',1),(2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Seguridad Ciudadana','APIREST',2),(3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Reportes Vecinales','APIREST',2),(4,'alertasurbanas.csv','Comunidad ARG','CSV',0);
/*!40000 ALTER TABLE `fuente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho`
--

DROP TABLE IF EXISTS `hecho`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho` (
  `id_hecho` int NOT NULL AUTO_INCREMENT,
  `codigo_fuente` varchar(255) DEFAULT NULL,
  `descripcion` longtext,
  `estado` varchar(255) NOT NULL,
  `fecha_carga` date DEFAULT NULL,
  `fecha_suceso` date DEFAULT NULL,
  `tipo_de_fuente` int DEFAULT NULL,
  `hash` varchar(255) DEFAULT NULL,
  `hora_suceso` time DEFAULT NULL,
  `id_fuente` int DEFAULT NULL,
  `link_fuente` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `ultima_fecha_modificacion` date DEFAULT NULL,
  `id_categoria` int DEFAULT NULL,
  `id_contribuyente` int DEFAULT NULL,
  `id_ubicacion` int DEFAULT NULL,
  PRIMARY KEY (`id_hecho`),
  KEY `FK3ldwx0nwh3rmki5wvkx4ujsnr` (`id_categoria`),
  KEY `FK6u97en7i1ccllj3365a8fco1y` (`id_contribuyente`),
  KEY `FKr0270cvquhqbumujgpndfh1xb` (`id_ubicacion`),
  CONSTRAINT `FK3ldwx0nwh3rmki5wvkx4ujsnr` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `FK6u97en7i1ccllj3365a8fco1y` FOREIGN KEY (`id_contribuyente`) REFERENCES `contribuyente` (`id_contribuyente`),
  CONSTRAINT `FKr0270cvquhqbumujgpndfh1xb` FOREIGN KEY (`id_ubicacion`) REFERENCES `coordenadas` (`id_ubicacion`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho`
--

LOCK TABLES `hecho` WRITE;
/*!40000 ALTER TABLE `hecho` DISABLE KEYS */;
INSERT INTO `hecho` VALUES (1,NULL,'Accidente vial con un motociclista herido.','ACEPTADO','2025-12-15','2025-06-05',2,'86d52e26fd1daba030dc96e14e17e8733375274fe4ad44763391fd24029f8863','08:15:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Choque entre auto y moto en avenida','2025-12-15',4,1,1),(2,NULL,'Dos grupos se enfrentaron durante la madrugada.','ACEPTADO','2025-12-15','2025-07-28',2,'15b73d1ca034d9f1108061728de77f6ddd03336b495e211c69d4d82374ad1b13','03:40:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea violenta a la salida de un boliche','2025-12-15',1,2,2),(3,NULL,'El ladrón empujó a la víctima durante el robo.','ACEPTADO','2025-12-15','2025-08-12',2,'42ad10842883777781ac5935d531d065db6da8605494212243c2f368a797e161','19:25:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo con empujones en comercio','2025-12-15',1,3,3),(4,NULL,'Colisión en hora pico sin heridos graves.','ACEPTADO','2025-12-15','2025-05-20',2,'0b671540374e7e73e905b9f87d37768f1b09e619db12802ea3a708c5bafb96b2','18:30:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente entre colectivo y auto','2025-12-15',4,4,4),(5,NULL,'La moto fue sustraída sin violencia.','ACEPTADO','2025-12-15','2025-06-18',2,'0df337defde7a5636da9d2d17a9b7a8f8e45f71d1227f4d71c99397349ad12e1','02:10:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo de moto estacionada','2025-12-15',2,5,5),(6,NULL,'Discusión de tránsito terminó en agresiones.','ACEPTADO','2025-12-15','2025-07-01',2,'219e5d80ac797cbce82fa029d030299be40ebc25a0abf1b845aef0569a06c60c','17:30:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea entre conductores','2025-12-15',1,2,6),(7,NULL,'El motociclista cayó al intentar esquivar un auto.','ACEPTADO','2025-12-15','2025-04-09',2,'68dac7b3dc67bc20e25cf2e825cbaa1b4e80c0a884b36d6091cc2c4a9626a6a7','09:10:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente de moto en cruce','2025-12-15',4,1,7),(8,NULL,'El delincuente intimidó verbalmente a la víctima.','ACEPTADO','2025-12-15','2025-08-03',2,'ae7adf278df7c4f27b346656726a60348630aeea231b8500827d2ce78f2faa11','22:15:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo a peatón con amenazas','2025-12-15',2,3,8),(9,NULL,'Tres vehículos involucrados en cadena.','ACEPTADO','2025-12-15','2025-05-29',2,'d40f553f4c18b669c46a721909e30900714a88bee8a5a2fe8a8ff93e6f72020c','07:55:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente múltiple en avenida','2025-12-15',4,4,9),(10,NULL,'Dos personas se golpearon tras una discusión.','ACEPTADO','2025-12-15','2025-09-18',2,'7e729bab35da7dae62eff1ccba06ceeed6d8a54d49b50496f4c70fa3ee6bcd4e','23:10:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea en bar durante la noche','2025-12-15',1,6,10),(11,NULL,'La víctima intentó resistirse al robo.','ACEPTADO','2025-12-15','2025-06-26',2,'7881eaca4df93e244c5146bfd25ed059c5880d4eaa62c7d36d007f5ead9f7963','18:40:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo de bicicleta con forcejeo','2025-12-15',1,2,11),(12,NULL,'Un vehículo cruzó en rojo y provocó el choque.','ACEPTADO','2025-12-15','2025-07-14',2,'64070ba1d15a090bb619e7e83fcf9bfd93fd97e3213014a7e1ef684d4f338d91','08:50:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente por semáforo en rojo','2025-12-15',4,1,12),(13,NULL,'El vehículo fue sustraído mientras estaba estacionado.','ACEPTADO','2025-12-15','2025-05-03',2,'7e701d02e896708803a3212b0c26bcebeb022056d3f3cb42db53dc107bc94387','01:30:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo de auto sin violencia','2025-12-15',2,5,13),(14,NULL,'Discusión que requirió intervención policial.','ACEPTADO','2025-12-15','2025-08-27',2,'c1cf7be7440b102dd5c0579772a7038ec07db38bbc14fbdd2f8cb55ec7ad2796','20:30:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea familiar en la vía pública','2025-12-15',1,3,14),(15,NULL,'Impacto lateral sin heridos de gravedad.','ACEPTADO','2025-12-15','2025-06-16',2,'d8cc84ef4a268bbcf453d433f3e66711b0346a249af92d04011b6050489e2222','15:20:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente entre camioneta y auto','2025-12-15',4,4,15),(16,NULL,'Forzaron la persiana del local.','ACEPTADO','2025-12-15','2025-07-07',2,'bf9e766cba6ec2679e768de0fd7063a07bdcc0a240e25d2ecbb51c609bfdeb6f','02:45:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo a comercio durante la madrugada','2025-12-15',2,6,16),(17,NULL,'Vecinos alertaron por ruidos y gritos.','ACEPTADO','2025-12-15','2025-09-01',2,'eb1389b5d30f68e2acf2850bace1dcaa6375984ccdada3f7337a3c2262cb5ffc','23:35:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea entre jóvenes en esquina','2025-12-15',1,2,17),(18,NULL,'Un auto despistó por el pavimento mojado.','ACEPTADO','2025-12-15','2025-04-21',2,'e2440c8eb991c40af72e03c8e226f60bb6de51b28f32cc02b007dc6587ef1c53','06:55:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Accidente por lluvia intensa','2025-12-15',4,1,18),(19,NULL,'El delincuente huyó corriendo.','ACEPTADO','2025-12-15','2025-08-19',2,'0b3dd4912aff2ee98fa623e8a5686cdc7b1b34562f5711ae5f31f8e37c322f57','21:10:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo de bolso en parada de taxi','2025-12-15',2,3,19),(20,NULL,'Discusión por una fila terminó en golpes.','ACEPTADO','2025-12-15','2025-07-23',2,'d134e6e9a041c410b16adcd16dad3a2a7204e525da963ed0e4929ece9827d8e2','19:00:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Pelea entre clientes en supermercado','2025-12-15',1,4,20),(21,NULL,'Un delincuente sustrajo un celular mientras la víctima esperaba el colectivo.','ACEPTADO','2025-12-15','2025-08-12',2,'121e1780d599b7b04e4f10ce2363afa86f1509d5670efb6918d898dac084ba80','19:20:00',2,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Robo de celular en parada de colectivo','2025-12-15',2,3,6),(22,NULL,'Un delincuente sustrajo un celular mientras la víctima esperaba el colectivo.','ACEPTADO','2025-12-15','2025-08-12',2,'121e1780d599b7b04e4f10ce2363afa86f1509d5670efb6918d898dac084ba80','19:20:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de celular en parada de colectivo','2025-12-15',2,3,6),(23,NULL,'Publicación ofrecía entradas para un recital que nunca fueron entregadas.','ACEPTADO','2025-12-15','2025-09-10',2,'12358c5def2b80d7d67ef0833067de4a71734c1aa864227f8a9f45a84435f13f','21:30:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Falsa venta de entradas por Instagram','2025-12-15',3,7,1),(24,NULL,'Una persona fue sorprendida por detrás y le robaron la mochila.','ACEPTADO','2025-12-15','2025-07-03',2,'74e51d0b02ecb164fafd89924739b82f4829abe456aa2d4dc4e6bbec9d566932','18:45:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Arrebato de mochila en la vía pública','2025-12-15',2,6,2),(25,NULL,'La bicicleta estaba apoyada y fue sustraída rápidamente.','ACEPTADO','2025-12-15','2025-04-18',2,'8672dcb9b9badb9fa9f7ba83ffc9bd14346138c5af5595ce332ed892bde49842','17:55:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de bicicleta en la calle','2025-12-15',2,2,11),(26,NULL,'Se transfirió una seña por un departamento que no existía.','ACEPTADO','2025-12-15','2025-10-12',2,'1616297e0a6b41bdf501c34f6a2f71a7438584b2dd5c35ba1970ad14c1545903','11:30:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Estafa por alquiler inexistente','2025-12-15',3,6,3),(27,NULL,'Discusión que terminó con empujones y golpes leves.','ACEPTADO','2025-12-15','2025-06-20',2,'dabd8388014ebac387cb2b0887c38b25c49a1cf5ea505de717a694e887b03e4a','20:10:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Pelea entre vecinos por estacionamiento','2025-12-15',1,4,5),(28,NULL,'Choque sin heridos en una esquina barrial.','ACEPTADO','2025-12-15','2025-05-08',2,'5d57d3494f3cf45a1bd78a73a96571fc2abe4b7c87f95ebca8cdf701d4917b98','09:05:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Accidente leve entre dos autos','2025-12-15',4,3,4),(29,NULL,'La víctima notó la sustracción al salir del local.','ACEPTADO','2025-12-15','2025-06-22',2,'2fb9a03d97440381a0eef9410234792a7350a751801b31777c4c124d0753d150','14:40:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de billetera en comercio','2025-12-15',2,2,8),(30,NULL,'El sitio imitaba una tienda real para robar datos.','ACEPTADO','2025-12-15','2025-11-01',2,'e2777e0b3f6e5ce323ffeb519dc288e75f30e19ddfb93dcf0921e54fe2b3caca','23:15:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Estafa por marketplace falso','2025-12-15',3,8,9),(31,NULL,'Dos grupos se enfrentaron verbalmente y a golpes.','ACEPTADO','2025-12-15','2025-09-05',2,'4136f64b5fc82f51d6af07173e84597d6b54eb6714af4dc68d74ed0fb945212c','22:05:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Pelea en plaza durante la noche','2025-12-15',1,1,10),(32,NULL,'La víctima sufrió el robo al descender del transporte.','ACEPTADO','2025-12-15','2025-07-18',2,'47edd0dd7567314518b3c54e79ff3fc1edd1e274a17d5f7107e219517fe2f0c8','18:30:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de mochila en colectivo','2025-12-15',2,7,13),(33,NULL,'Una pelea verbal escaló a agresiones físicas.','ACEPTADO','2025-12-15','2025-08-25',2,'d23a9e073568306653311697f17a95ee07f2c09d6da416600013330edd43e589','16:50:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Discusión violenta entre comerciantes','2025-12-15',1,4,12),(34,NULL,'El motociclista perdió el control y cayó al asfalto.','ACEPTADO','2025-12-15','2025-04-02',2,'ae19a7eeb4da3394bc14cdc615d41bfa86042c199cc013a10d0b1803bea108df','07:40:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Accidente de moto en calle residencial','2025-12-15',4,3,18),(35,NULL,'Se pedían datos personales para participar de un premio inexistente.','ACEPTADO','2025-12-15','2025-10-28',2,'2ab7090b5bbf46efb3c66a5f1a8ac5ec83485ccdcb8307ff8bcc79ec923b5702','20:45:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Estafa por falso sorteo','2025-12-15',3,9,14),(36,NULL,'Un arrebatador escapó corriendo tras el robo.','ACEPTADO','2025-12-15','2025-06-30',2,'d1c0e0d366eb67658abfd829ae9bdadedd62de09142aac74746c262645e6f804','18:05:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de cartera en vereda','2025-12-15',2,2,19),(37,NULL,'Colisión leve en senda peatonal.','ACEPTADO','2025-12-15','2025-05-27',2,'ac5e722e8b839cafc43b266f12f22e4d1f06b8e8db5dc79df298097aa088ab77','12:20:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Accidente entre bicicleta y peatón','2025-12-15',4,1,17),(38,NULL,'Discusión por ruidos molestos con amenazas verbales.','ACEPTADO','2025-12-15','2025-09-02',2,'c739b93bdc64cb049a89fd278fc5c929abb3a3304b3b7fd91e8438b44baa95e5','23:00:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Amenazas entre vecinos','2025-12-15',1,3,15),(39,NULL,'El producto nunca fue entregado tras el pago.','ACEPTADO','2025-12-15','2025-11-15',2,'bd1018ac6f13177525529a0fafdd2941893f5a5a5412c52feb5176b5cc47f4b6','19:50:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Estafa por compra de electrodoméstico','2025-12-15',3,8,21),(40,NULL,'El casco fue sustraído de una moto estacionada.','ACEPTADO','2025-12-15','2025-07-22',2,'9e6b2bf1c042402b4683cc7d686b7b74cdf2db1ab19f399a13c04bbc0dbc4542','17:15:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de casco de moto','2025-12-15',2,2,16),(41,NULL,'Dos personas discutieron y se empujaron en la calle.','ACEPTADO','2025-12-15','2025-08-08',2,'1d9f77ae66977bbabf548166a8b17b153b7e0671721ba5b7b2a9be6f5933b0a0','18:55:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Discusión violenta en vía pública','2025-12-15',1,4,7),(42,NULL,'Un auto impactó al de adelante en tráfico lento.','ACEPTADO','2025-12-15','2025-06-12',2,'53bd1d20bea380473592182dd4d380984b3eab9adbcb2404da9ffef1acc33476','08:35:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Accidente por frenada brusca','2025-12-15',4,1,22),(43,NULL,'Un supuesto técnico solicitó claves por teléfono.','ACEPTADO','2025-12-15','2025-10-05',2,'f5ddad47e5479f33fbf2ccb2569b2046dc6e00d9c8c96a1648b2e876c685b428','16:10:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Estafa por falso soporte técnico','2025-12-15',3,7,23),(44,NULL,'El bolso fue sustraído mientras la víctima estaba sentada.','ACEPTADO','2025-12-15','2025-09-18',2,'e61d106f0776b6e29975ba246a3c73f0dfa784201a7c8836d67906953479604b','22:20:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Robo de bolso en bar','2025-12-15',2,6,24),(45,NULL,'Discusión que derivó en golpes sin heridos graves.','ACEPTADO','2025-12-15','2025-07-11',2,'62398bd0496d65b62b4a0107515785e69d77c6448efaee4147066e741cca828f','21:05:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Pelea entre clientes en local','2025-12-15',1,3,25),(46,NULL,'Dos grupos se enfrentaron durante la madrugada.','ACEPTADO','2025-12-15','2025-07-28',2,'15b73d1ca034d9f1108061728de77f6ddd03336b495e211c69d4d82374ad1b13','03:40:00',3,'https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Pelea violenta a la salida de un boliche','2025-12-15',1,2,2);
/*!40000 ALTER TABLE `hecho` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_etiqueta`
--

DROP TABLE IF EXISTS `hecho_etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_etiqueta` (
  `id_hecho` int NOT NULL,
  `id_etiqueta` int NOT NULL,
  KEY `FK6ohfi3g782fu9429b24g7s9s2` (`id_etiqueta`),
  KEY `FKfs329vxq26kuxlnmisaey1lt0` (`id_hecho`),
  CONSTRAINT `FK6ohfi3g782fu9429b24g7s9s2` FOREIGN KEY (`id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`),
  CONSTRAINT `FKfs329vxq26kuxlnmisaey1lt0` FOREIGN KEY (`id_hecho`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_etiqueta`
--

LOCK TABLES `hecho_etiqueta` WRITE;
/*!40000 ALTER TABLE `hecho_etiqueta` DISABLE KEYS */;
INSERT INTO `hecho_etiqueta` VALUES (1,1),(1,2),(2,3),(2,4),(3,5),(3,6),(4,1),(4,7),(5,8),(5,3),(6,1),(6,9),(7,8),(7,10),(8,11),(8,12),(9,1),(9,10),(10,13),(10,3),(11,14),(11,11),(12,15),(12,1),(13,16),(13,3),(14,17),(14,11),(15,1),(15,7),(16,5),(16,18),(17,19),(17,3),(18,20),(18,10),(19,21),(19,3),(20,22),(20,7),(21,11),(21,23),(21,3),(22,11),(22,23),(22,3),(23,24),(23,25),(23,26),(24,11),(24,6),(24,27),(25,11),(25,14),(26,28),(26,24),(26,26),(27,29),(27,3),(28,1),(28,10),(29,5),(29,30),(30,24),(30,31),(31,32),(31,3),(32,33),(32,6),(33,5),(33,9),(34,8),(34,10),(35,34),(35,24),(36,35),(36,6),(37,14),(37,36),(38,29),(38,3),(39,24),(39,37),(40,8),(40,6),(41,11),(41,9),(42,1),(42,10),(43,38),(43,39),(44,13),(44,3),(45,40),(45,9),(46,3),(46,4);
/*!40000 ALTER TABLE `hecho_etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_multimedia`
--

DROP TABLE IF EXISTS `hecho_multimedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_multimedia` (
  `hecho_id` int NOT NULL,
  `multimedia` varchar(255) DEFAULT NULL,
  KEY `hecho_multimedia_hecho` (`hecho_id`),
  CONSTRAINT `hecho_multimedia_hecho` FOREIGN KEY (`hecho_id`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_multimedia`
--

LOCK TABLES `hecho_multimedia` WRITE;
/*!40000 ALTER TABLE `hecho_multimedia` DISABLE KEYS */;
/*!40000 ALTER TABLE `hecho_multimedia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hechos_visibles`
--

DROP TABLE IF EXISTS `hechos_visibles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hechos_visibles` (
  `id_coleccion` int NOT NULL,
  `id_hecho` int NOT NULL,
  KEY `FK934o6rq7omnongou3uyk9m74q` (`id_hecho`),
  KEY `FK3s0mykycwyiwbk3rfw22oso9w` (`id_coleccion`),
  CONSTRAINT `FK3s0mykycwyiwbk3rfw22oso9w` FOREIGN KEY (`id_coleccion`) REFERENCES `coleccion` (`id_coleccion`),
  CONSTRAINT `FK934o6rq7omnongou3uyk9m74q` FOREIGN KEY (`id_hecho`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hechos_visibles`
--

LOCK TABLES `hechos_visibles` WRITE;
/*!40000 ALTER TABLE `hechos_visibles` DISABLE KEYS */;
INSERT INTO `hechos_visibles` VALUES (1,2),(1,3),(1,10),(1,11),(1,14),(1,17),(1,20),(1,27),(1,31),(1,38),(1,41),(1,45),(1,46),(2,5),(2,8),(2,13),(2,16),(2,19),(2,21),(2,22),(2,24),(2,25),(2,29),(2,32),(2,36),(2,40),(2,44),(3,23),(3,26),(3,30),(3,35),(3,39),(3,43);
/*!40000 ALTER TABLE `hechos_visibles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (6);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modificacion_etiqueta`
--

DROP TABLE IF EXISTS `modificacion_etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modificacion_etiqueta` (
  `id_sugerencia_de_cambio` int NOT NULL,
  `id_etiqueta` int NOT NULL,
  KEY `FK5emeaxin4f9hncn268hwrco6a` (`id_etiqueta`),
  KEY `FK862r6e4e2lxdimn9p64qnvmwe` (`id_sugerencia_de_cambio`),
  CONSTRAINT `FK5emeaxin4f9hncn268hwrco6a` FOREIGN KEY (`id_etiqueta`) REFERENCES `etiqueta` (`id_etiqueta`),
  CONSTRAINT `FK862r6e4e2lxdimn9p64qnvmwe` FOREIGN KEY (`id_sugerencia_de_cambio`) REFERENCES `sugerencia_de_cambio` (`id_sugerencia_de_cambio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modificacion_etiqueta`
--

LOCK TABLES `modificacion_etiqueta` WRITE;
/*!40000 ALTER TABLE `modificacion_etiqueta` DISABLE KEYS */;
/*!40000 ALTER TABLE `modificacion_etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitud_de_eliminacion`
--

DROP TABLE IF EXISTS `solicitud_de_eliminacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitud_de_eliminacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aceptada` bit(1) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fechaDeRevision` datetime(6) DEFAULT NULL,
  `hecho_id_hecho` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqhjd6r74dnpwobooyno0ngl5x` (`hecho_id_hecho`),
  CONSTRAINT `FKqhjd6r74dnpwobooyno0ngl5x` FOREIGN KEY (`hecho_id_hecho`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitud_de_eliminacion`
--

LOCK TABLES `solicitud_de_eliminacion` WRITE;
/*!40000 ALTER TABLE `solicitud_de_eliminacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `solicitud_de_eliminacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sugerencia_de_cambio`
--

DROP TABLE IF EXISTS `sugerencia_de_cambio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sugerencia_de_cambio` (
  `id_sugerencia_de_cambio` int NOT NULL AUTO_INCREMENT,
  `aprobada` bit(1) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `descripcion_sugerencia` varchar(255) DEFAULT NULL,
  `fecha_revision` date DEFAULT NULL,
  `fecha_suceso` date DEFAULT NULL,
  `fecha_sugerencia` date DEFAULT NULL,
  `hora_suceso` time DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `id_categoria` int DEFAULT NULL,
  `id_hecho` int NOT NULL,
  PRIMARY KEY (`id_sugerencia_de_cambio`),
  KEY `FK6485vcixy61n4y67mp4245orj` (`id_categoria`),
  KEY `FKq2gm98dq8psum87g17ynwa1is` (`id_hecho`),
  CONSTRAINT `FK6485vcixy61n4y67mp4245orj` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`),
  CONSTRAINT `FKef3ttvx6chyqqni7mgk3xbd84` FOREIGN KEY (`id_sugerencia_de_cambio`) REFERENCES `hecho` (`id_hecho`),
  CONSTRAINT `FKq2gm98dq8psum87g17ynwa1is` FOREIGN KEY (`id_hecho`) REFERENCES `hecho` (`id_hecho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sugerencia_de_cambio`
--

LOCK TABLES `sugerencia_de_cambio` WRITE;
/*!40000 ALTER TABLE `sugerencia_de_cambio` DISABLE KEYS */;
/*!40000 ALTER TABLE `sugerencia_de_cambio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contrasena` varchar(255) DEFAULT NULL,
  `correo` varchar(255) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `foto` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2mlfr087gb1ce55f2j87o74t` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,NULL,'jmillan@frba.utn.edu.ar',NULL,NULL,'Juan Ignacio MillÁn','USER');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `cargador_dinamica`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cargador_dinamica` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cargador_dinamica`;

--
-- Table structure for table `fuente`
--

DROP TABLE IF EXISTS `fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente` (
  `id` int NOT NULL,
  `codigoFuente` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `strategy_tipo_conexion` varchar(255) DEFAULT NULL,
  `ultimoProcesamiento` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente`
--

LOCK TABLES `fuente` WRITE;
/*!40000 ALTER TABLE `fuente` DISABLE KEYS */;
INSERT INTO `fuente` VALUES (1,'DINAMICA',NULL,'Hechos Reportados','DINAMICA',NULL);
/*!40000 ALTER TABLE `fuente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_etiqueta`
--

DROP TABLE IF EXISTS `hecho_etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_etiqueta` (
  `hash` varchar(255) NOT NULL,
  `etiqueta` varchar(100) NOT NULL,
  KEY `hecho_etiqueta_hecho` (`hash`),
  CONSTRAINT `hecho_etiqueta_hecho` FOREIGN KEY (`hash`) REFERENCES `hechos` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_etiqueta`
--

LOCK TABLES `hecho_etiqueta` WRITE;
/*!40000 ALTER TABLE `hecho_etiqueta` DISABLE KEYS */;
/*!40000 ALTER TABLE `hecho_etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hecho_multimedia`
--

DROP TABLE IF EXISTS `hecho_multimedia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hecho_multimedia` (
  `hash` varchar(255) NOT NULL,
  `url` varchar(500) NOT NULL,
  KEY `hecho_multimedia_hecho` (`hash`),
  CONSTRAINT `hecho_multimedia_hecho` FOREIGN KEY (`hash`) REFERENCES `hechos` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hecho_multimedia`
--

LOCK TABLES `hecho_multimedia` WRITE;
/*!40000 ALTER TABLE `hecho_multimedia` DISABLE KEYS */;
/*!40000 ALTER TABLE `hecho_multimedia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hechos`
--

DROP TABLE IF EXISTS `hechos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hechos` (
  `hash` varchar(255) NOT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `contribuyente` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha_carga` varchar(255) DEFAULT NULL,
  `fecha_suceso` varchar(255) DEFAULT NULL,
  `fue_extraido` bit(1) DEFAULT NULL,
  `hora_suceso` varchar(255) DEFAULT NULL,
  `idFuente` int DEFAULT NULL,
  `latitud` varchar(255) DEFAULT NULL,
  `link_fuente` varchar(255) DEFAULT NULL,
  `longitud` varchar(255) DEFAULT NULL,
  `tipo_fuente` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hechos`
--

LOCK TABLES `hechos` WRITE;
/*!40000 ALTER TABLE `hechos` DISABLE KEYS */;
/*!40000 ALTER TABLE `hechos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `cargador_proxy`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cargador_proxy` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cargador_proxy`;

--
-- Table structure for table `fuente`
--

DROP TABLE IF EXISTS `fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente` (
  `id` int NOT NULL,
  `codigoFuente` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `strategy_tipo_conexion` varchar(255) DEFAULT NULL,
  `ultimoProcesamiento` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente`
--

LOCK TABLES `fuente` WRITE;
/*!40000 ALTER TABLE `fuente` DISABLE KEYS */;
INSERT INTO `fuente` VALUES (2,'API REST','https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/SeguridadCiudadana','Seguridad Ciudadana','APIREST','2025-12-15 23:49:15.565234'),(3,'API REST','https://69344a4e4090fe3bf01f882a.mockapi.io/metamapa/ReportesVecinales','Reportes Vecinales','APIREST','2025-12-15 23:49:15.732108');
/*!40000 ALTER TABLE `fuente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `cargador_estatica`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cargador_estatica` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cargador_estatica`;

--
-- Table structure for table `fuente`
--

DROP TABLE IF EXISTS `fuente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fuente` (
  `id` int NOT NULL,
  `codigoFuente` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `strategy_tipo_conexion` varchar(255) DEFAULT NULL,
  `ultimoProcesamiento` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fuente`
--

LOCK TABLES `fuente` WRITE;
/*!40000 ALTER TABLE `fuente` DISABLE KEYS */;
INSERT INTO `fuente` VALUES (4,'CSV','alertasurbanas.csv','Comunidad ARG','CSV','2025-12-15 23:49:15.833773');
/*!40000 ALTER TABLE `fuente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-15 23:51:47
