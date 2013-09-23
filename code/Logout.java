import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class Logout extends HttpServlet{
	public void init(ServletConfig config) throws ServletException{
		super.init(config);		
	}	
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		HttpSession session=req.getSession(false); 		
		PrintWriter out=res.getWriter();		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>LogOut</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY>");		
		if(session!=null)
		  session.invalidate();
		out.println("<body>");
		out.println("<Script language='javascript'>document.location='logout.htm'</script>");
		out.println("</BODY></HTML>");
		out.close();
	}  
}