
$sql = "CREATE TABLE IF NOT EXISTS `admin_table` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
)";


$sql = "INSERT INTO `admin_table` (`admin_id`, `email`, `username`, `password`) VALUES
(1, 'admin', 'admin', '25f9e794323b453885f5181f1b624db')";


$sql = "CREATE TABLE IF NOT EXISTS `booked` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `car_id` int(11) NOT NULL,
  `car_name` varchar(30) NOT NULL,
  `price` double NOT NULL,
  `customer` varchar(30) NOT NULL,
  `book_from` date DEFAULT NULL,
  `book_until` date DEFAULT NULL,
  `image` blob NOT NULL,
  PRIMARY KEY (`id`)
)";



$sql = "CREATE TABLE IF NOT EXISTS `car_table` (
  `car_id` int(11) NOT NULL AUTO_INCREMENT,
  `car_name` varchar(30) NOT NULL,
  `car_model` varchar(30) NOT NULL,
  `status` varchar(25) NOT NULL,
  `price` double NOT NULL,
  `image` blob NOT NULL,
  PRIMARY KEY (`car_id`)
)";


$sql = "CREATE TABLE IF NOT EXISTS `customer_table` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `surname` varchar(25) NOT NULL,
  `omang` varchar(25) NOT NULL,
  `email` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  PRIMARY KEY (`email`)
)";
