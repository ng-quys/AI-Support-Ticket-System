-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: ai_support_ticket_db
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.24.04.1

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
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `category` varchar(100) DEFAULT NULL,
  `priority` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `ai_suggestion` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `classification_source` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (19,'Payment failed test','My payment failed but money was deducted from my account','PAYMENT_ISSUE','HIGH','OPEN','Thank you for reporting this payment issue. We understand your money may have been deducted. Our support team will verify the transaction and update you as soon as possible.','2026-05-13 05:40:30','2026-05-13 07:22:57','KEYWORD_FALLBACK'),(22,'Payment failed','My payment failed but money was deducted','PAYMENT_ISSUE','HIGH','OPEN','Thank you for reporting this payment issue. We understand your money may have been deducted. Our support team will verify the transaction and update you as soon as possible.','2026-05-13 06:24:42','2026-05-13 07:22:57','KEYWORD_FALLBACK'),(25,'can\'t login','I entered the correct password but can\'t log in','AUTHENTICATION_ISSUE','MEDIUM','OPEN','We are sorry you are facing login issues. Please try resetting your password via the \'Forgot Password\' link on the login page.','2026-05-13 06:36:27','2026-05-13 07:22:57','GEMINI'),(26,'Chuyển tiền bị lỗi','Tôi chuyển tiền rồi, tài khoản đã bị trừ nhưng người nhận chưa nhận được tiền','PAYMENT_ISSUE','HIGH','OPEN','We understand that your money was deducted but the recipient has not yet received it. Please provide the transaction details so we can investigate and resolve this for you.','2026-05-13 06:40:30','2026-05-13 07:22:57','GEMINI'),(27,'Cannot login again','I cannot sign in and forgot my password','AUTHENTICATION_ISSUE','HIGH','OPEN','We\'re sorry to hear you\'re having trouble logging in. Please try resetting your password via the \'Forgot Password\' link on the login page. If the issue persists, our support team is ready to assist you further.','2026-05-13 06:43:42','2026-05-13 07:22:57','GEMINI'),(28,'Mobile app crashes frequently','The app crashes every time I try to open the dashboard.','TECHNICAL_BUG','HIGH','OPEN','We\'re sorry to hear you\'re experiencing app crashes. Our technical team is investigating this issue and will release a fix as soon as possible. Please ensure your app is updated to the latest version.','2026-05-13 06:44:49','2026-05-13 07:22:57','GEMINI'),(29,'Need to update phone number','I want to change my registered phone number.','ACCOUNT_ISSUE','MEDIUM','OPEN','To update your registered phone number, please navigate to your profile settings within the app. You may need to complete a verification step to confirm the change.','2026-05-13 06:45:50','2026-05-13 07:22:57','GEMINI'),(30,'Cannot login to account','I entered the correct password but still cannot login.','AUTHENTICATION_ISSUE','HIGH','OPEN','We\'re sorry you\'re having trouble logging in. Please try resetting your password via the \'Forgot Password\' link on the login page. If the issue persists, our support team is here to help.','2026-05-13 06:46:45','2026-05-13 07:22:57','GEMINI'),(31,'Payment failed but money deductedt','The payment transaction failed but money was deducted from my bank account.','PAYMENT_ISSUE','HIGH','OPEN','We understand your payment failed and money was deducted. Please provide your transaction ID or account details so we can investigate and process a refund.','2026-05-13 06:51:11','2026-05-13 07:22:57','GEMINI'),(32,'App crash','The app keeps crashing with an error.','TECHNICAL_BUG','MEDIUM','OPEN','We\'re sorry to hear you\'re experiencing app crashes. Please try clearing your app cache and restarting your device. If the issue persists, please provide us with more details about the error message you\'re seeing.','2026-05-13 06:52:08','2026-05-13 07:22:57','GEMINI'),(33,'Cần hỗ trợ','Tôi có một câu hỏi chung về dịch vụ.','OTHER','LOW','OPEN','Thank you for contacting us. Please provide more details about your question so we can assist you effectively.','2026-05-13 06:53:14','2026-05-13 07:22:57','GEMINI'),(34,'Đổi số điện thoại','Tôi muốn cập nhật số điện thoại trong tài khoản nhưng app cứ báo lỗi.','ACCOUNT_ISSUE','MEDIUM','OPEN','We\'re sorry you\'re experiencing an error while updating your phone number. Please try again later or contact support for assistance.','2026-05-13 06:54:24','2026-05-13 07:22:57','GEMINI'),(35,'Unable to reset password','I clicked forgot password but did not receive any reset email.','AUTHENTICATION_ISSUE','HIGH','OPEN','We\'re sorry you\'re having trouble resetting your password. Please check your spam/junk folder for the reset email. If you still can\'t find it, please try again in a few minutes, or contact support for further assistance.','2026-05-13 07:01:34','2026-05-13 07:22:57','GEMINI'),(36,'Transfer completed but receiver did not get money','I transferred money successfully but the receiver account still shows no balance update.','PAYMENT_ISSUE','HIGH','OPEN','We understand your concern about the transfer. Please provide the transaction ID and receiver\'s account details so we can investigate and resolve this promptly.','2026-05-13 07:02:19','2026-05-13 07:22:57','GEMINI'),(37,'Cannot verify OTP code','I entered the OTP code several times but verification keeps failing.','AUTHENTICATION_ISSUE','HIGH','OPEN','We\'re sorry you\'re having trouble with OTP verification. Please ensure your phone has a stable internet connection and try again. If the issue persists, please contact us so we can assist further.','2026-05-13 07:03:08','2026-05-13 07:22:57','GEMINI'),(38,'Dashboard loading very slowly','The dashboard takes more than 30 seconds to load data.','TECHNICAL_BUG','MEDIUM','OPEN','We apologize for the slow loading times. Our technical team is investigating this issue and working to resolve it as quickly as possible. We appreciate your patience.','2026-05-13 07:03:50','2026-05-13 07:22:57','GEMINI'),(39,'Transaction history missing','My latest transaction does not appear in transaction history.','PAYMENT_ISSUE','MEDIUM','OPEN','We\'re sorry to hear your transaction history is not updated. Please allow up to 24 hours for recent transactions to appear. If it\'s still missing after that, please contact us again.','2026-05-13 07:04:31','2026-05-13 07:22:57','GEMINI'),(40,'Unexpected server error during login','Every login attempt returns a 500 internal server error.','AUTHENTICATION_ISSUE','HIGH','OPEN','We are experiencing a technical issue that may be affecting login. Our team is working on a fix. Please try again in a few minutes.','2026-05-13 07:05:02','2026-05-13 07:22:57','GEMINI'),(41,'Double payment detected','I was charged twice for one transaction.','PAYMENT_ISSUE','HIGH','OPEN','We understand you\'ve been charged twice for a transaction. Please provide the transaction details, and we will investigate this immediately to process a refund for the duplicate charge.','2026-05-13 07:05:35','2026-05-13 07:22:57','GEMINI'),(42,'Need account help','I cannot login to my account and need authentication support','AUTHENTICATION_ISSUE','HIGH','CLOSED','We understand you\'re having trouble logging in. Please try resetting your password via the \'Forgot Password\' link on the login page. If the issue persists, please contact us again with your registered email address.','2026-05-13 07:26:59','2026-05-13 08:49:25','GEMINI'),(43,'Payment failed','My payment failed but money was deducted','PAYMENT_ISSUE','HIGH','OPEN','We\'re sorry to hear your payment failed and money was deducted. Please provide your transaction ID or account details so we can investigate this for you.','2026-05-13 07:44:57','2026-05-13 07:44:57','GEMINI');
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-13 16:30:53
