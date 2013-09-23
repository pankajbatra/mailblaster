import com.oreilly.servlet.MultipartRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

public class EditCamp1 extends HttpServlet{
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
		String temp_camp_id = multi.getParameter("camp_id");
		int camp_id;
		try{
			camp_id=Integer.parseInt(temp_camp_id);	
		}
		catch(Exception e){
			out.println("<H2>NO Campaign Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}	
		String from_name = multi.getParameter("from_name");
		String from_email = multi.getParameter("from_email");
		String mail_subject = multi.getParameter("mail_subject");
		//mail_subject=mail_subject.replaceAll("'", "''");
		String html_data = multi.getParameter("html_data");
		String text_data = multi.getParameter("text_data");
		String unsubs_reqd=multi.getParameter("unsubs_reqd");
		String attach_file=null;
		if((from_name==null)||(from_email ==null)||(unsubs_reqd==null)||(mail_subject==null)||(html_data==null))//||(text_data==null))
		{
		  out.println("<H2>Some field or fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}		
		if(from_name.equals("")||unsubs_reqd.equals("")||from_email.equals("")||mail_subject.equals("")||html_data.equals(""))//||text_data.equals(""))
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
		String last_modi= sdf.format(date);	
		int unsubs=Integer.parseInt(unsubs_reqd);
		try{
			con = pool.getConnection();
            /*PreparedStatement pstmt = null;
            pstmt = con.prepareStatement("Update mail_campaign set last_modi='"+last_modi+"',html_data='"+html_data+"',text_data='"+text_data+"',from_name=?,from_email=?,mail_subject=? where camp_id="+camp_id);
            pstmt.clearParameters();
            //pstmt.setInt(1,camp_id);
            //pstmt.setString(2,camp_name);
            //pstmt.setString(3,last_modi);
            //pstmt.setString(4,launch_time);
            //pstmt.setBlob(5,(Blob)html_data);
            //pstmt.setBlob(6,(Blob)text_data);
            pstmt.setString(1,from_name);
            pstmt.setString(2,from_email);
            pstmt.setString(3,mail_subject);
            pstmt.executeUpdate();
            pstmt.close();
            */
			Enumeration file = multi.getFileNames();
			if(file.hasMoreElements())
			{
				String file1= (String)file.nextElement();
		    	File attach;
				attach = multi.getFile(file1);
				if(attach!=null)
				{							
					attach_file= attach.getName();
					attach.renameTo(new File(getServletContext().getRealPath("")+"/attachments/"+camp_id+"_"+attach.getName()));
				}
			}
            PreparedStatement pstmt = con.prepareStatement("Update mail_campaign set last_modi=?,html_data=?,text_data=?, from_name=?," +
                    "from_email=?,mail_subject=?,unsubs_reqd=?,attach_field=? where camp_id=?");
            pstmt.clearParameters();
            pstmt.setInt(9, camp_id);
            pstmt.setString(1,last_modi);
            pstmt.setString(2,html_data);
            pstmt.setString(3,text_data);
            pstmt.setString(4,from_name);
            pstmt.setString(5,from_email);
            pstmt.setString(6,mail_subject);
            pstmt.setInt(7,unsubs);
            pstmt.setString(8,attach_file);
            pstmt.executeUpdate();
//            if(attach_file!=null)
//				stmt.executeUpdate("Update mail_campaign set last_modi='"+last_modi+"',html_data='"+html_data.replace('\'',' ')+"',text_data='"+text_data.replace('\'',' ')+"', from_name='"+from_name+"',from_email='"+from_email+"',mail_subject='"+mail_subject+"',unsubs_reqd="+unsubs+",attach_field='"+attach_file+"' where camp_id="+camp_id);
//			else
//				stmt.executeUpdate("Update mail_campaign set last_modi='"+last_modi+"',html_data='"+html_data.replace('\'',' ')+"',text_data='"+text_data.replace('\'',' ')+"', from_name='"+from_name+"',from_email='"+from_email+"',mail_subject='"+mail_subject+"',unsubs_reqd="+unsubs+" where camp_id="+camp_id);
			//stmt.executeUpdate("Update mail_campaign set last_modi='"+last_modi+"',html_data='"+html_data.replace('\'',' ')+"',text_data='"+text_data.replace('\'',' ')+"', from_name='"+from_name+"',from_email='"+from_email+"',mail_subject='"+mail_subject+"',unsubs_reqd="+unsubs+" where camp_id="+camp_id);
			//values("+camp_id+",'"+camp_name+"','"+last_modi+"','"+launch_time+"','"+html_data.replace('\'',' ')+"','"+text_data.replace('\'',' ')+"',,,,0,0,0,0,0)");
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
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><br><br><br>Successfully Edited Campaign<br><br></small></font></td>");
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
    	if(atsign!=-1)return false;
    	return true;
    }
}
                
                

		
	