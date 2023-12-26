CREATE DATABASE IF NOT EXISTS `POS` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `POS`;

CREATE TABLE `users` (
                         `user_id` int PRIMARY KEY AUTO_INCREMENT,
                         `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                         `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                         `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                         `fullname` nvarchar(255) COLLATE utf8_unicode_ci NOT NULL,
                         `is_admin` boolean NOT NULL,
                         `profile_picture` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'user.jpg',
                         `is_locked` boolean,
                         `first_login` boolean DEFAULT true,
                         `phone` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
                         `token` varchar(255) COLLATE utf8_unicode_ci,
                         UNIQUE(`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `customers` (
                             `id` int PRIMARY KEY AUTO_INCREMENT,
                             `phone_number` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
                             `fullname` nvarchar(50) COLLATE utf8_unicode_ci NOT NULL,
                             `address` nvarchar(50) COLLATE utf8_unicode_ci ,
                             UNIQUE(`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `sales` (
                         `sale_id` varchar(20) PRIMARY KEY,
                         `total_quantity` int NOT NULL,
                         `total_price` int NOT NULL,
                         `amount_given_by_customer` int NOT NULL,
                         `change_to_customer` int NOT NULL,
                         `user_id` int,
                         `customer_id` int,
                         `sale_date` date,
                         `sale_time` time,
                         FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `products` (
                            `product_id` int PRIMARY KEY AUTO_INCREMENT,
                            `product_name` nvarchar(255) COLLATE utf8_unicode_ci NOT NULL,
                            `import_price` int,
                            `retail_price` int,
                            `manufacturer` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                            `category` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                            `image` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
                            `creation_date` date,
                            `quantity_in_stock` int unsigned not null

) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE `sale_details` (
                                `sale_detail_id` int PRIMARY KEY AUTO_INCREMENT,
                                `quantity` int,
                                `unit_price` int,
                                `sale_id` varchar(20),
                                `product_id` int,
                                FOREIGN KEY (`sale_id`) REFERENCES `sales`(`sale_id`),
                                FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Password of admin is admin
INSERT INTO `users`(`user_id`, `username`, `password`, `email`, `fullname`, `is_admin`, `is_locked`) VALUES (1,'admin','$2b$10$GUX7D4isxJFeqX8hy5eB2OvJbKlXlJG3QNjhMOFOhtZam1PyrlC2K','admin@gmail.com', 'Admin', true,false);

ALTER TABLE `users`
    MODIFY `user_id` int AUTO_INCREMENT, AUTO_INCREMENT = 2;
COMMIT;


INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (1,'iPhone 13 Pro Max', 300, 319,'Apple', 'Phone/Apple', 'iPhone13-pro-max.jpg', '2022-1-1', 30);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (2,'iPhone 12 Pro Max', 200, 209,'Apple', 'Phone/Apple', 'iPhone12-pro-max.jpg','2021-9-15', 5);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (3,'iPhone 11 Pro Max', 100, 119,'Apple', 'Phone/Apple', 'iPhone11-pro-max.jpg', '2020-12-9', 12);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (4,'Airpods Pro', 130, 139,'Apple', 'Headphone/Apple', 'airpods-pro.jpg', '2023-10-15', 39);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (5,'Samgsung Galaxy S22 Ultra', 400, 419,'Samsung', 'Phone', 'samsung-s22-ultra.jpg','2022-1-1', 30);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (6,'Airpods 3', 110, 119,'Apple', 'Headphone/Apple', 'airpods3.jpg', '2021-9-15', 50);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (7,'Samsung Buds 2 Pro', 100, 119,'Samsung', 'Headphone', 'samsung-buds2-pro.jpg', '2020-12-9', 10);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (8,'Samsung Galaxy Z Flip5', 400, 419,'Samsung', 'Phone', 'samsung-z-flip5.jpg', '2023-10-15', 12);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (9,'Samsung Galaxy Z Fold5 5G', 549, 599,'Samsung', 'Phone', 'samsung-z-fold5.jpg', '2022-1-1', 6);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (10,'Nokia 215', 50, 59,'Nokia', 'Phone', 'nokia-215.jpg', '2021-9-15', 9);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (11,'Nokia 110 4G', 60, 69,'Nokia', 'Phone', 'nokia-110-4g.jpg', '2020-12-9', 9);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (12,'Oppo Find N3', 700, 799,'Oppo', 'Phone', 'oppo-find-n3.jpg', '2021-9-15', 2);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (13,'Oppo Reno 10', 719, 759, 'Oppo', 'Phone', 'oppo-reno10.jpg', '2020-12-9', 1);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (14,'iPhone 20W Adapter ', 30, 39, 'Apple', 'Charger/Apple', 'apple-adapter.jpg', '2020-12-9', 7);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (15,'Samsung Adapter', 20, 29, 'Samsung', 'Charger', 'samsung-adapter.jpg', '2020-12-9', 10);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (16,'Samsung Adapter + Cable', 49, 59, 'Samsung', 'Charger', 'samsung-adapter-cable.jpg', '2020-12-9', 11);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (17,'Apple TypeC to Lightning Cable', 50, 59, 'Apple', 'Charger/Apple', 'apple-typec-to-lightning.jpg', '2020-12-9', 9);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (18,'Oppo Charger Cable', 10, 19, 'Oppo', 'Charger', 'oppo-cable.jpg', '2020-12-9', 42);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (19,'Oppo 18W Adapter', 20, 29, 'Oppo', 'Charger', 'oppo-adapter.jpg', '2020-12-9', 12);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (20,'AVA+ 12W JP299', 10, 19, 'AVA', 'Backup_Charger', 'ava-backup-charger.jpg', '2022-12-9', 20);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (21,'AnKer 30W A1256', 50, 59, 'Anker', 'Backup_Charger', 'anker-backup-charger.jpg', '2022-10-19', 10);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (22,'Apple Watch Series 9', 1100, 1299, 'Apple', 'Watch', 'apple-watch-series9.png', '2023-10-29', 13);
INSERT INTO `products`(`product_id`, `product_name`, `import_price`, `retail_price`, `manufacturer`, `category`, `image`, `creation_date`, `quantity_in_stock`) VALUES (23,'Apple Watch Ultra 2', 1300, 1599, 'Apple', 'Watch', 'apple-watch-ultra2.jpg', '2023-12-10', 3);

ALTER TABLE `products`
    MODIFY `product_id` int AUTO_INCREMENT, AUTO_INCREMENT = 20;
COMMIT;