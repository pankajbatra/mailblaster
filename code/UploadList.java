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

public class UploadList extends HttpServlet{
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
	throws ServletException,IOException
	{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
		Connection con = null;
		String aduser;
		aduser = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			    session.invalidate();
			    out.println("<H2>Your Session has expired </H2>");
				out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
				return;
		}
		out.println("<html>");
		out.println("<head>");
		out.println("<title></title>");
		out.println("</head>");
		out.println("<body topmargin='0' leftmargin='0' link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF'>");
		out.println("<table border='0' width='102%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='24'>");
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
		out.println("<table border='0' width='92%' height='20' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<td width='100%'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
		out.println("<tr>");
		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Upload a list..</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		out.println("<tr>");
		out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");		
		out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' <tr>");
		
		out.println("<form method='POST' action='UploadList1' name='f2'>");
		out.println("<tr>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small><br><br>Please choose ONE of the following options:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small><br><br>Option 1: Create a new list:<br>Give this list a name so you can identify it later:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><input type='text' name='list_name' size='20'><small><font face='verdana'><small><small>*Maximum 25 chars</samll></small></font></small><br></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
		out.println("</tr>");
		out.println("</form>");
		out.println("<form method='POST' action='UploadList2' name='f1'>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small><br><br>Option 2: Append to an existing list:<br>Select the list you wish to add to here:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><select name=list_id>");
		out.println("<option value=''>-- select --</option>");
		try{			
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from global_list");
			while(rs.next()){
					out.println("<option value="+rs.getInt("list_id")+">"+rs.getString("list_name")+"</option>");				
			}			
			out.println("</select><br></td>");			
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='submit' value='Submit' name='B2' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'><br><br><br></td>");
			out.println("</tr>");
			out.println("</form>");		
			out.println("</tr>");
			out.println("</table>");
			out.println("</td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
			rs.close();
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
	
	