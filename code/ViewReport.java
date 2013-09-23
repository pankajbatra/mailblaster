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

public class ViewReport extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create a connection");
	    }
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		Connection con = null;
		String aduser;
		HttpSession session=req.getSession(false);
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			session.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
			return;
		}
		String temp_camp_id = req.getParameter("camp_id");
		int camp_id;
		try{
			camp_id=Integer.parseInt(temp_camp_id);	
		}
		catch(Exception e){
			out.println("<H2>NO Campaign Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}		
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			java.util.Date launch_time=null;
			ResultSet rs = stmt.executeQuery("select launch_time from mail_campaign where camp_id="+camp_id);			
			if(rs.next()){
				launch_time=rs.getDate("launch_time");	
			}
			java.util.Date now=new java.util.Date();
			if(now.before(launch_time)){
				out.println("<html>");
				out.println("<head>");
				out.println("<title></title>");
				out.println("</head>");
				out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
				out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
				out.println("<tr>");
				out.println("<td width='42%' height='24'></td>");
				out.println("<td width='28%' height='24'></td>");
				out.println("<td width='5%' height='24'></td>");
				out.println("<td width='25%' height='24'></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<div align='right'>");
				out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0'>");
				out.println("<tr>");
				out.println("<td width='100%' height='10' colspan='2'></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Mail Blaster Contol Panel</font></small></strong></td>");
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
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
				out.println("<tr>");
				out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>View Campaign Report..</strong></small></font></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div><div align='right'>");				
				out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
				out.println("<tr>");
				out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
				out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
				out.println("<tr>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><br><br><br>");
				out.println("This campaign has not been sent yet.<br><br>It is scheduled for "+launch_time+"</small></font></td>");
				out.println("</tr>");
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to go to Main Panel Page</small></font></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
				out.println("</body>");
				out.println("</html>");
            }
			else{
				int mail_opened=0,mail_sent=0,links=0,total_clicks=0,mail_unsub=0;
				rs=stmt.executeQuery("Select mail_sent,mail_opened,no_of_links,mail_unsub from camp_report where camp_id ="+camp_id);
				if(rs.next()){
					mail_sent=rs.getInt("mail_sent");
					mail_opened=rs.getInt("mail_opened");
					links=rs.getInt("no_of_links");
					mail_unsub=rs.getInt("mail_unsub");
				}
				rs.close();
				String urls[]=new String[links];
				int link_visits[]=new int[links];
				int co=0;
				rs=stmt.executeQuery("select * from camp_links where camp_id="+camp_id);
				while(rs.next()){
					link_visits[co]=rs.getInt("no_of_visits");
					urls[co]=rs.getString("link_url");
					total_clicks+=link_visits[co];	
					co++;					
				}
				out.println("<html>");
				out.println("<head>");
				out.println("<title></title>");
				out.println("</head>");
				out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
				out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
				out.println("<tr>");
				out.println("<td width='42%' height='24'></td>");
				out.println("<td width='28%' height='24'></td>");
				out.println("<td width='5%' height='24'></td>");
				out.println("<td width='25%' height='24'></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("<div align='right'>");
				out.println("<table border='0' width='98%' cellspacing='0' cellpadding='0'>");
				out.println("<tr>");
				out.println("<td width='100%' height='10' colspan='2'></td>");
				out.println("</tr>");
				out.println("<tr>");
				out.println("<td width='100%' colspan='2'><strong><small><font face='Verdana'>Mail Blaster Contol Panel</font></small></strong></td>");
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
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='100%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
				out.println("<tr>");
				out.println("<td width='97%'><small><font face='Verdana' color='#FFFFFF'><strong>Mail Reports</strong></font></small></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
				out.println("<tr>");
				out.println("<td width='33%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Mail Sent</font></small></small></center></td>");
				out.println("<td width='33%'><center><small><small><font face='Verdana'>Mail Opened</font></small></small></center></td>");  
				out.println("<td width='34%'><center><small><small><font face='Verdana'>Unsubscribed</font></small></small></center></td>");        	      	
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("<div align='right'>");
        		out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        		out.println("<tr>");
        		out.println("<td width='33%' valign='middle' align='center' bgcolor='#FFFFFF'><font face='Verdana' color='#8000FF'><small><small><strong>"+mail_sent+"</strong></small></small></font></td>");
        		out.println("<td width='33%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+mail_opened+"</strong></small></small></font></td>");
        		out.println("<td width='34%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+mail_unsub+"</strong></small></small></font></td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div>"); 
        		out.println("<br><br>");
        		rs.close();
        		out.println("<div align='right'>");
				out.println("<table border='1' width='100%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
				out.println("<tr>");
				out.println("<td width='97%'><small><font face='Verdana' color='#FFFFFF'><strong>Clickthrough Performance</strong></font></small></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div><div align='right'>");
				out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
				out.println("<tr>");
				out.println("<td width='10%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>S.No.</font></small></small></center></td>");
				out.println("<td width='70%'><center><small><small><font face='Verdana'>URL</font></small></small></center></td>");
				out.println("<td width='20%'><center><small><small><font face='Verdana'>No.of Visits</font></small></small></center></td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div>");    
        		for(int i=0;i<co;i++){
        			out.println("<div align='right'>");
        			out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        			out.println("<tr>");
        			out.println("<td width='10%' valign='middle' align='center' bgcolor='#FFFFFF'><font face='Verdana' color='#8000FF'><small><small><strong>"+(i+1)+"</strong></small></small></font></td>");        			
        			out.println("<td width='70%'valign='middle' ><font face='Verdana' color='#8000FF'><small><small><strong>"+urls[i]+"</strong></small></small></font></td>");
        			out.println("<td width='20%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small>"+link_visits[i]+"</small></small></font></td>");
        			out.println("</tr>");
        			out.println("</table>");
        			out.println("</div>");   
				} 
				out.println("</small></font></td>");
				out.println("</tr>");								
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>Total Links Visited In This campaign: "+total_clicks+"<br></small></font></td>");
				out.println("</tr>");
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to go to Main Panel Page<br><br></small></font></td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				out.println("</div>");
				out.println("</body>");
				out.println("</html>");                    
			}		
        }
        catch(Exception e){
        	try{
				out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				e.printStackTrace();
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
                
                

		
	