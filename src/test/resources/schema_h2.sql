SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `game`
-- ----------------------------
DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) DEFAULT NULL,
  `game_type`  VARCHAR(255) NOT NULL CHECK
        (game_type in ('BOT_GAME',
         'TWO_PLAYER_GAME',
         'KAIJI_GAME')) ,
  `number_of_cards` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `game_history`
-- ----------------------------
DROP TABLE IF EXISTS `game_history`;
CREATE TABLE `game_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) NOT NULL,
  `game_state` VARCHAR(50) NOT NULL CHECK
              (game_state in ('GAME_INITIALIZATION',
               'GAME_STARTED','GAME_PLAYING',
               'GAME_FINISHED',
                'GAME_INTERRUPTED',
                'GAME_BROKEN')),
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5obkctm2iilnd4d8mm3y4314p` (`game_id`),
  FOREIGN KEY (`game_id`) REFERENCES `game` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `game_winners`
-- ----------------------------
DROP TABLE IF EXISTS `game_winners`;
CREATE TABLE `game_winners` (
  `game_history_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`game_history_id`,`user_id`),
  FOREIGN KEY (`game_history_id`) REFERENCES `game_history` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `link_game_to_user`
-- ----------------------------
DROP TABLE IF EXISTS `link_game_to_user`;
CREATE TABLE `link_game_to_user` (
  `game_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`game_id`,`user_id`),
  FOREIGN KEY (`game_id`) REFERENCES `game` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `round`
-- ----------------------------
DROP TABLE IF EXISTS `round`;
CREATE TABLE `round` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_history_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`game_history_id`) REFERENCES `game_history` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `round_detail`
-- ----------------------------
DROP TABLE IF EXISTS `round_detail`;
CREATE TABLE `round_detail` (
  `round_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `card` VARCHAR(20) NOT NULL CHECK (card in ('ROCK','PAPER','SCISSORS')),
  `duel_result` VARCHAR(20) NOT NULL CHECK (duel_result in ('WIN','LOSE','DRAW')),
  PRIMARY KEY (`round_id`,`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  FOREIGN KEY (`round_id`) REFERENCES `round` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(30) NOT NULL,
  `password` varchar(60) NOT NULL,
  `name` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `registration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UK_n4swgcf30j6bmtb4l4cjryuym` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role` VARCHAR(20) NOT NULL CHECK (role in ('USER_ROLE','ADMIN_ROLE')),
  PRIMARY KEY (`user_id`,`role`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;