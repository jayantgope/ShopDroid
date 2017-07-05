-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 25, 2016 at 09:12 AM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `shopdroid`
--

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE IF NOT EXISTS `customers` (
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `state` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `pincode` varchar(100) NOT NULL,
  `mobile_no` varchar(100) NOT NULL,
  `wallet_amount` varchar(100) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`username`, `password`, `first_name`, `last_name`, `address`, `state`, `city`, `pincode`, `mobile_no`, `wallet_amount`) VALUES
('jayant', 'lol', 'Jayant', 'Gope', 'Village - Lodhma, Post - Tetri, P.S. - Jagarnathpur', 'Jharkhand', 'Ranchi', '834010', '8950891699', '2500'),
('jayantgope@yahoo.in', 'lol', 'Jayant', 'Gope', 'APIIT SD INDIA', 'Haryana', 'Panipat', '132103', '8950891699', '1890');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE IF NOT EXISTS `products` (
  `product_code` varchar(100) NOT NULL,
  `barcode` varchar(100) NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `location` varchar(100) NOT NULL,
  `quantity` varchar(100) NOT NULL,
  `unit_cost` varchar(100) NOT NULL,
  `image` varchar(10000) NOT NULL,
  `shop_id` varchar(100) NOT NULL,
  `date_added` datetime NOT NULL,
  PRIMARY KEY (`barcode`,`shop_id`),
  KEY `shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_code`, `barcode`, `product_name`, `category`, `location`, `quantity`, `unit_cost`, `image`, `shop_id`, `date_added`) VALUES
('001', '00001234560000000018', 'Pepsi', 'Cold drinks', 'Godown', '50', '35', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 15:07:47'),
('1', '112323663906', 'Rohit Notebook', 'Stationary', 'Shelf', '10', '30', 'No Image', 'modi@yahoo.in', '2016-05-25 08:41:04'),
('002', '123456789012', 'CDS Preperation', 'Books', 'Shelf', '22', '385', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 18:36:08'),
('002', '850006000012', 'Sprite ', 'Books', 'Godown', '50', '55', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 19:18:01'),
('003', '850006000013', 'Android Headfirst', 'Books', 'Shelf', '40', '450', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 15:17:52'),
('003', '866703966792655', 'Jayant Gope ', 'Cold Drink ', 'Lol', '50', '30', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 00:00:00'),
('002', '866703966792656', 'Sprite', 'Cold Drink ', 'Godown', '50', '50', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 00:00:00'),
('001', '866703966792657 ', 'Pepsi', 'Cold Drink ', 'Shelf', '30', '35', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 00:00:00'),
('004', '8901764032806', 'Sprite', 'Cold drinks', 'Fridge', '50', '82', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 15:07:47'),
('2', '8902519009845', 'Classmate Copy', 'Stationary', 'Shelf', '4', '75', 'No Image', 'modi@yahoo.in', '2016-05-25 08:41:04'),
('001', '8969', 'JAyant', 'a', 'a', '10', '50', 'lol', 'dobhaistore', '2016-05-23 00:00:00'),
('001', '9789383643226', 'RSA', 'Books', 'Shelf', '75', '50', 'No Image', 'jayantgope@yahoo.in', '2016-05-24 19:18:01');

-- --------------------------------------------------------

--
-- Table structure for table `shop_registration`
--

CREATE TABLE IF NOT EXISTS `shop_registration` (
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `shop_name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `state` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `pincode` varchar(50) NOT NULL,
  `mobile_no` varchar(50) NOT NULL,
  `latitude` varchar(100) NOT NULL DEFAULT '29',
  `longitude` varchar(100) NOT NULL DEFAULT '77',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `shop_registration`
--

INSERT INTO `shop_registration` (`username`, `password`, `shop_name`, `address`, `state`, `city`, `pincode`, `mobile_no`, `latitude`, `longitude`) VALUES
('dobhaistore', 'dobhai', 'Do Bhai Store', 'ddd', 'Haryana', 'Panipat', '132103', '9199600886', '29', '77'),
('gupta@gmail.com', 'gupta', 'God', 'sss', 'Haryana', 'Panipat', '132103', '9199600887', '29.42', '76.97'),
('jayantgope@yahoo.in', 'lol', 'Do Bhai Store11', 'dddf', 'New Delhi', 'Delhi', '132103', '9199600888', '28.8139', '77'),
('modi@yahoo.in', 'lol', 'Nanosoft', 'panipat', 'Haryana', 'Panipat', '132103', '8950891699', '29.42', '76.9'),
('rahul@gmail.com', 'lol', 'Gupta Store', 'Model Town, Panipat', 'Haryana', 'Panipat', '132104', '8950891213', '29', '77');

-- --------------------------------------------------------

--
-- Table structure for table `tbllocation`
--

CREATE TABLE IF NOT EXISTS `tbllocation` (
  `userid` varchar(50) NOT NULL,
  `latitude` varchar(100) NOT NULL DEFAULT '29',
  `longitude` varchar(100) NOT NULL DEFAULT '77',
  UNIQUE KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tbllocation`
--

INSERT INTO `tbllocation` (`userid`, `latitude`, `longitude`) VALUES
('gupta@gmail.com', '29.42', '76.97'),
('jayant', '29', '77'),
('jayantgope@yahoo.in', '29', '77'),
('rahul@gmail.com', '0', '0');

-- --------------------------------------------------------

--
-- Table structure for table `transaction`
--

CREATE TABLE IF NOT EXISTS `transaction` (
  `transaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(100) NOT NULL,
  `customer_id` varchar(100) NOT NULL,
  `shop_id` varchar(100) NOT NULL,
  `date` datetime NOT NULL,
  `quantity` varchar(100) NOT NULL,
  `updated_quantity` varchar(50) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transaction_id`),
  KEY `fk_transaction` (`barcode`),
  KEY `fk_transaction1` (`customer_id`),
  KEY `fk_transaction2` (`shop_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `transaction`
--

INSERT INTO `transaction` (`transaction_id`, `barcode`, `customer_id`, `shop_id`, `date`, `quantity`, `updated_quantity`) VALUES
(2, '123456789012', 'jayant', 'jayantgope@yahoo.in', '2016-05-24 11:40:23', '1', 'N'),
(3, '9789383643226', 'jayant', 'jayantgope@yahoo.in', '2016-05-24 18:51:23', '2', '0');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`shop_id`) REFERENCES `shop_registration` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `fk_transaction1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`username`),
  ADD CONSTRAINT `fk_transaction2` FOREIGN KEY (`shop_id`) REFERENCES `shop_registration` (`username`),
  ADD CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`barcode`) REFERENCES `products` (`barcode`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
