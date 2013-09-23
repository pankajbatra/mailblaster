import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ScheduleMail extends HttpServlet{
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		res.setHeader("Cache-Control","no-store");
		//Connection con = null;
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
			//con = pool.getConnection();
			//Statement stmt = con.createStatement();
			//ResultSet rs = null;
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Scheduling a Campaign..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<form method='POST' action='ScheduleMail2'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><br><br><br>You are about to Schedule Mail in a campaign<br><br></small></font></td>");
			out.println("</tr>");
			out.println("<tr>");
			java.util.Date now=new java.util.Date();
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br>Current Time is: "+(now.getMonth()+1)+"-"+now.getDate()+"-"+(now.getYear()+1900)+" "+now.getHours()+":"+now.getMinutes()+"<br></small></font></td>");
			out.println("</tr>");			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small>Set Your Launch Date:");
			out.println("<select name=month>");
			out.println("<option value=''>Month</option>");
			out.println("<option  value='1'>Jan</option>");
			out.println("<option  value='2'>Feb</option>");
			out.println("<option  value='3'>Mar</option>");
			out.println("<option  value='4'>Apr</option>");
			out.println("<option  value='5'>May</option>");
			out.println("<option  value='6'>Jun</option>");
			out.println("<option  value='7'>Jul</option>");
			out.println("<option  value='8'>Aug</option>");
			out.println("<option  value='9'>Sep</option>");
			out.println("<option  value='10'>Oct</option>");
			out.println("<option  value='11'>Nov</option>");
			out.println("<option  value='12'>Dec</option>");
			out.println("</select>&nbsp;&nbsp;<select name=day>");
			out.println("<option value=''>Day</option>");
			out.println("<option  value='1'>1</option>");
			out.println("<option  value='2'>2</option>");
			out.println("<option  value='3'>3</option>");
			out.println("<option  value='4'>4</option>");
			out.println("<option  value='5'>5</option>");
			out.println("<option  value='6'>6</option>");
			out.println("<option  value='7'>7</option>");
			out.println("<option  value='8'>8</option>");
			out.println("<option  value='9'>9</option>");
			out.println("<option  value='10'>10</option>");
			out.println("<option  value='11'>11</option>");
			out.println("<option  value='12'>12</option>");
			out.println("<option  value='13'>13</option>");
			out.println("<option  value='14'>14</option>");
			out.println("<option  value='15'>15</option>");
			out.println("<option  value='16'>16</option>");
			out.println("<option  value='17'>17</option>");
			out.println("<option  value='18'>18</option>");
			out.println("<option  value='19'>19</option>");
			out.println("<option  value='20'>20</option>");
			out.println("<option  value='21'>21</option>");
			out.println("<option  value='22'>22</option>");
			out.println("<option  value='23'>23</option>");
			out.println("<option  value='24'>24</option>");
			out.println("<option  value='25'>25</option>");
			out.println("<option  value='26'>26</option>");
			out.println("<option  value='27'>27</option>");
			out.println("<option  value='28'>28</option>");
			out.println("<option  value='29'>29</option>");
			out.println("<option  value='30'>30</option>");
			out.println("<option  value='31'>31</option>");
			out.println("</select>&nbsp;&nbsp;<select name=year>");
			out.println("<option value=''>Year</option>");
			out.println("<option  value='2007'>2007</option>");
			out.println("<option  value='2008'>2008</option>");
			out.println("<option  value='2009'>2009</option>");
			out.println("</select></small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small>Set Your Launch Time:");						
			out.println("<select name=hour>");
			out.println("<option value=''>Hour</option>");
			out.println("<option  value='1'>1</option>");
			out.println("<option  value='2'>2</option>");
			out.println("<option  value='3'>3</option>");
			out.println("<option  value='4'>4</option>");
			out.println("<option  value='5'>5</option>");
			out.println("<option  value='6'>6</option>");
			out.println("<option  value='7'>7</option>");
			out.println("<option  value='8'>8</option>");
			out.println("<option  value='9'>9</option>");
			out.println("<option  value='10'>10</option>");
			out.println("<option  value='11'>11</option>");
			out.println("<option  value='12'>12</option>");
			out.println("<option  value='13'>13</option>");
			out.println("<option  value='14'>14</option>");
			out.println("<option  value='15'>15</option>");
			out.println("<option  value='16'>16</option>");
			out.println("<option  value='17'>17</option>");
			out.println("<option  value='18'>18</option>");
			out.println("<option  value='19'>19</option>");
			out.println("<option  value='20'>20</option>");
			out.println("<option  value='21'>21</option>");
			out.println("<option  value='22'>22</option>");
			out.println("<option  value='23'>23</option>");
			out.println("<option  value='00'>00</option>");
			out.println("</select>:<select name=min>");
			out.println("<option value=''>Min</option>");
			out.println("<option  value='00'>00</option>");
			out.println("<option  value='05'>05</option>");
			out.println("<option  value='10'>10</option>");
			out.println("<option  value='15'>15</option>");
			out.println("<option  value='20'>20</option>");
			out.println("<option  value='25'>25</option>");
			out.println("<option  value='30'>30</option>");
			out.println("<option  value='35'>35</option>");
			out.println("<option  value='40'>40</option>");
			out.println("<option  value='45'>45</option>");
			out.println("<option  value='50'>50</option>");
			out.println("<option  value='55'>55</option>");
			out.println("</select>");	
			out.println("<br><br></small></font></td>");
			out.println("</tr>");							
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><input type='submit' value='Schedule' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'> &nbsp; </td>");
			out.println("</tr>");			
			out.println("</table>");
			out.println("<INPUT TYPE='hidden' NAME='camp_id' VALUE='"+camp_id+"'>");
			out.println("</form>");
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
				//con.rollback();				
			}
			catch (Exception ignored) { }
		}
		finally{
			
				//if(con!=null) pool.returnConnection(con);
				out.close();		
		}
	}
}
                
                

		
	