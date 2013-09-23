import com.oreilly.servlet.MultipartRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;


public class BuildCampaign2 extends HttpServlet{	
public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException
	{
		res.setContentType("text/html");
		res.setHeader("Cache-Control","no-store");
		PrintWriter out=res.getWriter();
		MultipartRequest multi = new MultipartRequest(req, getServletContext().getRealPath("")+"/temp");
			
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
	/*	try{
		Enumeration file = multi.getFileNames();
		if(file==null)
		out.println("");
	}
	catch(Exception Ignored)
	{
	}
		String file1= (String)file.nextElement();
		    
			File attach = multi.getFile(file1);
			String html_file= attach.getName();*/
		   String camp_name=multi.getParameter("camp_name");
			String from_name=multi.getParameter("from_name");
			String from_email=multi.getParameter("from_email");
			String mail_subject=multi.getParameter("mail_subject");

        if(camp_name==null)
			camp_name="";
        if(from_name==null)
            from_name="";
        if(from_email==null)
            from_email="";
        if(mail_subject==null)
            mail_subject="";
			
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
		out.println("<table border='0' width='92%' height='20' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<td width='100%'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' bgcolor='#C0C0C0' bordercolor='#C0C0C0' cellspacing='0' cellpadding='0' bordercolorlight='#C0C0C0'>");
		out.println("<tr>");
		out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Create a New Campaign..</strong></small></font></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div><div align='right'>");
		out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
		out.println("<tr>");
		out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'><form method='POST' action='BuildCampaign1' ENCTYPE='multipart/form-data' >");
		out.println("<table border='0' width='79%' cellspacing='0' cellpadding='0' height='161' <tr>");
		out.println("<tr>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Please Fill the form below to create a new campaign.</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>&nbsp; Enter the name for your new campaign:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='camp_name' size='20' value='"+camp_name+"'><small><font face='verdana'><small><small>*Maximum 25 chars</samll></small></font></small><br></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>&nbsp;Enter the From Name Label:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='from_name' size='20' value='"+from_name+"'><small><font face='verdana'><small><small></samll></small></font></small></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Enter the From Email Label:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='from_email' size='20' value='"+from_email+"'><small><font face='verdana'><small><small></samll></small></font></small><br></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Enter the Subject of email:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <input type='text' name='mail_subject' size='20' value='"+mail_subject+"'><small><font face='verdana'><small><small></samll></small></font></small><br></td>");
		out.println("</tr>");
		out.println("<input type='hidden' name='mail_subject' value='"+mail_subject+"'>");		
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Unsubscribe Field Required:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'>&nbsp; <select name='unsubs_reqd'><option value='1'>Yes</option> <option value='0'>No</option> </select><font face='verdana'><small><small></samll></small></font></small><br></td>");
		out.println("</tr>");		
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small> Your File is Below:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
			String text="";  
		try
	
		{
		Enumeration file = multi.getFileNames();
		String file1= (String)file.nextElement();
		    
			File attach = multi.getFile(file1);
			
	
		if(attach!=null)
		{
			System.out.println(attach.getAbsolutePath());
	     	BufferedReader fin = new BufferedReader(new FileReader(attach));	    
        	while(true)        
        	{
	        	String buf = fin.readLine(); 
          		if (buf == null) 
          			break;         
	          	text = text + buf;
	        }   
	        attach.delete();    
     	} 
        }
        catch(Exception ignored)
        {
        }
         
        out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><textarea rows='15' name='html_data' cols='70'>"+text+"</textarea><br></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Enter Your Text Mail Below:</small></font></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><textarea rows='15' name='text_data' cols='70'></textarea><br></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");	
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small>Attach File</small></font></td>");
		out.println("</tr>");
	    out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='file'  name='attach'></td>");
		out.println("</tr>");
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");	
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><input type='submit' value='Submit' name='B1' style='background-color: rgb(196,196,255); color: rgb(0,0,0); border: 1px solid rgb(0,0,128)'></td>");
		out.println("</tr>");			
		out.println("<tr align='center'>");
		out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</form>");
		out.println("</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}
}
	
	