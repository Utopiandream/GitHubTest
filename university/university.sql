-- phpMyAdmin SQL Dump
-- version 2.11.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 06, 2015 at 04:21 PM
-- Server version: 5.0.67
-- PHP Version: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: 'university'
--

-- --------------------------------------------------------

--
-- Table structure for table 'course'
--

DROP TABLE IF EXISTS course;
CREATE TABLE course (
  course_number varchar(10) NOT NULL,
  course_name varchar(50) default '',
  credit_hours int(1) unsigned default '0',
  department varchar(10) default '',
  PRIMARY KEY  (course_number)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'course'
--

INSERT INTO course VALUES
('CS1310', 'Intro to Computer Science', 4, 'CS'),
('CS3320', 'Data Structures', 4, 'CS'),
('MATH2410', 'Discrete Mathematics', 3, 'MATH'),
('CS3380', 'Database', 3, 'CS');

-- --------------------------------------------------------

--
-- Table structure for table 'grade_report'
--

DROP TABLE IF EXISTS grade_report;
CREATE TABLE grade_report (
  student_number int(10) unsigned NOT NULL,
  section_identifier int(10) NOT NULL,
  grade varchar(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'grade_report'
--

INSERT INTO grade_report VALUES
(17, 112, 'B'),
(17, 119, 'C'),
(8, 85, 'A'),
(8, 92, 'A'),
(8, 102, 'B'),
(8, 135, 'A');

-- --------------------------------------------------------

--
-- Table structure for table 'mysection'
--

DROP TABLE IF EXISTS mysection;
CREATE TABLE mysection (
  section_identifier int(10) unsigned NOT NULL,
  course_number varchar(10) NOT NULL,
  semester varchar(10) default '',
  myyear varchar(10) default '',
  instructor varchar(10) default '',
  PRIMARY KEY  (section_identifier)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'mysection'
--

INSERT INTO mysection VALUES
(85, 'MATH2410', 'Fall', '07', 'King'),
(92, 'CS1310', 'Fall', '07', 'Anderson'),
(102, 'CS3320', 'Spring', '08', 'Knuth'),
(112, 'MATH2410', 'Fall', '08', 'Chang'),
(119, 'CS1310', 'Fall', '08', 'Anderson'),
(135, 'CS3380', 'Fall', '08', 'Stone');

-- --------------------------------------------------------

--
-- Table structure for table 'prerequisite'
--

DROP TABLE IF EXISTS prerequisite;
CREATE TABLE prerequisite (
  course_number varchar(10) NOT NULL,
  prerequisite_number varchar(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'prerequisite'
--

INSERT INTO prerequisite VALUES
('CS3380', 'CS3320'),
('CS3380', 'MATH2410'),
('CS3320', 'CS1310');

-- --------------------------------------------------------

--
-- Table structure for table 'student'
--

DROP TABLE IF EXISTS student;
CREATE TABLE student (
  student_number int(10) unsigned NOT NULL,
  `name` varchar(30) default '',
  myclass int(1) unsigned default '0',
  major varchar(10) default '',
  PRIMARY KEY  (student_number)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table 'student'
--

INSERT INTO student VALUES
(17, 'Smith', 1, 'CS'),
(8, 'Brown', 2, 'CS');
