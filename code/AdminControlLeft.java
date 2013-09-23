import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminControlLeft extends HttpServlet{
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);
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
        out.println("<title>Home</title>");
        out.println("<base target='main'>");
        out.println("</head>");
        out.println("<body topmargin='0' leftmargin='0' link='#000080'>");
        out.println("<div align='left'>");
        out.println("<table border='0' width='100%' cellspacing='0' cellpadding='0' bgcolor='#000000' height='20'>");
        out.println("<tr>");
        out.println("<td width='50%' valign='middle' align='left'>&nbsp;<img src='images/home.gif' width='17' height='18' alt='Go To Home' align='center'> <a href='AdminControlMiddle' target='main'><font face='Verdana' color='#FFFFFF'><small>Home</small></font></a></td>");
        out.println("<td width='50%' valign='middle' align='left'><img src='images/logout.gif' width='17' height='18' alt='Logout.' align='center'> <a href='Logout' target='_top'><font face='Verdana' color='#FFFFFF'><small>Logout</small></font></a></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</div>");
        out.println("<table border='0' width='98%' height='10' cellspacing='0' cellpadding='0'>");
        out.println("<tr>");
        out.println("<td width='100%'></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br><br>");
        out.println("<table border='1' width='90%' height='20' cellspacing='0' cellpadding='0'bordercolor='#C0C0C0' bordercolorlight='#C0C0C0' bordercolordark='#FFFFFF' bgcolor='#F0F0FF'>");
        if(aduser.equals("admin")){
        	out.println("<tr>");
        	out.println("<td width='100%'><center><a href='BuildCampaign' target='main'><font face='Verdana' color='#000080'><small>Build a Campaign</small></font></a></center></td></tr>");
        	out.println("<tr>");
        	out.println("<td width='100%'><center><a href='UploadList' target='main'><font face='Verdana' color='#000080'><small>Upload a List</small></font></a></center></td></tr>");        	        	
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");		 
	}
}
	