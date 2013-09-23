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

public class ViewList extends HttpServlet{
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
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		String ad_user;
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
				return;
		}
		String temp_list_id = req.getParameter("list_id");
		int list_id;
		try{
			list_id=Integer.parseInt(temp_list_id);	
		}
		catch(Exception e){
			out.println("<H2>NO List Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}		
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			//ResultSet rs=null;
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
			out.println("<table border='1' width='100%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='97%'><small><font face='Verdana' color='#FFFFFF'><strong>List Contents:</strong></font></small></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F3F3F3' height='20'>");
			out.println("<tr>");
			out.println("<td width='10%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>S.No.</font></small></small></center></td>");
			out.println("<td width='35%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Name</font></small></small></center></td>");
			out.println("<td width='35%'><center><small><small><font face='Verdana'>Email Address</font></small></small></center></td>");
			out.println("<td width='20%' bgcolor='#F3F3F3'><center><small><small><font face='Verdana'>Unsubscribed</font></small></small></center></td>");
        	out.println("</tr>");
        	out.println("</table>");
        	out.println("</div>");
        	int co=0;
            ResultSet rs = stmt.executeQuery("Select * from list_member where list_id="+list_id);
       		while(rs.next()){
        		String name = rs.getString("member_name");   
        		String email = rs.getString("member_email"); 
        		int unsub = rs.getInt("member_unsub"); 
        		out.println("<div align='right'>");
        		out.println("<table border='1' width='100%' cellspacing='0' cellpadding='0' bgcolor='#F0F0FF' bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF'>");
        		out.println("<tr>");
        		out.println("<td width='10%' valign='middle' align='center' bgcolor='#FFFFFF'><font face='Verdana' color='#8000FF'><small><small><strong>"+(co+1)+"</strong></small></small></font></td>"); 		
        		out.println("<td width='35%' valign='middle' align='center' bgcolor='#FFFFFF'><font face='Verdana' color='#8000FF'><small><small><strong>"+name+"</strong></small></small></font></td>");
        		out.println("<td width='35%'valign='middle' align='center'><font face='Verdana' color='#8000FF'><small><small><strong>"+email+"</strong></small></small></font></td>");
        		out.println("<td width='25%' valign='middle' align='center' bgcolor='#FFFFFF'><font face='Verdana' color='#8000FF'><small><small><strong>");
        		if(unsub==0)out.println("No");
        		else out.println("Yes");
        		out.println("</strong></small></small></font></td>");
        		out.println("</tr>");
        		out.println("</table>");
        		out.println("</div>");  
        		co++;      		        		
        	}        	
        	out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><a href='ViewUnsub?list_id="+list_id+"'><font face='Verdana' color='#6767B4'>Click here</font></a> to View Unsubscribed Members. </small></font></td>");
			out.println("</tr>");
        	out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to goto Main Panel Page. </small></font></td>");
			out.println("</tr>");
        	out.println("<br><br>");
        	rs.close();
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
