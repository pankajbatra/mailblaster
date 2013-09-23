import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateLinkCount extends HttpServlet{
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
		String temp_camp_id = req.getParameter("camp_id");
		int camp_id;
		try{
			camp_id=Integer.parseInt(temp_camp_id);	
		}
		catch(Exception e){
			out.println("<H2>Invalid Link Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}
		String temp_link_no = req.getParameter("link_no");
		int link_no;
		try{
			link_no=Integer.parseInt(temp_link_no);	
		}
		catch(Exception e){
			out.println("<H2>Invalid Link Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}				
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			int updt=stmt.executeUpdate("Update camp_links set no_of_visits=no_of_visits+1 where link_no="+link_no+" AND camp_id="+camp_id);
			if(updt==0){
				out.println("<H2>Invalid Link Found</H2>");
		  		out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  		return;
			}
			rs=stmt.executeQuery("select link_url from camp_links where link_no="+link_no+" AND camp_id="+camp_id);
			if(rs.next()){
				String link_url=rs.getString("link_url");
				out.println("<html>");
				out.println("<head>");
				out.println("<title></title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<script language='javascript'> document.location='"+link_url+"' </script>");
				out.println("</body>");
				out.println("</html>"); 	
			}
			else{
				out.println("<H2>Invalid Link Found</H2>");
		  		out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
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
                
                

		
	