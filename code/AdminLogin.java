import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminLogin extends HttpServlet{
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
  		res.setContentType("text/html");
  		PrintWriter out = res.getWriter();
  		HttpSession session = req.getSession(false);
  		String aduser,passwd;
  		try{
	  		aduser = req.getParameter("username");
			passwd = req.getParameter("passwd");
			if(aduser==null||passwd==null){
		  		out.println("<H2>Some of the Fields were left blank</H2>");
		  		out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				  return;
			}
			if(aduser.equals("")||passwd.equals("")){
				out.println("<H2>Some of the Fields were left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
			}	
//			Connection con=CustomConnectionPool.getConnection();
//			Statement stmt=con.createStatement();
//			ResultSet rs=stmt.executeQuery("Select * from admin where aduser = '"+aduser+"' AND adpasswd = '"+passwd+"'");
//			int c=0;
//			while(rs.next())
//			c++;
//			if(c==0){				
//				out.println("<H2>Invalid Username Or Password</H2>");
//				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
//				return;
//			}

            if(aduser.equals("admin")&&passwd.equals("passwd"));
			else{				
				out.println("<H2>Invalid Username Or Password</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Choose a Different User Name");
				return;
			}
			//rs.close(); 
			if(session!=null) session.invalidate();
			session=req.getSession(true);
			session.putValue("aduser",aduser);					
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Mail Blaster Control Panel</title>");
			out.println("</head>");
			out.println("<frameset framespacing='0' border='false' rows='64,20%' frameborder='0'>");
        	out.println("<frame name='banner' scrolling='no' noresize target='main' src='header.htm' marginwidth='0' marginheight='0'>");
        	out.println("<frameset cols='20%,80%'>");
        	out.println("<frame name='contents' target='main' src='AdminControlLeft' scrolling='no' marginwidth='0' marginheight='0' noresize>");
        	out.println("<frame name='main' scrolling='auto' marginwidth='0' marginheight='0' noresize src='AdminControlMiddle'>");
        	out.println("</frameset>");
        	out.println("<noframes>");
        	out.println("<body leftmargin='0' topmargin='0'>");
        	out.println("<p>This page uses frames, but your browser doesn't support them.</p>");
        	out.println("</body>");
        	out.println("</noframes>");
        	out.println("</frameset>");
        	out.println("</html>");
    	}
    	catch(Exception e){
    		try{
    			out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
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
			