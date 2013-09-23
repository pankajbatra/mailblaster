import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminControlMiddle extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create connection pool");
		} 
    }
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con = null;
		res.setContentType("text/html");
        if(!Utils.validateSession(req, res))
            return;
        res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user;
		ad_user = (String)session.getValue("aduser");
		String sort="desc";
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
				return;
		}	
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			String order_camp="camp_name";
			String order_camp1="last_modi";
			out.println("<html>");
			out.println("<head>");
			out.println("<title></title>");
			out.println("</head>");
			out.println("<body topmargin='0' leftmargin='0' link='#ffffff' vlink='#ffffff' alink='#ffffff' >");
			out.println("<table border='0' width='102%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
			out.println("<tr>");
			out.println("<td width='42%' height='24'></td>");
			out.println("<td width='28%' height='24'></td>");
			out.println("<td width='5%' height='24'></td>");
			out.println("<td width='25%' height='24'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<div align='right'>");
			out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
			out.println("<td width='100%' height='10' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Welcome To Mail Blaster Contol Panel</font></small></strong></td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td width='64%' bgcolor='#C0C0C0' height='1'></td>");
			out.println("<td width='16%' bgcolor='#FFFFFF' height='1'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='0' width='93%' height='20' cellspacing='0' cellpadding='0'>");
			out.println("<tr>");
			out.println("<td width='100%'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			out.println("<div align='right'>");
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><small><font face='Verdana' color='#FFFFFF'><strong>Available Campaigns</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='25%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Actions</font></small></small></center></td>");
			
			out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Campaign Name</font></small></small>");
			out.println("<a href='AdminControlMiddle?order_camp="+order_camp+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order_camp="+order_camp+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
			out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Launch Date</font></small></small></center></td>");
            out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Last Modified On</font></small></small>");
			out.println("<a href='AdminControlMiddle?order_camp1="+order_camp1+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order_camp1="+order_camp1+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
            out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");
            //ResultSet rs = stmt.executeQuery("Select * from mail_campaign");
            	String sort2=req.getParameter("sort");
            String order2=req.getParameter("order_camp");
        	String order3=req.getParameter("order_camp1");
        	
        	ResultSet rs=null;
        if(((order2==null)||(order2.equals("")))&&((order3==null)||(order3.equals(""))))
                rs = stmt.executeQuery("Select * from mail_campaign");
        else if(order2!=null)
        {
            	if((sort2==null)||(sort2.equals("")))
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order2);
                else             
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order2+" desc");
        }
        
            else if(order3!=null)
            {
            	if((sort2==null)||(sort2.equals("")))
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order3);
                else             
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order3+" desc");
            }

            while(rs.next()){
                int camp_id = rs.getInt("camp_id");
        		String camp_name = rs.getString("camp_name");        		
        		java.sql.Date last_modi = rs.getDate("last_modi");
        		java.sql.Date launch_time = rs.getDate("launch_time");
        		int camp_launch;
        		camp_launch=rs.getInt("camp_launch");
        		if(camp_launch==0){
        			out.println("<div align='right'>");
        			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        			out.println("<tr>");
        			out.println("<td width='25%' valign='middle' align='center' bgcolor='#FFFFFF'>");        		
        			out.println("<a href='EditCamp?camp_id="+camp_id+"' target='_self'><img src='images/edit.gif' width='16' height='16' alt='Edit Campaign.'></a> ");
        			out.println("<a href='RenameCamp?camp_id="+camp_id+"' target='_self'><img src='images/rename.gif' width='16' height='16' alt='Rename Campaign.'></a>");
        			out.println("<a href='DeleteCamp?camp_id="+camp_id+"' target='_self'><img src='images/delete.gif' width='16' height='16' alt='Delete Campaign.'></a>");
        			out.println("<a href='PreviewCamp?camp_id="+camp_id+"' target='_self' ><img src='images/preview.gif' width='16' height='16' alt='Preview.'></a>");
        			out.println("<a href='SendTestMail?camp_id="+camp_id+"' target='_self'><img src='images/sendtest.gif' width='16' height='16' alt='Send Test Mail.'></a>");
        			out.println("<a href='SelectAudi?camp_id="+camp_id+"' target='_self'><img src='images/select.gif' width='16' height='16' alt='Select Audience.'></a>");
        			out.println("<a href='ScheduleMail?camp_id="+camp_id+"' target='_self'><img src='images/schedule.gif' width='16' height='16' alt='Schedule Mail.'></a>");
        			out.println("<a href='LaunchCamp?camp_id="+camp_id+"' target='_self'><img src='images/start.gif' width='16' height='16' alt='Launch Campaign.'></a></td>");
        			out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+camp_name+"</strong></small></small></font></td>");
        			java.util.Date now=new java.util.Date(0,0,1);
        			if(now.equals(launch_time)){
        				out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>--</small></small></font></td>");
        			}        			
        			else{
        				out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>"+launch_time+"</small></small></font></td>");
        			}
        			out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>"+last_modi+"</small></small></font></td>");
        			out.println("</tr>");
        			out.println("</table>");
        			out.println("</div>");        		        		
        		}
        	}
        	out.println("<br><br>");
        	rs.close();
        	String order_list="list_name";
        	String order1_list="modi_date";
        	out.println("<div align='right'>");
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><small><font face='Verdana' color='#FFFFFF'><strong>Available Lists</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
		    out.println("<tr>");
			out.println("<td width='25%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Actions</font></small></small></center></td>");
			out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>List Name</font></small></small>");
			out.println("<a href='AdminControlMiddle?order_list="+order_list+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order_list="+order_list+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
			out.println("<td width='25%'><center><small><small><font face='Verdana'>Total Items</font></small></small></center></td>");
            out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Last Modified On</font></small></small>");
			out.println("<a href='AdminControlMiddle?order1_list="+order1_list+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order1_list="+order1_list+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
            out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");
        	String sort1=req.getParameter("sort");
            String order=req.getParameter("order_list");
        	String order1=req.getParameter("order1_list");
        if(((order==null)||(order.equals("")))&&((order1==null)||(order1.equals(""))))
         rs = stmt.executeQuery("Select * from global_list");
        else if(order!=null)
        {
            	if((sort1==null)||(sort1.equals("")))
            rs = stmt.executeQuery("Select * from global_list order by "+order);
                else             
            rs = stmt.executeQuery("Select * from global_list order by "+order+" desc");
        }
        
            else if(order1!=null)
            {
            	if((sort1==null)||(sort1.equals("")))
            rs = stmt.executeQuery("Select * from global_list order by "+order1);
                else             
            rs = stmt.executeQuery("Select * from global_list order by "+order1+" desc");
            }
            
            	        	
            //rs = stmt.executeQuery("Select * from global_list");
            while(rs.next()){
                int list_id = rs.getInt("list_id");
        		String list_name = rs.getString("list_name");        		
        		java.sql.Date modi_date = rs.getDate("modi_date");
        		out.println("<div align='right'>");
        		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        		out.println("<tr>");
        		out.println("<td width='25%' valign='middle' align='center' bgcolor='#FFFFFF'>");        		
        		out.println("<a href='AddToList?list_id="+list_id+"' target='_self'><img src='images/select.gif' width='16' height='16' alt='Add Account.'></a> ");
        		out.println("<a href='EditList?list_id="+list_id+"' target='_self'><img src='images/edit.gif' width='16' height='16' alt='Edit List.'></a> ");
        		//out.println("<a href='LaunchCamp?camp_id="+camp_id+"' target='_self'><img src='images/start.gif' width='16' height='16' alt='Launch Campaign.'></a>");
        		out.println("<a href='DeleteFromList?list_id="+list_id+"' target='_self'><img src='images/D.gif' width='16' height='16' alt='Delete An Account.'></a>");
        		out.println("<a href='DeleteList?list_id="+list_id+"' target='_self'><img src='images/delete.gif' width='16' height='16' alt='Delete List.'></a>");
        		out.println("<a href='RenameList?list_id="+list_id+"' target='_self'><img src='images/rename.gif' width='16' height='16' alt='Rename List.'></a>");
        		//out.println("<a href='SendTestMail?camp_id="+camp_id+"' target='_self'><img src='images/sendtest.gif' width='16' height='16' alt='Send Test Mail.'></a>");
        		out.println("<a href='ViewList?list_id="+list_id+"' target='_self'><img src='images/preview.gif' width='16' height='16' alt='View List.'></a>");
        		out.println("<a href='ViewUnsub?list_id="+list_id+"' target='_self'><img src='images/logout.gif' width='16' height='16' alt='View Unsubscribers.'></a>");
        		//out.println("<a href='SelectAudi?camp_id="+camp_id+"' target='_self'><img src='images/select.gif' width='16' height='16' alt='Select Audience.'></a>");
        		out.println("<a href='ExportList?list_id="+list_id+"' target='_self'><img src='images/export.gif' width='16' height='16' alt='Export List.'></a></td>");
        		out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+list_name+"</strong></small></small></font></td>");
        		int total_items=0;
        		Statement stmt2=con.createStatement();
        		ResultSet rs2=stmt2.executeQuery("Select COUNT(*) from list_member where list_id="+list_id);
        		if(rs2.next()) total_items=rs2.getInt("COUNT(*)");
        		out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+total_items+"</strong></small></small></font></td>");
        		out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>"+modi_date+"</small></small></font></td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div>");        		        		
        	}        	
        	rs.close();
        	String order_report="camp_name";
        	String order1_report="launch_time";
        	out.println("<br><br>");
        	out.println("<div align='right'>");
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><small><font face='Verdana' color='#FFFFFF'><strong>Campaign Reporting</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
		    out.println("<tr>");
			out.println("<td width='20%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Actions</font></small></small></center></td>");
			out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Campaign Name</font></small></small>");
			out.println("<a href='AdminControlMiddle?order_report="+order_report+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order_repoert="+order_report+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
        	out.println("<td width='25%' height='13'><small><small><font face='Verdana'><center>Launch Date</font></small></small>");
			out.println("<a href='AdminControlMiddle?order1_report="+order1_report+"' target='_self'><img src='images/up.gif'></a>");
			out.println("<a href='AdminControlMiddle?order1_report="+order1_report+"&sort="+sort+"' target='_self'><img src='images/down.gif'></a></cemter></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");  
        	String sort3=req.getParameter("sort");
            String order4=req.getParameter("order_report");
        	String order5=req.getParameter("order1_report");
        if(((order4==null)||(order4.equals("")))&&((order5==null)||(order5.equals(""))))
         rs = stmt.executeQuery("Select * from mail_campaign");
        else if(order4!=null)
        {
            	if((sort3==null)||(sort3.equals("")))
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order4);
                else             
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order4+" desc");
        }
        
            else if(order5!=null)
            {
            	if((sort3==null)||(sort3.equals("")))
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order5);
                else             
            rs = stmt.executeQuery("Select * from mail_campaign order by "+order5+" desc");
            }      	
            //rs = stmt.executeQuery("Select * from mail_campaign");
            while(rs.next()){
                int camp_id = rs.getInt("camp_id");
        		String camp_name = rs.getString("camp_name"); 
        		java.sql.Timestamp launch_time = rs.getTimestamp("launch_time");
        		int camp_launch;
        		camp_launch=rs.getInt("camp_launch");
        		if(camp_launch==1){
        			out.println("<div align='right'>");
        			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        			out.println("<tr>");
        			out.println("<td width='20%' valign='middle' align='center' bgcolor='#FFFFFF'>");        		
        			//out.println("<a href='EditCamp?camp_id="+camp_id+"' target='_self'><img src='images/edit.gif' width='16' height='16' alt='Edit Campaign.'></a> ");
        			//out.println("<a href='LaunchCamp?camp_id="+camp_id+"' target='_self'><img src='images/start.gif' width='16' height='16' alt='Launch Campaign.'></a>");
        			java.util.Date now= new java.util.Date();
        			if(now.after(launch_time))out.println("<a href='DeleteReport?camp_id="+camp_id+"' target='_self'><img src='images/delete.gif' width='16' height='16' alt='Delete Report.'></a>");
        			else out.println("<a href='CancelLaunch?camp_id="+camp_id+"' target='_self'><img src='images/stop.gif' width='16' height='16' alt='Cancel Launch.'></a>");
        			//out.println("<a href='RenameCamp?camp_id="+camp_id+"' target='_self'><img src='images/rename.gif' width='16' height='16' alt='Rename Campaign.'></a>");
        			//out.println("<a href='SendTestMail?camp_id="+camp_id+"' target='_self'><img src='images/sendtest.gif' width='16' height='16' alt='Send Test Mail.'></a>");
        			out.println("<a href='ViewReport?camp_id="+camp_id+"' target='_self' ><img src='images/preview.gif' width='16' height='16' alt='Preview.'></a>");
        			//out.println("<a href='SelectAudi?camp_id="+camp_id+"' target='_self'><img src='images/select.gif' width='16' height='16' alt='Select Audience.'></a>");
        			//out.println("<a href='ScheduleMail?camp_id="+camp_id+"' target='_self'><img src='images/schedule.gif' width='16' height='16' alt='Schedule Mail.'></a></td>");
        			out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+camp_name+"</strong></small></small></font></td>");
        			out.println("<td width='25%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>"+launch_time+"</small></small></font></td>");
        			out.println("</tr>");
        			out.println("</table>");
        			out.println("</div>");        		        		
        		}
        	}
        	out.println("</body>");
        	out.println("</html>");
        	rs.close();
        }
        catch(Exception e){
        	try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
				con.rollback();				
			}
			catch (Exception ignored) { }
		}
		finally{
			if(con!=null) pool.returnConnection(con);
			out.close();		
		}
	}
}
