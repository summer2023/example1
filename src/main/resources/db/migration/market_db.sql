# Host: 192.168.99.100  (Version 5.7.21)
# Date: 2018-05-04 01:43:34
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "Inventory"
#

CREATE TABLE `Inventory` (
  `id` int(10) NOT NULL DEFAULT '0',
  `count` int(10) NOT NULL DEFAULT '0',
  `lockedCount` int(10) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

#
# Data for table "Inventory"
#

INSERT INTO `Inventory` VALUES (1,113,107),(2,30,0),(3,100,0),(4,100,85);

#
# Structure for table "Product"
#

CREATE TABLE `Product` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL DEFAULT '',
  `description` varchar(100) DEFAULT NULL,
  `price` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

#
# Data for table "Product"
#

INSERT INTO `Product` VALUES (1,'test666','test',1000),(2,'方便面','test',20),(3,'方便面','康师傅',5),(4,'农夫山泉','矿泉水',3);

#
# Structure for table "ProductSnap"
#

CREATE TABLE `ProductSnap` (
  `snapId` int(10) NOT NULL AUTO_INCREMENT,
  `id` int(10) NOT NULL DEFAULT '0',
  `orderId` int(10) NOT NULL DEFAULT '0',
  `productName` varchar(40) NOT NULL DEFAULT '',
  `productDescription` varchar(100) DEFAULT NULL,
  `purchasePrice` varchar(40) NOT NULL DEFAULT '',
  `purchaseCount` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`snapId`)
) ENGINE=InnoDB  DEFAULT CHARSET=gbk;

#
# Data for table "ProductSnap"
#



#
# Structure for table "UserOrder"
#

CREATE TABLE `UserOrder` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `totalPrice` varchar(20) NOT NULL DEFAULT '',
  `status` varchar(20) NOT NULL DEFAULT '',
  `createTime` varchar(20) NOT NULL DEFAULT '',
  `finishTime` varchar(20) DEFAULT NULL,
  `paidTime` varchar(20) DEFAULT NULL,
  `withdrawnTime` varchar(20) DEFAULT NULL,
  `userId` int(10) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

#
# Data for table "UserOrder"
#


