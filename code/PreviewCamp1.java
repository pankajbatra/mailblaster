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

public class PreviewCamp1 extends HttpServlet{
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
		Connection con;
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
			String html=null;
            ResultSet rs = stmt.executeQuery("select html_data,text_data from mail_campaign where camp_id="+camp_id);
			if(rs.next()){
				html=rs.getString("html_data");
            }
			stmt.executeUpdate("Update mail_campaign set camp_previewed=1 where camp_id="+camp_id);
			out.println(html);
			//out.println(text);			           
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
                
                

		
	