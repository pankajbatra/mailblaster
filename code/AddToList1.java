import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.text.*;

public class AddToList1 extends HttpServlet{
	private ConnectionPool pool;
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);
		}
		catch(Exception e){
            e.printStackTrace();
            throw new UnavailableException(this,"could not create a connection");
	    }
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
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
		String temp_list_id = req.getParameter("list_id");
		int list_id;
		try{
			list_id=Integer.parseInt(temp_list_id);	
		}
		catch(Exception e){
			out.println("<H2>NO List Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}	
		String email=req.getParameter("email");
		String name=req.getParameter("name");
		if(email==null||name==null)
		{
		  out.println("<H2>Some of the fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}		
		if(email.equals("")||name.equals(""))
		{
		  out.println("<H2>Some of the fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;
		}
		if(checkmail(email));
		else{
			out.println("<H2>Invalid Email Addres</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;
		}	
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs=stmt.executeQuery("select * from list_member where member_email='"+email+"' AND list_id="+list_id);
			if(rs.next()){
				out.println("<H2>Email Id already Exists.</H2>");
				out.println("<a href='javascript:history.go(-2)'>Click Here</a>  to go back to previous page & try again");
				return;
			}
			java.util.Date date = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String last_modi= sdf.format(date);	
			stmt.executeUpdate("Update global_list set modi_date='"+last_modi+"' where list_id="+list_id);			
			stmt.executeUpdate("Insert into list_member values("+list_id+",'"+name+"','"+email+"',0)");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Edited a List..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><br><br><br>Successfully Added an Account<br><br></small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to go to Main Panel Page</small></font></td>");
			out.println("</tr>");
			out.println("</table>");			
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
	public boolean checkmail(String value) {
    	int atsign,lastsign;
    	atsign=value.indexOf("@");
    	lastsign=value.lastIndexOf("@");    	
    	if(atsign==-1||lastsign!=atsign) return false;    	
    	atsign=value.lastIndexOf(".");
    	if(atsign==-1) return false;
    	/*int len=value.length();
    	if(atsign==len-2);
    	else{
    		String domain=value.substring(atsign+1);
    		//if(domain.equals("com")||domain.equals("COM")||domain.equals("NET")||domain.equals("ORG")||domain.equals("net")||domain.equals("org")||domain.equals("edu")||domain.equals("EDU")||domain.equals("int")||domain.equals("INT")||domain.equals("MIL")||domain.equals("mil")||domain.equals("gov")||domain.equals("GOV")||domain.equals("arpa")||domain.equals("ARPA")||domain.equals("biz")||domain.equals("BIZ")||domain.equals("aero")||domain.equals("name")||domain.equals("coop")||domain.equals("info")||domain.equals("INFO")||domain.equals("pro")||domain.equals("museum"));
    		//else return false;
    	}
    	*/
    	atsign=value.indexOf(" ");
        return atsign == -1;
    }
}
                
                

		
	