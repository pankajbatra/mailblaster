import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ExportList2 extends HttpServlet{
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
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		Connection con = null;
		//res.setContentType("text/html");
		//res.setHeader("Cache-Control","no-store");
		//PrintWriter out=res.getWriter();
		HttpSession session=req.getSession(false);		
		String ad_user;
		ad_user = (String)session.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||ad_user==null||accessed.before(time_comp)){
			    session.invalidate();
			    //out.println("<H2>Your Session has expired </H2>");
				//out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
				return;
		}
		String temp_list_id = req.getParameter("list_id");
		int list_id;
		try{
			list_id=Integer.parseInt(temp_list_id);	
		}
		catch(Exception e){
			//out.println("<H2>NO List Found</H2>");
		  	//out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}		
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			File file=new File("temp.csv");
			FileWriter fout = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fout);
			//String line="";
			bw.write("Name,Email\r\n");			
			ResultSet rs = stmt.executeQuery("Select * from list_member where list_id="+list_id);
       		while(rs.next()){
        		String name = rs.getString("member_name");   
        		String email = rs.getString("member_email");         				
        		bw.write(name+","+email+"\r\n");
        	}
        	bw.close();
		    fout.close();
        	rs.close();
        	//String fname=file.getName();
        	//String contentType = getServletContext().getMimeType(fname);
        	//System.out.println(contentType);
    		res.setContentType("application/csv");
    		ServletOutputStream out = res.getOutputStream();
        	FileInputStream fis = new FileInputStream("temp.csv");
    		byte[] buf = new byte[4 * 1024];  // 4K buffer
    		int bytesRead;
    		while ((bytesRead = fis.read(buf)) != -1) {
      		out.write(buf, 0, bytesRead);
    		}
    		out.close();
        }
        catch(Exception e){
        	try{
				//out.println("<H2>An Error has occured: "+e.getMessage()+"</H2>");
				e.printStackTrace();
				//out.println("<br><br><a href='javascript:history.go(-1)'>Click Here</a> to go back to previous page & Try Again");
				con.rollback();				
			}
			catch (Exception ignored) { }
		}
		finally{
			if(con!=null) pool.returnConnection(con);
			//out.close();		
		}
	}
}
