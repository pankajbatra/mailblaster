import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import com.oreilly.servlet.MultipartRequest;


public class BuildCampaign1 extends HttpServlet{
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
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		MultipartRequest multi = new MultipartRequest(req,getServletContext().getRealPath("")+"/attachments");		
		res.setHeader("Cache-Control","no-store");
		//MultipartRequest req = new MultipartRequest(request, ".");
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
		String camp_name = multi.getParameter("camp_name");
		String from_name = multi.getParameter("from_name");
		String from_email = multi.getParameter("from_email");
		String mail_subject = multi.getParameter("mail_subject");
		String html_data = multi.getParameter("html_data");
		String text_data = multi.getParameter("text_data");
		String unsubs_reqd=multi.getParameter("unsubs_reqd");
        String attach_file=null;		
        if((camp_name ==null)||(from_name==null)||(unsubs_reqd==null)||(from_email ==null)||(mail_subject==null)||(html_data==null))//||(text_data==null))
		{
		  out.println("<H2>Some field or fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}		
		if(camp_name.equals("")||from_name.equals("")||from_email.equals("")||mail_subject.equals("")||unsubs_reqd.equals("")||html_data.equals(""))//||text_data.equals(""))
		{
		  out.println("<H2>Some field or fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;
		}
		if(checkmail(from_email));
			else{
				out.println("<H2>Invalid Email Address</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
				return;
		}			
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int camp_id;
		String last_modi= sdf.format(date);	
		java.util.Date ldate = new java.util.Date(0,0,1);
		SimpleDateFormat lsdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String launch_time=lsdf.format(ldate);
		int unsubs=Integer.parseInt(unsubs_reqd);
		try{
			con = pool.getConnection();
			Statement stmt = con.createStatement();
            PreparedStatement pstmt = con.prepareStatement("insert into mail_campaign values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ResultSet rs = stmt.executeQuery("Select * from mail_campaign");
			int c=0;
			while(rs.next())
			{
				c++;
			}
			rs.close();
			if(c==0){
				camp_id = 1;
			}
			else{
				rs=stmt.executeQuery("select MAX(camp_id) from mail_campaign");
				while(rs.next()){
					c=rs.getInt("MAX(camp_id)");
				}
				rs.close();
				camp_id= ++c;
			}
			Enumeration file = multi.getFileNames();
			String file1= (String)file.nextElement();
		    
			File attach;
			attach = multi.getFile(file1);
			
			if(attach==null){
				//out.println("");
				//out.println("<a href='javascript:history.go(-1)'>Click Here</a>to go back to previous page & Choose a CSV File to Upload");
				//return;
			}
			else
			{								
				attach_file= attach.getName();
				//System.out.println(attach.getAbsolutePath());
				attach.renameTo(new File(getServletContext().getRealPath("")+"/attachments/"+camp_id+"_"+attach.getName()));
			}
				
			//out.println("attach file is" +attach_file);
			//out.println("full name is" +attach_file);
								
				
//		if(attach_file!=null)
//        {
            pstmt.clearParameters();
            pstmt.setInt(1, camp_id);
            pstmt.setString(2,camp_name);
            pstmt.setString(3,last_modi);
            pstmt.setString(4,launch_time);
            pstmt.setString(5,html_data);
            pstmt.setString(6,text_data);
            pstmt.setString(7,from_name);
            pstmt.setString(8,from_email);
            pstmt.setString(9,mail_subject);
            pstmt.setInt(10,0);
            pstmt.setInt(11,0);
            pstmt.setInt(12,0);
            pstmt.setInt(13,0);
            pstmt.setInt(14,0);
            pstmt.setInt(15,unsubs);
            pstmt.setString(16,attach_file);
            pstmt.executeUpdate();
//            stmt.executeUpdate("insert into mail_campaign values("+camp_id+",'"+camp_name+"','"+last_modi+"','"+launch_time+"','"+html_data.replace('\'',' ')+"'," +
//                    "'"+text_data.replace('\'',' ')+"','"+from_name+"','"+from_email+"','"+mail_subject+"',0,0,0,0,0,'"+unsubs+"','"+attach_file+"')");
//        }
//        else
//		stmt.executeUpdate("insert into mail_campaign values("+camp_id+",'"+camp_name+"','"+last_modi+"','"+launch_time+"','"+html_data+"','"+text_data.replace('\'',' ')+"','"+from_name+"','"+from_email+"','"+mail_subject+"',0,0,0,0,0,'"+unsubs+"',null)");
			//System.out.println(html_data);
			/*pstmt = con.prepareStatement("insert into mail_campaign values(?,?,'"+last_modi+"','"+launch_time+"','"+html_data+"','"+text_data+"',?,?,?,0,0,0,0,0)");
			pstmt.clearParameters();
			pstmt.setInt(1,camp_id);
			pstmt.setString(2,camp_name);
			//pstmt.setString(3,last_modi);
			//pstmt.setString(4,launch_time);
			//pstmt.setBlob(5,(Blob)html_data);
			//pstmt.setBlob(6,(Blob)text_data);
			pstmt.setString(3,from_name);
			pstmt.setString(4,from_email);
			pstmt.setString(5,mail_subject);			
            pstmt.executeUpdate();
			pstmt.close();*/
			//stmt.executeUpdate("insert into mail_campaign values("+camp_id+",'"+camp_name+"','"+last_modi+"','"+launch_time+"','"+html_data.replace('\'',' ')+"','"+text_data.replace('\'',' ')+"','"+from_name+"','"+from_email+"','"+mail_subject+"',0,0,0,0,0,"+unsubs+")");
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Building a new Campaign..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><br><br><br>Created a New campaign: "+camp_name+"</small></font></td>");
			out.println("</tr>");		
			
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><a href='PreviewCamp?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Preview Campaign</font></a> </small></font></td>");
			out.println("</tr>");
			
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><a href='SendTestMail?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Send Test Mail</font></a> </small></font></td>");
			out.println("</tr>");
			
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><a href='SelectAudi?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Select Audience</font></a> </small></font></td>");
			out.println("</tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><a href='ScheduleMail?camp_id="+camp_id+"'><font face='Verdana' color='#6767B4'>Schedule Mail</font></a> </small></font></td>");
			out.println("</tr>");
			
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p>Or if you don't want at this time <br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> </small></font></td>");
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
                
                

		
	