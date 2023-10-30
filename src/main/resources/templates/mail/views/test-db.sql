-- profileshop.`action` definition

CREATE TABLE `action` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `object` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `state` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                          `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `comment` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `object_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `last_modified_date` datetime DEFAULT NULL,
                          `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `action_object_id_IDX` (`object_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1324098 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.carrier definition

CREATE TABLE `carrier` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `ref` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `max_weight` decimal(21,2) DEFAULT NULL,
                           `api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `rate_table` json DEFAULT NULL,
                           `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.carrier_zone definition

CREATE TABLE `carrier_zone` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `code` varchar(10) NOT NULL,
                                `name` varchar(128) NOT NULL,
                                `active` tinyint(1) NOT NULL DEFAULT '1',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `carrier_zone_un` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;


-- profileshop.carrier_zone_rate definition

CREATE TABLE `carrier_zone_rate` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `carrier_ref` varchar(128) NOT NULL,
                                     `rate_name` varchar(128) NOT NULL,
                                     `carrier_zone_code` varchar(10) NOT NULL,
                                     `price` varchar(10) NOT NULL,
                                     `condition_min` varchar(10) DEFAULT NULL,
                                     `condition_max` varchar(10) DEFAULT NULL,
                                     `condition_type` varchar(10) DEFAULT NULL,
                                     `handling_fee` json DEFAULT NULL,
                                     `active` tinyint(1) NOT NULL DEFAULT '1',
                                     `is_free` tinyint(1) NOT NULL DEFAULT '0',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;


-- profileshop.carrier_zone_zone definition

CREATE TABLE `carrier_zone_zone` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `carrier_zone_code` varchar(10) NOT NULL,
                                     `zone_code` varchar(10) NOT NULL,
                                     `active` tinyint(1) NOT NULL DEFAULT '1',
                                     PRIMARY KEY (`id`),
                                     KEY `carrier_zone_zone_zone_code_IDX` (`zone_code`) USING BTREE,
                                     KEY `carrier_zone_zone_carrier_zone_code_IDX` (`carrier_zone_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;


-- profileshop.cart definition

CREATE TABLE `cart` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `secure_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                        `gift` bit(1) DEFAULT NULL,
                        `gift_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `cart_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `customer_id` bigint DEFAULT NULL,
                        `currency_id` bigint DEFAULT NULL,
                        `carrier_id` bigint DEFAULT NULL,
                        `delivery_address_id` bigint DEFAULT NULL,
                        `invoice_address_id` bigint DEFAULT NULL,
                        `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `ux_cart_secure_key` (`secure_key`)
) ENGINE=InnoDB AUTO_INCREMENT=14836 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.cart_item definition

CREATE TABLE `cart_item` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `quantity` int DEFAULT NULL,
                             `cart_id` bigint DEFAULT NULL,
                             `product_id` bigint NOT NULL,
                             `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `fk_cart_item_cart_id` (`cart_id`),
                             KEY `fk_cart_item_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46436 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.category_product definition

CREATE TABLE `category_product` (
                                    `product_id` bigint NOT NULL,
                                    `category_id` bigint NOT NULL,
                                    PRIMARY KEY (`category_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.checkout definition

CREATE TABLE `checkout` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `ref` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `secure_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `delivery_address_id` int DEFAULT NULL,
                            `delivery_address` json DEFAULT NULL,
                            `invoice_address` json DEFAULT NULL,
                            `addresses` json DEFAULT NULL,
                            `payment_methods` json DEFAULT NULL,
                            `carrier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `currency` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `checkout_content` json DEFAULT NULL,
                            `payment_token` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `checked_out` tinyint(1) NOT NULL DEFAULT '0',
                            `_lock` tinyint(1) NOT NULL DEFAULT '0',
                            `allow_pickup` tinyint(1) DEFAULT '0',
                            `payment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `cart_weight` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `guest` tinyint(1) DEFAULT NULL,
                            `carrier_rate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `carrier_service` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `adjustments` json DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `checkout_secure_key_IDX` (`secure_key`) USING BTREE,
                            KEY `checkout_payment_token_IDX` (`payment_token`) USING BTREE,
                            KEY `checkout_tenant_id_IDX` (`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7852 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.`element` definition

CREATE TABLE `element` (
                           `id` int NOT NULL,
                           `type` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `content` json DEFAULT NULL,
                           `active` smallint DEFAULT NULL,
                           `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.hashtag definition

CREATE TABLE `hashtag` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `icon` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `lang` json NOT NULL,
                           `position` int DEFAULT NULL,
                           `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
                           `last_modified_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `created_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `last_modified_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_authority definition

CREATE TABLE `jhi_authority` (
                                 `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                 PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_order definition

CREATE TABLE `jhi_order` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `reference` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `invoice_date` date DEFAULT NULL,
                             `delivery_date` date DEFAULT NULL,
                             `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `currency` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `customer_id` bigint DEFAULT NULL,
                             `cart_id` bigint DEFAULT NULL,
                             `delivery_address_id` bigint DEFAULT NULL,
                             `invoice_address_id` bigint DEFAULT NULL,
                             `delivery_address` json DEFAULT NULL,
                             `invoice_address` json DEFAULT NULL,
                             `carrier_id` int DEFAULT NULL,
                             `confirmation_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `subtotal` decimal(10,4) DEFAULT NULL,
                             `total` decimal(10,4) DEFAULT NULL,
                             `delivery_total` decimal(10,4) DEFAULT NULL,
                             `taxes_total` decimal(10,4) DEFAULT NULL,
                             `discounts_total` decimal(10,4) DEFAULT NULL,
                             `coupon_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `created_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `carrier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `items` json DEFAULT NULL,
                             `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `adjustments` json DEFAULT NULL,
                             `email_sent` tinyint(1) NOT NULL DEFAULT '0',
                             PRIMARY KEY (`id`),
                             KEY `reference` (`reference`),
                             KEY `jhi_order_tenant_id_IDX` (`tenant_id`) USING BTREE,
                             KEY `jhi_order_state_IDX` (`state`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=104251 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_persistent_audit_event definition

CREATE TABLE `jhi_persistent_audit_event` (
                                              `event_id` bigint NOT NULL AUTO_INCREMENT,
                                              `principal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                              `event_date` timestamp NULL DEFAULT NULL,
                                              `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                              PRIMARY KEY (`event_id`),
                                              KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB AUTO_INCREMENT=199087 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_tenant_authority definition

CREATE TABLE `jhi_tenant_authority` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `user_id` bigint NOT NULL,
                                        `authority_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `jhi_tenant_authority_UN` (`user_id`,`authority_name`,`tenant_id`),
                                        KEY `fk_authority_name` (`authority_name`),
                                        KEY `jhi_tenant_authority_tenant_id_IDX` (`tenant_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_user definition

CREATE TABLE `jhi_user` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `login` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `password_hash` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `first_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `last_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `image_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `activated` bit(1) NOT NULL,
                            `lang_key` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `activation_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `reset_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `created_date` timestamp NULL DEFAULT NULL,
                            `reset_date` timestamp NULL DEFAULT NULL,
                            `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `last_modified_date` timestamp NULL DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_user_login` (`login`),
                            UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.media definition

CREATE TABLE `media` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `file_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.merchant definition

CREATE TABLE `merchant` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `added` datetime DEFAULT NULL,
                            `is_plus` tinyint(1) DEFAULT NULL,
                            `domain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `merchant_UN` (`name`),
                            UNIQUE KEY `merchant_UN_domain` (`domain`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.order_item definition

CREATE TABLE `order_item` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `quantity` int DEFAULT NULL,
                              `price` decimal(10,2) DEFAULT NULL,
                              `cost` decimal(10,2) DEFAULT NULL,
                              `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `sequence` int DEFAULT NULL,
                              `shipping_instructions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `order_id` bigint NOT NULL,
                              `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `weight` decimal(5,2) DEFAULT NULL,
                              `line_total` decimal(10,2) DEFAULT NULL,
                              `url` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `product_id` varchar(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              `availability` int DEFAULT NULL,
                              `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `fk_order_item_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=313352 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.payment definition

CREATE TABLE `payment` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `payment_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `auth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `card_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `amount` decimal(21,2) NOT NULL,
                           `transaction_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `order_id` bigint DEFAULT NULL,
                           `last_modified_date` datetime DEFAULT NULL,
                           `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `bank_account_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `bank_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `bank_owner_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `customer_id` bigint DEFAULT NULL,
                           `ref` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `processed_date` datetime DEFAULT NULL,
                           `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `invoice_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `settlement_date` datetime DEFAULT NULL,
                           `track_id` bigint DEFAULT NULL,
                           `capture_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `void` tinyint(1) NOT NULL DEFAULT '0',
                           `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `currency` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'OMR',
                           PRIMARY KEY (`id`),
                           KEY `fk_payment_order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36923 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.payment_method definition

CREATE TABLE `payment_method` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `ref` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `name` json DEFAULT NULL,
                                  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `config` json DEFAULT NULL,
                                  `tenant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.point_customer definition

CREATE TABLE `point_customer` (
                                  `id` int unsigned NOT NULL,
                                  `customer_id` int unsigned DEFAULT NULL,
                                  `total_points` int unsigned DEFAULT NULL,
                                  `spent_points` int unsigned DEFAULT NULL,
                                  `referral_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                  `spent_money` decimal(17,2) unsigned DEFAULT NULL,
                                  `active` tinyint unsigned DEFAULT NULL,
                                  `id_shop` int unsigned DEFAULT NULL,
                                  `date_add` datetime NOT NULL,
                                  `date_upd` datetime NOT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.product definition

CREATE TABLE `product` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `ref` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `slug` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `sku` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `upc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `list_price` json DEFAULT NULL,
                           `price` json DEFAULT NULL,
                           `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `unit` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `release_date` date DEFAULT NULL,
                           `active` bit(1) NOT NULL,
                           `similar_products` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `jhi_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `last_modified_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `jhi_condition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `jhi_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `is_used` bit(1) DEFAULT NULL,
                           `available_for_order` bit(1) DEFAULT NULL,
                           `weight` decimal(21,2) DEFAULT NULL,
                           `volume_weight` decimal(21,2) DEFAULT NULL,
                           `variation_options` json DEFAULT NULL,
                           `variation_dimensions` json DEFAULT NULL,
                           `variation_attributes` json DEFAULT NULL,
                           `variations` json DEFAULT NULL,
                           `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `merchant_id` int DEFAULT NULL COMMENT 'supplier_id',
                           `expires` datetime DEFAULT NULL,
                           `in_stock` tinyint(1) DEFAULT '1',
                           `stub` tinyint(1) DEFAULT NULL,
                           `index` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `hashtags` json DEFAULT NULL,
                           `rating` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `api` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `pricing_api` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `deleted` tinyint(1) DEFAULT NULL,
                           `oversize` tinyint(1) DEFAULT '0',
                           `description` json DEFAULT NULL,
                           `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `attributes` json DEFAULT NULL,
                           `delivery_profiles` json DEFAULT NULL,
                           `url` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `product_un` (`ref`),
                           UNIQUE KEY `product_no_duplicate_sku` (`sku`,`merchant_id`,`tenant_id`),
                           KEY `sku` (`sku`),
                           KEY `ref` (`ref`),
                           KEY `slug` (`slug`),
                           KEY `product_merchant_id_IDX` (`merchant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10015687 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.product_override definition

CREATE TABLE `product_override` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `sku` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `override` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                    `active` bit(1) NOT NULL,
                                    `lazy` bit(1) NOT NULL,
                                    `created_date` datetime DEFAULT NULL,
                                    `last_modified_date` datetime DEFAULT NULL,
                                    `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                    `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `fk_product_override_created_by_id` (`created_by`),
                                    KEY `fk_product_override_last_modified_by_id` (`last_modified_by`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.ps_address definition

CREATE TABLE `ps_address` (
                              `id_address` int unsigned NOT NULL AUTO_INCREMENT,
                              `id_country` int unsigned NOT NULL,
                              `id_state` int unsigned DEFAULT NULL,
                              `id_customer` int unsigned NOT NULL DEFAULT '0',
                              `id_manufacturer` int unsigned NOT NULL DEFAULT '0',
                              `id_supplier` int unsigned NOT NULL DEFAULT '0',
                              `id_warehouse` int unsigned NOT NULL DEFAULT '0',
                              `alias` varchar(155) NOT NULL,
                              `company` varchar(255) DEFAULT NULL,
                              `lastname` varchar(255) NOT NULL,
                              `firstname` varchar(255) NOT NULL,
                              `address1` varchar(128) NOT NULL,
                              `address2` varchar(128) DEFAULT NULL,
                              `postcode` varchar(12) DEFAULT NULL,
                              `city` varchar(64) NOT NULL,
                              `other` text,
                              `phone` varchar(32) DEFAULT NULL,
                              `phone_mobile` varchar(32) DEFAULT NULL,
                              `vat_number` varchar(32) DEFAULT NULL,
                              `dni` varchar(16) DEFAULT NULL,
                              `date_add` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `date_upd` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              `active` tinyint unsigned NOT NULL DEFAULT '1',
                              `deleted` tinyint unsigned NOT NULL DEFAULT '0',
                              `plus_code` varchar(100) DEFAULT NULL,
                              `country` varchar(4) DEFAULT NULL,
                              `state` varchar(6) DEFAULT NULL,
                              `lat` varchar(100) DEFAULT NULL,
                              `lng` varchar(100) DEFAULT NULL,
                              PRIMARY KEY (`id_address`),
                              KEY `address_customer` (`id_customer`),
                              KEY `id_country` (`id_country`),
                              KEY `id_state` (`id_state`),
                              KEY `id_manufacturer` (`id_manufacturer`),
                              KEY `id_supplier` (`id_supplier`),
                              KEY `id_warehouse` (`id_warehouse`),
                              KEY `id_address` (`id_address`)
) ENGINE=InnoDB AUTO_INCREMENT=23245 DEFAULT CHARSET=utf8mb3;


-- profileshop.ps_country definition

CREATE TABLE `ps_country` (
                              `id_country` int unsigned NOT NULL AUTO_INCREMENT,
                              `id_zone` int unsigned NOT NULL,
                              `id_currency` int unsigned NOT NULL DEFAULT '0',
                              `iso_code` varchar(3) NOT NULL,
                              `call_prefix` int NOT NULL DEFAULT '0',
                              `active` tinyint unsigned NOT NULL DEFAULT '0',
                              `contains_states` tinyint(1) NOT NULL DEFAULT '0',
                              `need_identification_number` tinyint(1) NOT NULL DEFAULT '0',
                              `need_zip_code` tinyint(1) NOT NULL DEFAULT '1',
                              `zip_code_format` varchar(12) NOT NULL DEFAULT '',
                              `display_tax_label` tinyint(1) NOT NULL,
                              PRIMARY KEY (`id_country`),
                              KEY `country_iso_code` (`iso_code`),
                              KEY `country_` (`id_zone`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8mb3;


-- profileshop.ps_customer definition

CREATE TABLE `ps_customer` (
                               `id_customer` int unsigned NOT NULL AUTO_INCREMENT,
                               `id_shop_group` int unsigned NOT NULL DEFAULT '1',
                               `id_shop` int unsigned NOT NULL DEFAULT '1',
                               `id_gender` int unsigned DEFAULT NULL,
                               `id_default_group` int unsigned NOT NULL DEFAULT '1',
                               `id_lang` int unsigned DEFAULT NULL,
                               `id_risk` int unsigned NOT NULL DEFAULT '1',
                               `company` varchar(64) DEFAULT NULL,
                               `siret` varchar(14) DEFAULT NULL,
                               `ape` varchar(5) DEFAULT NULL,
                               `firstname` varchar(255) NOT NULL,
                               `lastname` varchar(255) NOT NULL,
                               `email` varchar(128) NOT NULL,
                               `passwd` varchar(60) NOT NULL,
                               `last_passwd_gen` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `birthday` date DEFAULT NULL,
                               `newsletter` tinyint unsigned NOT NULL DEFAULT '0',
                               `ip_registration_newsletter` varchar(15) DEFAULT NULL,
                               `newsletter_date_add` datetime DEFAULT NULL,
                               `optin` tinyint unsigned NOT NULL DEFAULT '0',
                               `website` varchar(128) DEFAULT NULL,
                               `outstanding_allow_amount` decimal(20,6) NOT NULL DEFAULT '0.000000',
                               `show_public_prices` tinyint unsigned NOT NULL DEFAULT '0',
                               `max_payment_days` int unsigned NOT NULL DEFAULT '60',
                               `secure_key` varchar(32) DEFAULT '-1',
                               `note` text,
                               `active` tinyint unsigned NOT NULL DEFAULT '0',
                               `is_guest` tinyint(1) NOT NULL DEFAULT '0',
                               `deleted` tinyint(1) NOT NULL DEFAULT '0',
                               `date_add` datetime DEFAULT CURRENT_TIMESTAMP,
                               `date_upd` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               `reset_password_token` varchar(40) DEFAULT NULL,
                               `reset_password_validity` datetime DEFAULT NULL,
                               `oc_salt` varchar(50) DEFAULT NULL,
                               `mobile` varchar(15) DEFAULT NULL,
                               `allow_pickup` tinyint(1) DEFAULT NULL,
                               `plus_discount` varchar(2) DEFAULT '0' COMMENT 'discount percentage (%)',
                               `shipper_markup` varchar(2) DEFAULT '0',
                               `tenant_id` varchar(100) DEFAULT NULL,
                               PRIMARY KEY (`id_customer`),
                               KEY `customer_email` (`email`),
                               KEY `customer_login` (`email`,`passwd`),
                               KEY `id_customer_passwd` (`id_customer`,`passwd`),
                               KEY `id_gender` (`id_gender`),
                               KEY `id_shop_group` (`id_shop_group`),
                               KEY `id_shop` (`id_shop`,`date_add`)
) ENGINE=InnoDB AUTO_INCREMENT=100592 DEFAULT CHARSET=utf8mb3;


-- profileshop.purchase definition

CREATE TABLE `purchase` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `ref` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `shipping_instructions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `invoice_date` date DEFAULT NULL,
                            `estimated_delivery_date` date DEFAULT NULL,
                            `order_date` date DEFAULT NULL,
                            `subtotal` decimal(21,2) DEFAULT NULL,
                            `delivery_total` decimal(21,2) DEFAULT NULL,
                            `taxes_total` decimal(21,2) DEFAULT NULL,
                            `discount_total` decimal(21,2) DEFAULT NULL,
                            `total` decimal(21,2) DEFAULT NULL,
                            `delivery_address_id` bigint DEFAULT NULL,
                            `invoice_address_id` bigint DEFAULT NULL,
                            `merchant_id` bigint NOT NULL,
                            `order_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `created_date` datetime DEFAULT NULL,
                            `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `last_modified_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `last_modified_date` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_purchase_delivery_address_id` (`delivery_address_id`),
                            UNIQUE KEY `ux_purchase_invoice_address_id` (`invoice_address_id`),
                            KEY `fk_purchase_merchant_id` (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=112836 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.purchase_item definition

CREATE TABLE `purchase_item` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `sequence` int DEFAULT NULL,
                                 `quantity` decimal(21,2) DEFAULT NULL,
                                 `price` decimal(21,2) DEFAULT NULL,
                                 `estimated_delivery_date` date DEFAULT NULL,
                                 `shipping_instructions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                 `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                 `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                 `purchase_id` bigint NOT NULL,
                                 `product_id` bigint DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `fk_purchase_item_purchase_id` (`purchase_id`)
) ENGINE=InnoDB AUTO_INCREMENT=388383 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.purchase_item_order_item definition

CREATE TABLE `purchase_item_order_item` (
                                            `order_item_id` bigint NOT NULL,
                                            `purchase_item_id` bigint NOT NULL,
                                            PRIMARY KEY (`purchase_item_id`,`order_item_id`),
                                            KEY `fk_purchase_item_order_item_order_item_id` (`order_item_id`),
                                            KEY `purchase_item_id` (`purchase_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.s3_upload_request definition

CREATE TABLE `s3_upload_request` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `object_key` varchar(128) NOT NULL,
                                     `url` varchar(256) NOT NULL,
                                     `tenant_id` varchar(20) NOT NULL,
                                     `created_by` varchar(100) DEFAULT NULL,
                                     `last_modified_by` varchar(100) DEFAULT NULL,
                                     `created_date` datetime DEFAULT NULL,
                                     `last_modified_date` datetime DEFAULT NULL,
                                     `asset_type` varchar(100) DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb3;


-- profileshop.stock definition

CREATE TABLE `stock` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `quantity` decimal(21,2) NOT NULL,
                         `cost` json DEFAULT NULL,
                         `availability` int NOT NULL,
                         `allow_backorder` bit(1) NOT NULL,
                         `backorder_availability` int DEFAULT NULL,
                         `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `store` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `product_id` bigint NOT NULL,
                         `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         `price` json DEFAULT NULL,
                         `link` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `stock_product_id` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.tenant definition

CREATE TABLE `tenant` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `tenant_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `payment_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                          `carrier_profile` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                          `plan` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `is_stock` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `active` tinyint(1) NOT NULL,
                          `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `mobile_logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `google_ads` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `is_profile_auth` bit(1) DEFAULT NULL,
                          `max_products` int NOT NULL DEFAULT '25',
                          `sku_prefix` bigint DEFAULT NULL,
                          `plan_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `monthly_fee` decimal(11,2) DEFAULT NULL,
                          `discount_rate` int DEFAULT NULL,
                          `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                          `is_subdomain` bit(1) NOT NULL DEFAULT b'1',
                          `subdomain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `customDomain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `sliders` json DEFAULT NULL,
                          `social` json DEFAULT NULL,
                          `default_locale` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                          `reply_to_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          `search_engine_key` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `tenant_UN` (`tenant_id`),
                          UNIQUE KEY `tenant_UN2` (`subdomain`),
                          UNIQUE KEY `tenant_UN3` (`customDomain`)
) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.`zone` definition

CREATE TABLE `zone` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `code` varchar(10) NOT NULL,
                        `name` varchar(128) NOT NULL,
                        `part_of` varchar(10) DEFAULT NULL,
                        `level` tinyint DEFAULT NULL,
                        `active` tinyint(1) NOT NULL DEFAULT '1',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `zone_un` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8mb3;


-- profileshop.category definition

CREATE TABLE `category` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `slug` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `jhi_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `parent_id` bigint DEFAULT NULL,
                            `tenant_id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `fk_category_parent_id` (`parent_id`),
                            CONSTRAINT `fk_category_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_persistent_audit_evt_data definition

CREATE TABLE `jhi_persistent_audit_evt_data` (
                                                 `event_id` bigint NOT NULL,
                                                 `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                 `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                                                 PRIMARY KEY (`event_id`,`name`),
                                                 KEY `idx_persistent_audit_evt_data` (`event_id`),
                                                 CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.jhi_user_authority definition

CREATE TABLE `jhi_user_authority` (
                                      `user_id` bigint NOT NULL,
                                      `authority_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                      PRIMARY KEY (`user_id`,`authority_name`),
                                      KEY `fk_authority_name` (`authority_name`),
                                      CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- profileshop.slug definition

CREATE TABLE `slug` (
                        `id` bigint NOT NULL,
                        `sku` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                        `merchant_id` bigint NOT NULL,
                        `slug` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `slug_FK` (`merchant_id`),
                        CONSTRAINT `slug_FK` FOREIGN KEY (`merchant_id`) REFERENCES `merchant` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;