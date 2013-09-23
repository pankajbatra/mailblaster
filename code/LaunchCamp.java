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

public class LaunchCamp extends HttpServlet{
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
			int camp_previewed=0;
            int camp_tested=0;
            int camp_audi;
            int camp_schd=0;
            ResultSet rs = stmt.executeQuery("select * from mail_campaign where camp_id="+camp_id);
			if(rs.next()){
				camp_previewed=rs.getInt("camp_previewed");
				camp_tested=rs.getInt("camp_tested");
				camp_schd=rs.getInt("camp_schd");
				//camp_launch=rs.getInt("camp_launch");				
			}				
			rs.close();
			rs = stmt.executeQuery("select * from camp_list where camp_id="+camp_id);
			if(rs.next()){
				camp_audi=1;		
			}
			else{
				camp_audi=0;
				stmt.executeUpdate("Update from camp_list set camp_audi=0 where camp_id="+camp_id);
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
			out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
			out.println("<tr>");
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Verifying Pre-Launch Items..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>Campaign was <a href='PreviewCamp?camp_id="+camp_id+"'><font face='Verdana' color='##6767B4'>Previewed...</font></a>");
			if(camp_previewed==1) out.println(" Yes");
			else out.println(" No");
			out.println("</small></font></td>");
			out.println("</tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>Campaign was <a href='SendTestMail?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Tested...</font></a>");
			if(camp_tested==1) out.println(" Yes");
			else out.println(" No");
			out.println("</small></font></td>");
			out.println("</tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>Campaign was given <a href='SelectAudi?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Audience...</font></a>");
			if(camp_audi==1) out.println(" Yes");
			else out.println(" No");
			out.println("</small></font></td>");
			out.println("</tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>Campaign was <a href='ScheduleMail?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Scheduled...</font></a>");
			if(camp_schd==1) out.println(" Yes");
			else out.println(" No");
			out.println("</font></td>");
			out.println("</tr>");
			
			if(camp_schd==1&&camp_audi==1&&camp_tested==1&&camp_previewed==1){
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>");
				out.println("Everything checked out!<br>Click the \"Launch Campaign\" button below to finish launching this campaign.");
				out.println("</small></font></td></tr>");
				out.println("<form method='POST' action='LaunchCamp1'>");										
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><br><input type='submit' value='Launch Campaign!' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'> &nbsp; </td>");
				out.println("</tr>");			
        		out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to go back to Main Panel Page. <br><br></small></font></td>");
				out.println("</tr>");
				out.println("<INPUT TYPE='hidden' NAME='camp_id' VALUE='"+camp_id+"'>");
				out.println("</form>");
			}
			else{
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br>");
				out.println("You didn't complete all the pre-launch steps...<br>Please make sure that you have completed all of the above tasks.");
				out.println("</small></font></td></tr>");
				out.println("<tr align='center'>");
				out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to go back to main Page<br><br></small></font></td>");
				out.println("</tr>");				
			}
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");                  
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
                
                

		
	