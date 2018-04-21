# Host: 192.168.99.100  (Version: 5.7.21)
# Date: 2018-04-21 19:21:54
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES gb2312 */;

#
# Structure for table "Inventory"
#

DROP TABLE IF EXISTS `Inventory`;
CREATE TABLE `Inventory` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `productId` int(10) NOT NULL DEFAULT '0',
  `count` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=gbk;

#
# Data for table "Inventory"
#


#
# Structure for table "Product"
#

DROP TABLE IF EXISTS `Product`;
CREATE TABLE `Product` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL DEFAULT '',
  `description` varchar(100) DEFAULT NULL,
  `price` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=gbk;

#
# Data for table "Product"
#

