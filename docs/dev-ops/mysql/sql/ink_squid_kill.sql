SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE database if NOT EXISTS `ink_squid_kill` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `ink_squid_kill`;

-- ----------------------------
-- Table structure for game
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
                        `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                        `room_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '房间ID',
                        `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` timestamp NOT NULL COMMENT '更新时间',
                        `status` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '房间状态',
                        `has_host` tinyint NOT NULL COMMENT '是否有主持人',
                        `room_num` int DEFAULT NULL COMMENT '房间人数',
                        `task_id` varchar(255) DEFAULT NULL COMMENT '任务ID',
                        `is_need_middle` tinyint DEFAULT NULL COMMENT '是否存在呆呆鱿（人数为6时考虑）',
                        `result_img` longblob COMMENT '战绩数据',
                        `vote_result` varchar(2048) DEFAULT NULL COMMENT '投票结果',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                        `role_id` varchar(16) NOT NULL DEFAULT '' COMMENT '角色ID',
                        `role_name` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名称',
                        `role_desc` varchar(256) NOT NULL COMMENT '角色介绍',
                        `role_type` varchar(256) NOT NULL COMMENT '角色类型',
                        `create_time` datetime NOT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `role_id`, `role_name`, `role_desc`, `create_time`, `update_time`, `role_type`) VALUES (1, '10000001', '坏鱿', '需完成指定任务', '2024-06-18 10:09:06', '2024-06-18 10:09:06', 'GOOD');
INSERT INTO `role` (`id`, `role_id`, `role_name`, `role_desc`, `create_time`, `update_time`, `role_type`) VALUES (2, '10000010', '好鱿', '找出坏鱿', '2024-06-18 10:09:06', '2024-06-18 10:09:06', 'BAD');
INSERT INTO `role` (`id`, `role_id`, `role_name`, `role_desc`, `create_time`, `update_time`, `role_type`) VALUES (3, '10000011', '呆呆鱿', '假装坏鱿', '2024-06-18 10:09:06', '2024-06-18 10:09:06', 'MIDDLE');

COMMIT;

-- ----------------------------
-- Table structure for game_player
-- ----------------------------
DROP TABLE IF EXISTS `game_player`;
CREATE TABLE `game_player` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                               `room_id` varchar(16) NOT NULL DEFAULT '' COMMENT '游戏房ID',
                               `player_id` varchar(32) NOT NULL DEFAULT '' COMMENT '玩家ID',
                               `voted_player_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '' COMMENT '被投玩家ID',
                               `group_id` varchar(8) DEFAULT NULL COMMENT '分队名称',
                               `role_name` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色名称',
                               `is_creator` tinyint DEFAULT NULL COMMENT '是否为创建者',
                               `is_restart` tinyint DEFAULT NULL COMMENT '是否重新开始',
                               `is_alive` tinyint DEFAULT NULL COMMENT '是否存活',
                               `is_host` tinyint DEFAULT NULL COMMENT '是否为主持人',
                               `is_voted` tinyint DEFAULT NULL COMMENT '是否投票',
                               `is_ready` tinyint DEFAULT NULL COMMENT '是否准备',
                               `is_top` tinyint DEFAULT NULL COMMENT '是否为第一名',
                               `is_complete` tinyint DEFAULT NULL COMMENT '是否完成任务',
                               `create_time` datetime NOT NULL COMMENT '创建时间',
                               `update_time` datetime NOT NULL COMMENT '更新时间',
                               PRIMARY KEY ( `id` ),
                               INDEX `idx_room_id` (`room_id`),
                               INDEX `idx_player_id` (`player_id`),
                               INDEX `idx_room_id_player_id` (`room_id`, `player_id`)
) ENGINE = INNODB AUTO_INCREMENT = 3 DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
                        `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                        `task_id` varchar(16) NOT NULL DEFAULT '' COMMENT '任务id',
                        `type` varchar(8) NOT NULL DEFAULT '' COMMENT '任务类型',
                        `sub_type` varchar(8) NOT NULL DEFAULT '' COMMENT '任务子类型',
                        `level` varchar(8) NOT NULL DEFAULT '' COMMENT '任务级别',
                        `title` varchar(32) NOT NULL DEFAULT '' COMMENT '任务标题',
                        `sub_title` varchar(32) NOT NULL DEFAULT '' COMMENT '任务副标题',
                        `create_name` varchar(32) NOT NULL DEFAULT '' COMMENT '任务提供者姓名',
                        `create_time` datetime NOT NULL COMMENT '创建时间',
                        `update_time` datetime NOT NULL COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (3, '10000001', 'ALL', 'ALL', '1', '杀鱿偿命', '每条命,击杀不能超过1人(助攻不算)', 'Maomao', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (4, '10000002', 'ALL', 'ALL', '1', '蓄势不发', '满槽保持60s不使用大招', 'Maomao', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (5, '10000003', 'ALL', 'ALL', '2', '我来支援', '复活后跳队友并且死于敌方『蹲跳点击杀』5次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (6, '10000004', 'ALL', 'ALL', '2', '按兵不动', '潜伏达25s期间内不可移动', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (7, '10000005', 'ALL', 'ALL', '2', '兼爱非攻', '开局1分钟不可击杀、助攻以及死亡', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (8, '10000006', 'ALL', 'ALL', '3', '终场冲刺', '剩余1分钟累计死亡5次（若加时算在1分钟内）', 'Sola', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (9, '10000007', 'ALL', 'ALL', '3', '认准你了', '本局只可以击杀同一个敌人（其他助攻不算）', 'Qingshui', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (10, '10000008', 'ALL', 'ALL', '3', '副武器大师', '仅适用副武器击杀5人以上，主武器和大招不可杀敌', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (11, '10000009', 'ANARCHY', 'AREA', '1', '养精蓄锐', '开局30s内不可以涂区域', 'Qibai', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (12, '10000010', 'ANARCHY', 'AREA', '1', '中立单位', '区域僵持是不可对区域内进行涂地', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (13, '10000011', 'ANARCHY', 'AREA', '1', '坚守阵地？', '死在区域内4次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (14, '10000012', 'ANARCHY', 'AREA', '1', '点到为止', '我方区域占领时不可击杀敌人', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (15, '10000013', 'ANARCHY', 'AREA', '2', '副武器？主武器！', '副武器击杀区域内敌人3次', 'Xiaoxu', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (16, '10000014', 'ANARCHY', 'AREA', '3', '区域绞肉机', '区域内累积鱿鱼滚行5次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (17, '10000015', 'ANARCHY', 'YUHU', '1', '艺术就是爆炸', '死于鱼护防护罩2次', 'Qibai', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (18, '10000016', 'ANARCHY', 'YUHU', '1', '一个顶俩', '队友拿鱼虎势不可攻击敌方直到鱼虎死亡或放置检查点', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (19, '10000017', 'ANARCHY', 'YUHU', '2', '极致操作', '鱼虎连续鱿鱼滚行2次', 'Qingshui', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (20, '10000018', 'ANARCHY', 'YUHU', '2', '在劫难逃', '死于敌方鱼炮击杀4次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (21, '10000019', 'ANARCHY', 'YUHU', '3', '极度专一', '仅使用鱼虎杀敌其他武器仅用于涂地', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (22, '10000020', 'ANARCHY', 'YUHU', '3', '顶天立地', '拿鱼不潜墨20s', 'Qingshui', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (23, '10000021', 'ANARCHY', 'GELI', '1', '害怕关注', '保存7个蛤蜊不合成持续30s', 'Polo°', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (24, '10000022', 'ANARCHY', 'GELI', '1', '顶级失败', '篮筐下大蛤蜊投掷失败1次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (25, '10000023', 'ANARCHY', 'GELI', '2', '还你自由', '连续丢出场外3个小蛤蜊', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (26, '10000024', 'ANARCHY', 'GELI', '2', '鱿点碍事', '合成大蛤蜊后丢掉，让大蛤蜊自己消失完成3次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (27, '10000025', 'ANARCHY', 'GELI', '3', '吝啬鬼', '整局不可以丢小蛤蜊', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (28, '10000026', 'ANARCHY', 'GELI', '3', '你行你上', '将大蛤蜊投给队友，队友成功捡起3次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (29, '10000027', 'ANARCHY', 'TA', '1', '害怕孤单', '和队友2人一起站塔10s', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (30, '10000028', 'ANARCHY', 'TA', '1', '仰卧起坐', '站塔后1s内下塔累计完成4次', 'Nighter', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (31, '10000029', 'ANARCHY', 'TA', '2', '人在塔在', '在塔上死亡5次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (32, '10000030', 'ANARCHY', 'TA', '2', '自己人别开枪', '不在塔上的情况下，被塔楼上的敌人累计击杀5次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (33, '10000031', 'ANARCHY', 'TA', '3', '恐高症', '站塔次数最多2次', 'Xiaohei', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (34, '10000032', 'ANARCHY', 'TA', '3', '临阵脱逃', '刚达到敌方任意检查点时，下塔累计完成3次', 'Polo°', '2024-06-24 18:14:16', '2024-06-24 18:14:18');
INSERT INTO `task` (`id`, `task_id`, `type`, `sub_type`, `level`, `title`, `sub_title`, `create_name`, `create_time`, `update_time`) VALUES (35, '10000033', 'ALL', 'ALL', '0', '失败乃成功之母', '输掉比赛', '', '2024-06-25 10:08:16', '2024-06-25 10:08:18');

