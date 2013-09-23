<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" %>
<%
Class.forName("com.mysql.jdbc.Driver").newInstance();
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mailblaster?user=root");
Statement stmt=con.createStatement();	
/*stmt.executeUpdate("drop table if exists mail_campaign");
stmt.executeUpdate("drop table if exists camp_list");
stmt.executeUpdate("drop table if exists global_list");
stmt.executeUpdate("drop table if exists list_member");
stmt.executeUpdate("drop table if exists camp_report");
stmt.executeUpdate("drop table if exists camp_links");
stmt.executeUpdate("drop table if exists camp_indi_report");
stmt.executeUpdate("drop table if exists sent_mail_list");
*/
stmt.executeUpdate("Create table mail_campaign(camp_id SMALLINT PRIMARY KEY,camp_name char(25) NOT NULL,last_modi DATE NOT NULL,launch_time DATETIME ,html_data BLOB NOT NULL,text_data BLOB NOT NULL,from_name char(25) NOT NULL,from_email char(50) NOT NULL, mail_subject char(100) NOT NULL,camp_previewed TINYINT NOT NULL, camp_tested TINYINT NOT NULL, camp_audi TINYINT NOT NULL, camp_schd TINYINT NOT NULL, camp_launch TINYINT NOT NULL,unsubs_reqd TINYINT NOT NULL, attach_field VARCHAR( 500 ) DEFAULT NULL)");

stmt.executeUpdate("Create table camp_list (camp_id SMALLINT NOT NULL,list_id SMALLINT NOT NULL)");

stmt.executeUpdate("Create table global_list (list_id SMALLINT PRIMARY KEY,list_name char(25) NOT NULL,modi_date DATE NOT NULL)");

stmt.executeUpdate("Create table list_member (list_id SMALLINT NOT NULL,member_name char(35) NOT NULL,member_email char(50) NOT NULL,member_unsub TINYINT NOT NULL)");
	
stmt.executeUpdate("Create table camp_report (camp_id SMALLINT PRIMARY KEY,mail_sent int NOT NULL,mail_opened int NOT NULL,no_of_links SMALLINT NOT NULL,mail_unsub SMALLINT NOT NULL)");

stmt.executeUpdate("Create table camp_links (link_no SMALLINT NOT NULL, camp_id SMALLINT NOT NULL, link_url char(200) NOT NULL, no_of_visits int NOT NULL)");

stmt.executeUpdate("Create table camp_indi_report(camp_id SMALLINT NOT NULL, member_email char(50) NOT NULL)");

stmt.executeUpdate("Create table sent_mail_list(camp_id SMALLINT NOT NULL, member_email char(50) NOT NULL)");

out.println("tables created are:- mail_campaign,camp_list,global_list,list_member,camp_report,camp_links,camp_indi_report,sent_mail_list");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

</body>
</html>
<%
con.close();
stmt.close();
%>
