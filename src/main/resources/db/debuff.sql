/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : debuff

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 16/07/2025 11:29:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adminactions
-- ----------------------------
DROP TABLE IF EXISTS `adminactions`;
CREATE TABLE `adminactions`  (
  `action_id` int NOT NULL AUTO_INCREMENT COMMENT '操作ID，主键，自动递增',
  `admin_id` int NOT NULL COMMENT '管理员ID，外键关联Users表的user_id',
  `action_type` enum('review_deposit_withdraw','manage_order','recommend_item','ban_user','delete_item') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `target_type` enum('user','item','order','transaction') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` int NOT NULL COMMENT '目标ID',
  `details` json NULL COMMENT '详细信息',
  `action_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间，默认为当前时间戳',
  PRIMARY KEY (`action_id`) USING BTREE,
  INDEX `idx_admin_id`(`admin_id` ASC) USING BTREE,
  CONSTRAINT `adminactions_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理员操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auctions
-- ----------------------------
DROP TABLE IF EXISTS `auctions`;
CREATE TABLE `auctions`  (
  `auction_id` int NOT NULL AUTO_INCREMENT COMMENT '拍卖ID，主键',
  `item_id` int NOT NULL COMMENT '商品ID，外键关联Items表的item_id',
  `seller_id` int NOT NULL COMMENT '卖家ID，外键关联Users表的user_id',
  `start_price` decimal(10, 2) NOT NULL COMMENT '起始价格',
  `current_bid` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前最高出价',
  `end_time` datetime NOT NULL COMMENT '拍卖结束时间',
  `status` enum('active','completed','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'active' COMMENT '状态：进行中、已完成、已取消',
  PRIMARY KEY (`auction_id`) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  INDEX `idx_seller_id`(`seller_id` ASC) USING BTREE,
  CONSTRAINT `auctions_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `auctions_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '拍卖信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for conversations
-- ----------------------------
DROP TABLE IF EXISTS `conversations`;
CREATE TABLE `conversations`  (
  `conversation_id` int NOT NULL AUTO_INCREMENT COMMENT '会话ID，主键',
  `user1_id` int NOT NULL COMMENT '参与者1',
  `user2_id` int NOT NULL COMMENT '参与者2',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `unread_count` int NULL DEFAULT 0 COMMENT '未读消息总数',
  `last_message_id` int NULL DEFAULT NULL COMMENT '最后一条消息',
  `conversation_type` enum('private','group','customer_service') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'private' COMMENT '会话类型',
  `status` enum('active','archived','blocked') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'active' COMMENT '会话状态',
  PRIMARY KEY (`conversation_id`) USING BTREE,
  INDEX `idx_user1_id`(`user1_id` ASC) USING BTREE,
  INDEX `idx_user2_id`(`user2_id` ASC) USING BTREE,
  INDEX `last_message_id`(`last_message_id` ASC) USING BTREE,
  CONSTRAINT `conversations_ibfk_1` FOREIGN KEY (`user1_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `conversations_ibfk_2` FOREIGN KEY (`user2_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `conversations_ibfk_3` FOREIGN KEY (`last_message_id`) REFERENCES `messages` (`message_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for favorites
-- ----------------------------
DROP TABLE IF EXISTS `favorites`;
CREATE TABLE `favorites`  (
  `favorite_id` int NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键，自动递增',
  `user_id` int NOT NULL COMMENT '用户ID，外键关联Users表的user_id',
  `item_id` int NOT NULL COMMENT '商品ID，外键关联Items表的item_id',
  `add_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间，默认为当前时间戳',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  CONSTRAINT `favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `favorites_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for feedbacks
-- ----------------------------
DROP TABLE IF EXISTS `feedbacks`;
CREATE TABLE `feedbacks`  (
  `feedback_id` int NOT NULL AUTO_INCREMENT COMMENT '反馈ID，主键',
  `user_id` int NOT NULL COMMENT '用户ID，外键关联Users表的user_id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间戳',
  PRIMARY KEY (`feedback_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `feedbacks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户反馈信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for identityverification
-- ----------------------------
DROP TABLE IF EXISTS `identityverification`;
CREATE TABLE `identityverification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自动递增',
  `user_id` int NOT NULL COMMENT '外键，关联到用户表中的`user_id`',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号或其他身份证明编号（加密存储）',
  `verification_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '其他可能的验证资料',
  `verified_at` datetime NULL DEFAULT NULL COMMENT '认证通过的时间',
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '审核状态，默认是待审核',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `identityverification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实名认证信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for items
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items`  (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键，自动递增',
  `seller_id` int NOT NULL COMMENT '卖家ID，外键关联Users表的user_id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品描述',
  `category_id` int NULL DEFAULT NULL COMMENT '商品分类ID，外键关联Categories表的category_id',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `wear_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '磨损值（可为空）',
  `cool_down` datetime NULL DEFAULT NULL COMMENT '冷却期结束时间',
  `post_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间，默认为当前时间戳',
  `is_sold` tinyint(1) NULL DEFAULT 0 COMMENT '是否已售出，默认否',
  `status` enum('on_sale','sold_out','removed') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'on_sale' COMMENT '商品状态：在售、已售完、已移除，默认在售',
  PRIMARY KEY (`item_id`) USING BTREE,
  INDEX `idx_seller_id`(`seller_id` ASC) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  CONSTRAINT `items_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `items_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `message_id` int NOT NULL AUTO_INCREMENT COMMENT '消息ID，主键，自动递增',
  `conversation_id` int NOT NULL COMMENT '会话ID，外键关联Conversations表',
  `from_user_id` int NOT NULL COMMENT '发送者ID，外键关联Users表的user_id',
  `reply_to_id` int NULL DEFAULT NULL COMMENT '回复的消息ID，外键关联本表message_id',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `message_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `message_type` enum('text','image','video','system','order') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'text' COMMENT '消息内容类型',
  `status` enum('sent','delivered','read','failed') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'sent' COMMENT '消息状态',
  `item_id` int NULL DEFAULT NULL COMMENT '关联商品ID',
  `extra_data` json NULL COMMENT '扩展数据(位置、商品快照等)',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_conversation_id`(`conversation_id` ASC) USING BTREE,
  INDEX `idx_from_user_id`(`from_user_id` ASC) USING BTREE,
  INDEX `idx_reply_to_id`(`reply_to_id` ASC) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`conversation_id`) REFERENCES `conversations` (`conversation_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `messages_ibfk_3` FOREIGN KEY (`reply_to_id`) REFERENCES `messages` (`message_id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `messages_ibfk_4` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `notification_id` int NOT NULL AUTO_INCREMENT COMMENT '通知ID，主键，自动递增',
  `user_id` int NOT NULL COMMENT '用户ID，外键关联Users表的user_id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `send_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间，默认为当前时间戳',
  `is_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已读，默认否',
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID，主键',
  `buyer_id` int NOT NULL COMMENT '买家ID，外键关联Users表的user_id',
  `item_id` int NOT NULL COMMENT '商品ID，外键关联Items表的item_id',
  `purchase_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间，默认为当前时间戳',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `payment_method` enum('balance','alipay','wechat') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'balance' COMMENT '支付方式：余额、支付宝、微信，默认余额',
  `status` enum('presale','pending','paid','shipped','completed','canceled') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'presale' COMMENT '订单状态：预售、待处理、已支付、已发货、已完成、已取消，默认预售',
  `expected_delivery_time` datetime NULL DEFAULT NULL COMMENT '预计发货时间，用于预售商品',
  `delivery_info` json NULL COMMENT '配送信息',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `idx_buyer_id`(`buyer_id` ASC) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rentals
-- ----------------------------
DROP TABLE IF EXISTS `rentals`;
CREATE TABLE `rentals`  (
  `rental_id` int NOT NULL AUTO_INCREMENT COMMENT '租赁ID，主键',
  `item_id` int NOT NULL COMMENT '商品ID，外键关联Items表的item_id',
  `renter_id` int NOT NULL COMMENT '租用者ID，外键关联Users表的user_id',
  `owner_id` int NOT NULL COMMENT '所有者ID，外键关联Users表的user_id',
  `rental_price` decimal(10, 2) NOT NULL COMMENT '租金',
  `start_date` date NOT NULL COMMENT '租赁开始日期',
  `end_date` date NOT NULL COMMENT '租赁结束日期',
  `status` enum('active','completed','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'active' COMMENT '状态：进行中、已完成、已取消',
  PRIMARY KEY (`rental_id`) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  INDEX `idx_renter_id`(`renter_id` ASC) USING BTREE,
  INDEX `idx_owner_id`(`owner_id` ASC) USING BTREE,
  CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `rentals_ibfk_2` FOREIGN KEY (`renter_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `rentals_ibfk_3` FOREIGN KEY (`owner_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tradeconfirmations
-- ----------------------------
DROP TABLE IF EXISTS `tradeconfirmations`;
CREATE TABLE `tradeconfirmations`  (
  `confirmation_id` int NOT NULL AUTO_INCREMENT COMMENT '确认ID，主键',
  `order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单ID，外键关联Orders表的order_id',
  `buyer_id` int NOT NULL COMMENT '买家ID，外键关联Users表的user_id',
  `seller_id` int NOT NULL COMMENT '卖家ID，外键关联Users表的user_id',
  `payment_confirmed` tinyint(1) NULL DEFAULT 0 COMMENT '支付是否确认，默认否',
  `shipping_confirmed` tinyint(1) NULL DEFAULT 0 COMMENT '发货是否确认，默认否',
  `trade_completed` tinyint(1) NULL DEFAULT 0 COMMENT '交易是否完成，默认否',
  PRIMARY KEY (`confirmation_id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_buyer_id`(`buyer_id` ASC) USING BTREE,
  INDEX `idx_seller_id`(`seller_id` ASC) USING BTREE,
  CONSTRAINT `tradeconfirmations_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `tradeconfirmations_ibfk_2` FOREIGN KEY (`buyer_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `tradeconfirmations_ibfk_3` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易确认表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions`  (
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易ID，主键',
  `user_id` int NOT NULL COMMENT '用户ID，外键关联Users表的user_id',
  `type` enum('deposit','withdraw') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型：充值、提现',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `method` enum('balance','alipay','wechat','bank') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'balance' COMMENT '方法：余额、支付宝、微信、银行，默认余额',
  `status` enum('success','failed','processing','reviewing') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'processing' COMMENT '状态：成功、失败、处理中、审核中，默认处理中',
  `detail` json NULL COMMENT '详细信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间戳',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，默认为当前时间戳',
  PRIMARY KEY (`transaction_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '交易记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_ban_history
-- ----------------------------
DROP TABLE IF EXISTS `user_ban_history`;
CREATE TABLE `user_ban_history`  (
  `history_id` int NOT NULL AUTO_INCREMENT COMMENT '唯一标识一条封禁历史记录',
  `user_id` int NOT NULL COMMENT '与被封禁用户的用户ID相关联',
  `ban_start` datetime NOT NULL COMMENT '封禁的开始时间',
  `ban_end` datetime NOT NULL COMMENT '封禁的预计结束时间',
  `unban_time` datetime NULL DEFAULT NULL COMMENT '实际解封的时间，若未解封则为NULL',
  `unban_type` tinyint NULL DEFAULT 0 COMMENT '解封的方式，0-未解封, 1-自动解封, 2-管理员解封',
  `admin_unban_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '如果是由管理员手动解封，则需要填写解封的原因',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '封禁的原因说明',
  PRIMARY KEY (`history_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_ban_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '记录用户的封禁历史信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for userinventory
-- ----------------------------
DROP TABLE IF EXISTS `userinventory`;
CREATE TABLE `userinventory`  (
  `inventory_id` int NOT NULL AUTO_INCREMENT COMMENT '库存ID，主键，自动递增',
  `user_id` int NOT NULL COMMENT '用户ID，外键关联Users表的user_id',
  `item_id` int NOT NULL COMMENT '商品ID，外键关联Items表的item_id',
  `quantity` int NULL DEFAULT 1 COMMENT '数量，默认为1',
  `acquisition_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间，默认为当前时间戳',
  `status` enum('available','on_sale','in_transaction','deleted') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'available' COMMENT '状态：可用、在售、交易中、已删除，默认可用',
  PRIMARY KEY (`inventory_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_item_id`(`item_id` ASC) USING BTREE,
  CONSTRAINT `userinventory_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `userinventory_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户库存信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自动递增',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱，唯一',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号码',
  `password_hash` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希值',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` enum('male','female','other') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'other' COMMENT '性别，默认其他',
  `birth_date` date NULL DEFAULT NULL COMMENT '出生日期',
  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '自我描述，用户简介信息',
  `steam_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Steam账户的唯一标识符',
  `api_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户提供的Steam API密钥',
  `trade_link` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户的Steam交易链接',
  `is_verified` tinyint(1) NULL DEFAULT 0 COMMENT '实名认证状态，默认未认证',
  `register_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间，默认为当前时间戳',
  `role` enum('user','admin') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'user' COMMENT '角色：用户或管理员，默认用户',
  `status` enum('active','banned','pending') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '状态：激活、禁用、待定，默认待定',
  `ban_end` datetime NULL DEFAULT NULL COMMENT '封禁结束时间',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '账户余额，默认0.00',
  `last_login` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for verification_code
-- ----------------------------
DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE `verification_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接收验证码的邮箱地址',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime NOT NULL COMMENT '验证码过期时间',
  `create_time` datetime NOT NULL COMMENT '记录创建时间',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '邮箱验证码记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Event structure for unban_users_event
-- ----------------------------
DROP EVENT IF EXISTS `unban_users_event`;
delimiter ;;
CREATE EVENT `unban_users_event`
ON SCHEDULE
EVERY '1' DAY STARTS '2025-07-08 14:35:50'
COMMENT '自动解除那些禁令期限已到或已过的用户的禁令状态。'
DO BEGIN
    /*
     * 更新Users表中的记录：
     * - 将status字段更新为'active'，表示用户恢复正常活动。
     * - 清除ban_end字段值，表示该用户不再处于任何禁令之下。
     * 
     * 条件：
     * 1. 用户当前的状态是'banned'；
     * 2. ban_end字段不为空，意味着用户有一个设定的解禁时间；
     * 3. 当前时间大于等于ban_end所指定的时间，表明用户的禁令期已经结束。
     */
    UPDATE Users
    SET status = 'active', -- 更新用户状态为'active'
        ban_end = NULL     -- 清除用户的ban_end日期
    WHERE status = 'banned' -- 只更新被标记为'banned'的用户
      AND ban_end IS NOT NULL -- 确保只有设置了ban_end日期的用户才被考虑
      AND CURRENT_TIMESTAMP >= ban_end; -- 仅更新那些禁令期已满的用户
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
