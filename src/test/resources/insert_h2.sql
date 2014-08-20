--
-- Dumping data for table `game`
--

/*LOCK TABLES `game` WRITE;*/
INSERT INTO `game` VALUES (16, 'Duel', 'BOT_GAME',2 ,'2014-08-17 09:56:33.0','2014-08-17 09:56:36.0',),(30, 'Duel', 'BOT_GAME',2, '2014-08-20 09:19:35','2014-08-20 09:19:40');
/*UNLOCK TABLES; 09:56:36.0*/

--
-- Dumping data for table `game_history`
--

/*LOCK TABLES `game_history` WRITE;*/
INSERT INTO `game_history`
VALUES (16,16,'GAME_FINISHED'),(30, 30,'GAME_FINISHED');
/*UNLOCK TABLES;*/

--
-- Dumping data for table `game_winners`
--

/*LOCK TABLES `game_winners` WRITE;*/
INSERT INTO `game_winners` VALUES (16,23);
/*UNLOCK TABLES;*/

--
-- Dumping data for table `link_game_to_user`
--

/*LOCK TABLES `link_game_to_user` WRITE;*/
INSERT INTO `link_game_to_user` VALUES (16,23),(30,25);
/*UNLOCK TABLES;*/

--
-- Dumping data for table `round`
--

/*LOCK TABLES `round` WRITE;*/
INSERT INTO `round` VALUES (61,16),(62,16),(63,16),(64,16),(106,30),(107,30),(108,30),(109,30),(110,30);
/*UNLOCK TABLES;*/

--
-- Dumping data for table `round_detail`
--

/*LOCK TABLES `round_detail` WRITE;*/
INSERT INTO `round_detail` VALUES (61, 23,'SCISSORS','WIN'),(62, 23,'ROCK','WIN'),(63, 23,'PAPER','WIN'),(64,23,'PAPER','DRAW'),(106, 25,'PAPER','LOSE'),(107, 25,'ROCK','DRAW'),(108, 25,'SCISSORS','WIN'),(109,25,'PAPER','LOSE'),(110, 25, 'SCISSORS','WIN');
/*UNLOCK TABLES;*/

--
-- Dumping data for table `user`
--

/*LOCK TABLES `user` WRITE;*/
INSERT INTO `user` VALUES (15,'petya','$2a$10$jDIzLI1hY9HsLF8/q3eyoe5g5JSYEp1FN.ayv.TRTubVxZlZPRCHy','petya','2@2','2014-07-18 16:00:37'),(23,'vas','$2a$10$b1zfl0hlzgLdH6G3mFgA9eJVh492pfJBAKOUys.90INubpb76VqZy','vasya','12122@1212','2014-07-30 17:54:18'),(25, 'kol','$2a$10$HofV19Tp6kg5uiPbhoXT5.VJgQDVom0Y/agNN.7zXmdkiWs4G7zje','kol','kol@kol','2014-08-10 17:38:19');
/*UNLOCK TABLES;*/

--
-- Dumping data for table `user_role`
--

/*LOCK TABLES `user_role` WRITE;*/
INSERT INTO `user_role` VALUES (15,'USER_ROLE'),(15,'ADMIN_ROLE'),(23,'USER_ROLE'),(25,'USER_ROLE');
/*UNLOCK TABLES;*/