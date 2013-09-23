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

public class EditCamp extends HttpServlet{
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
			//String camp_name="";
			String html_data="";
			String text_data="";
			String from_name="";
			String from_email="";
			String mail_subject="";
			String file_name="";
            con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from mail_campaign where camp_id="+camp_id);
			if(rs.next()){
				//camp_name=
				html_data=rs.getString("html_data");
				text_data=rs.getString("text_data");
				from_name=rs.getString("from_name");
				from_email=rs.getString("from_email");
				mail_subject=rs.getString("mail_subject");
				file_name=rs.getString("attach_field");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Editing a Campaign..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'><form method='POST' action='EditCamp1' ENCTYPE='multipart/form-data'>");
			out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Please Change the required information in the form below to edit campaign.</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>&nbsp;From Name Label:</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='from_name' size='20' value='"+from_name+"'><small><font face='verdana'><small><small></samll></small></font></small></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>From Email Label:</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='from_email' size='20' value='"+from_email+"'><small><font face='verdana'><small><small></samll></small></font></small><br></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Subject of email:</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='mail_subject' size='20' value='"+mail_subject+"'><small><font face='verdana'><small><small></samll></small></font></small><br></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Unsubscribe Field Required:</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <select name='unsubs_reqd'><option value='1'>Yes</option> <option value='0'>No</option> </select><font face='verdana'><small><small></samll></small></font></small><br></td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>HTML Mail:</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><textarea rows='15' name='html_data' cols='70'>"+html_data+"</textarea><br></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Text Mail :</small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><textarea rows='15' name='text_data' cols='70'>"+text_data+"</textarea><br></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
		    out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		    out.println("</tr>");
		    if(file_name!=null)
		    {	
		    out.println("<tr align='center'>");
		    out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana'><a href='DownloadAttachment?id="+camp_id+"&file="+file_name+"' target='_blank'><img src='images/download.gif'></a></font>");
		    //out.println("</tr>");
	       // out.println("<tr align='center'>");
		    out.println("<a href='DeleteAttachment?id="+camp_id+"&file="+file_name+"' target='_self'><img src='images/delete1.gif'></a></font></td>");
		    out.println("</tr>");
		    }
		  else
		  {
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='file' name='attachment'></td>");
			out.println("</tr>");
		  }
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");				
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
			out.println("</tr>");		
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type='hidden' name='camp_id' value="+camp_id+">");
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
                
                

		
	