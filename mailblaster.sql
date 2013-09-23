-- phpMyAdmin SQL Dump
-- version 2.11.0
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 21, 2008 at 06:23 PM
-- Server version: 5.0.27
-- PHP Version: 5.1.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `mailblaster`
--

-- --------------------------------------------------------

--
-- Table structure for table `camp_indi_report`
--

DROP TABLE IF EXISTS `camp_indi_report`;
CREATE TABLE IF NOT EXISTS `camp_indi_report` (
  `camp_id` smallint(6) NOT NULL,
  `member_email` char(50) collate latin1_general_ci NOT NULL,
  PRIMARY KEY  (`camp_id`,`member_email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `camp_links`
--

DROP TABLE IF EXISTS `camp_links`;
CREATE TABLE IF NOT EXISTS `camp_links` (
  `link_no` smallint(6) NOT NULL,
  `camp_id` smallint(6) NOT NULL,
  `link_url` char(200) collate latin1_general_ci NOT NULL,
  `no_of_visits` int(11) NOT NULL,
  PRIMARY KEY  (`link_no`,`camp_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `camp_list`
--

DROP TABLE IF EXISTS `camp_list`;
CREATE TABLE IF NOT EXISTS `camp_list` (
  `camp_id` smallint(6) NOT NULL,
  `list_id` smallint(6) NOT NULL,
  PRIMARY KEY  (`camp_id`,`list_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `camp_report`
--

DROP TABLE IF EXISTS `camp_report`;
CREATE TABLE IF NOT EXISTS `camp_report` (
  `camp_id` smallint(6) NOT NULL,
  `mail_sent` int(11) NOT NULL,
  `mail_opened` int(11) NOT NULL,
  `no_of_links` smallint(6) NOT NULL,
  `mail_unsub` smallint(6) NOT NULL,
  PRIMARY KEY  (`camp_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `global_list`
--

DROP TABLE IF EXISTS `global_list`;
CREATE TABLE IF NOT EXISTS `global_list` (
  `list_id` smallint(6) NOT NULL,
  `list_name` char(25) collate latin1_general_ci NOT NULL,
  `modi_date` date NOT NULL,
  PRIMARY KEY  (`list_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `list_member`
--

DROP TABLE IF EXISTS `list_member`;
CREATE TABLE IF NOT EXISTS `list_member` (
  `list_id` smallint(6) NOT NULL,
  `member_name` varchar(100) collate latin1_general_ci NOT NULL,
  `member_email` varchar(100) collate latin1_general_ci NOT NULL,
  `member_unsub` tinyint(4) NOT NULL,
  PRIMARY KEY  (`list_id`,`member_email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mail_campaign`
--

DROP TABLE IF EXISTS `mail_campaign`;
CREATE TABLE IF NOT EXISTS `mail_campaign` (
  `camp_id` smallint(6) NOT NULL,
  `camp_name` char(25) collate latin1_general_ci NOT NULL,
  `last_modi` date NOT NULL,
  `launch_time` datetime default NULL,
  `html_data` blob NOT NULL,
  `text_data` blob NOT NULL,
  `from_name` char(25) collate latin1_general_ci NOT NULL,
  `from_email` char(50) collate latin1_general_ci NOT NULL,
  `mail_subject` char(100) collate latin1_general_ci NOT NULL,
  `camp_previewed` tinyint(4) NOT NULL,
  `camp_tested` tinyint(4) NOT NULL,
  `camp_audi` tinyint(4) NOT NULL,
  `camp_schd` tinyint(4) NOT NULL,
  `camp_launch` tinyint(4) NOT NULL,
  `unsubs_reqd` tinyint(4) NOT NULL,
  `attach_field` varchar(500) collate latin1_general_ci default NULL,
  PRIMARY KEY  (`camp_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sent_mail_list`
--

DROP TABLE IF EXISTS `sent_mail_list`;
CREATE TABLE IF NOT EXISTS `sent_mail_list` (
  `camp_id` smallint(6) NOT NULL,
  `member_email` char(50) collate latin1_general_ci NOT NULL,
  PRIMARY KEY  (`camp_id`,`member_email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;
