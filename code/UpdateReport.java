import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateReport extends HttpServlet{
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
		/*boolean isvisit=false;
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("report")) {
					isvisit=true;
					break;
				}
			}
		}*/
		Connection con = null;
		/*if(cookies==null||isvisit==false){
			Cookie cookie = new Cookie("report", "1");
			cookie.setMaxAge(60*60*24*10);
			res.addCookie(cookie);
			*/
			
			String temp_camp_id = req.getParameter("camp_id");
			int camp_id;
			if(temp_camp_id==null)camp_id=0;
			else{
				try{
					camp_id=Integer.parseInt(temp_camp_id);	
				}
				catch(Exception e){
					camp_id=0;
					//throw new UnavailableException(this,"Could not find this Image");
				}
			}
			String member_email=req.getParameter("email");	
			if(member_email==null)member_email=" ";		
			try{
				con = pool.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select * from camp_indi_report where camp_id="+camp_id+" AND member_email='"+member_email+"'");
				if(rs.next());
				else{
                rs = stmt.executeQuery("select * from mail_campaign where camp_id="+camp_id);
                if(rs.next()&&checkmail(member_email)){
                    stmt.executeUpdate("Update camp_report set mail_opened=mail_opened+1 where camp_id="+camp_id);
                    int updt=stmt.executeUpdate("Insert into camp_indi_report values ("+camp_id+",'"+member_email+"')");
                    if(updt==0){
                        //throw new UnavailableException(this,"Could not find this Image");
                    }
                }
				}				
        	}
        	catch(Exception e){
	        	try{
        			//throw new UnavailableException(this,"Could not find this Image");
				}
				catch (Exception ignored) { }
			}
			finally{				
				if(con!=null) pool.returnConnection(con);
			}
		//}
		//return image
		ServletOutputStream out=res.getOutputStream();	
		//String file = req.getPathTranslated();
//		String file="/home/rishi/tomcat/jakarta-tomcat-4.0.1/webapps/mailblaster/logo.gif";
        String file=getServletContext().getRealPath("images/logo.gif");
        if (file == null) {
	     	//throw new UnavailableException(this,"Could not find this Image");
    	}
		String contentType = getServletContext().getMimeType(file);
		if(contentType==null){
			//throw new UnavailableException(this,"Could not find this Image");
		}    	
		try {
			res.setContentType(contentType);
	    	returnFile(file, out);
    	}
    	catch (Exception e) { 
	     	//throw new UnavailableException(this,"Could not find this Image");
    	} 
		out.flush();
		out.close();						
	}
	public static void returnFile(String filename, OutputStream out) throws IOException {
		FileInputStream fis = null;
		try {
    		fis = new FileInputStream(filename);
    		byte[] buf = new byte[4 * 1024];  // 4K buffer
    		int bytesRead;
    		while ((bytesRead = fis.read(buf)) != -1) {
      			out.write(buf, 0, bytesRead);
    		}
  		}
  		catch(Exception e){ }
  		finally {
    		if (fis != null) fis.close();
  		}
  	}
  	public boolean checkmail(String value) {
  		int atsign,lastsign;
  		atsign=value.indexOf("@");
  		lastsign=value.lastIndexOf("@");
  		if(atsign==-1||lastsign!=atsign) return false;
  		atsign=value.lastIndexOf(".");
  		if(atsign==-1) return false;
  		atsign=value.indexOf(" ");
        return atsign == -1;
      }
}
                
                

		
	