/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : test-product

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2021-11-15 18:07:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for test_product_attribute_value
-- ----------------------------
DROP TABLE IF EXISTS `test_product_attribute_value`;
CREATE TABLE `test_product_attribute_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `product_attribute_id` bigint(20) DEFAULT NULL,
  `value` varchar(64) DEFAULT NULL COMMENT '手动添加规格或参数的值，参数单值，规格有多个时以逗号隔开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=utf8 COMMENT='存储产品参数信息的表';

-- ----------------------------
-- Records of test_product_attribute_value
-- ----------------------------
INSERT INTO `test_product_attribute_value` VALUES ('1', '9', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('2', '10', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('3', '11', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('4', '12', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('5', '13', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('6', '14', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('7', '18', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('8', '7', '1', 'X');
INSERT INTO `test_product_attribute_value` VALUES ('9', '7', '1', 'XL');
INSERT INTO `test_product_attribute_value` VALUES ('10', '7', '1', 'XXL');
INSERT INTO `test_product_attribute_value` VALUES ('11', '22', '7', 'x,xx');
INSERT INTO `test_product_attribute_value` VALUES ('12', '22', '24', 'no110');
INSERT INTO `test_product_attribute_value` VALUES ('13', '22', '25', '春季');
INSERT INTO `test_product_attribute_value` VALUES ('14', '22', '37', '青年');
INSERT INTO `test_product_attribute_value` VALUES ('15', '22', '38', '2018年春');
INSERT INTO `test_product_attribute_value` VALUES ('16', '22', '39', '长袖');
INSERT INTO `test_product_attribute_value` VALUES ('124', '23', '7', '米白色,浅黄色');
INSERT INTO `test_product_attribute_value` VALUES ('125', '23', '24', 'no1098');
INSERT INTO `test_product_attribute_value` VALUES ('126', '23', '25', '春季');
INSERT INTO `test_product_attribute_value` VALUES ('127', '23', '37', '青年');
INSERT INTO `test_product_attribute_value` VALUES ('128', '23', '38', '2018年春');
INSERT INTO `test_product_attribute_value` VALUES ('129', '23', '39', '长袖');
INSERT INTO `test_product_attribute_value` VALUES ('130', '1', '13', null);
INSERT INTO `test_product_attribute_value` VALUES ('131', '1', '14', null);
INSERT INTO `test_product_attribute_value` VALUES ('132', '1', '15', null);
INSERT INTO `test_product_attribute_value` VALUES ('133', '1', '16', null);
INSERT INTO `test_product_attribute_value` VALUES ('134', '1', '17', null);
INSERT INTO `test_product_attribute_value` VALUES ('135', '1', '18', null);
INSERT INTO `test_product_attribute_value` VALUES ('136', '1', '19', null);
INSERT INTO `test_product_attribute_value` VALUES ('137', '1', '20', null);
INSERT INTO `test_product_attribute_value` VALUES ('138', '1', '21', null);
INSERT INTO `test_product_attribute_value` VALUES ('139', '2', '13', null);
INSERT INTO `test_product_attribute_value` VALUES ('140', '2', '14', null);
INSERT INTO `test_product_attribute_value` VALUES ('141', '2', '15', null);
INSERT INTO `test_product_attribute_value` VALUES ('142', '2', '16', null);
INSERT INTO `test_product_attribute_value` VALUES ('143', '2', '17', null);
INSERT INTO `test_product_attribute_value` VALUES ('144', '2', '18', null);
INSERT INTO `test_product_attribute_value` VALUES ('145', '2', '19', null);
INSERT INTO `test_product_attribute_value` VALUES ('146', '2', '20', null);
INSERT INTO `test_product_attribute_value` VALUES ('147', '2', '21', null);
INSERT INTO `test_product_attribute_value` VALUES ('183', '31', '24', null);
INSERT INTO `test_product_attribute_value` VALUES ('184', '31', '25', '夏季');
INSERT INTO `test_product_attribute_value` VALUES ('185', '31', '37', '青年');
INSERT INTO `test_product_attribute_value` VALUES ('186', '31', '38', '2018年夏');
INSERT INTO `test_product_attribute_value` VALUES ('187', '31', '39', '短袖');
INSERT INTO `test_product_attribute_value` VALUES ('198', '30', '24', null);
INSERT INTO `test_product_attribute_value` VALUES ('199', '30', '25', '夏季');
INSERT INTO `test_product_attribute_value` VALUES ('200', '30', '37', '青年');
INSERT INTO `test_product_attribute_value` VALUES ('201', '30', '38', '2018年夏');
INSERT INTO `test_product_attribute_value` VALUES ('202', '30', '39', '短袖');
INSERT INTO `test_product_attribute_value` VALUES ('203', '26', '43', '金色,银色');
INSERT INTO `test_product_attribute_value` VALUES ('204', '26', '45', '5.0');
INSERT INTO `test_product_attribute_value` VALUES ('205', '26', '46', '4G');
INSERT INTO `test_product_attribute_value` VALUES ('206', '26', '47', 'Android');
INSERT INTO `test_product_attribute_value` VALUES ('207', '26', '48', '3000');
INSERT INTO `test_product_attribute_value` VALUES ('213', '27', '43', '黑色,蓝色');
INSERT INTO `test_product_attribute_value` VALUES ('214', '27', '45', '5.8');
INSERT INTO `test_product_attribute_value` VALUES ('215', '27', '46', '4G');
INSERT INTO `test_product_attribute_value` VALUES ('216', '27', '47', 'Android');
INSERT INTO `test_product_attribute_value` VALUES ('217', '27', '48', '3000ml');
INSERT INTO `test_product_attribute_value` VALUES ('218', '28', '43', '金色,银色');
INSERT INTO `test_product_attribute_value` VALUES ('219', '28', '45', '5.0');
INSERT INTO `test_product_attribute_value` VALUES ('220', '28', '46', '4G');
INSERT INTO `test_product_attribute_value` VALUES ('221', '28', '47', 'Android');
INSERT INTO `test_product_attribute_value` VALUES ('222', '28', '48', '2800ml');
INSERT INTO `test_product_attribute_value` VALUES ('223', '29', '43', '金色,银色');
INSERT INTO `test_product_attribute_value` VALUES ('224', '29', '45', '4.7');
INSERT INTO `test_product_attribute_value` VALUES ('225', '29', '46', '4G');
INSERT INTO `test_product_attribute_value` VALUES ('226', '29', '47', 'IOS');
INSERT INTO `test_product_attribute_value` VALUES ('227', '29', '48', '1960ml');
