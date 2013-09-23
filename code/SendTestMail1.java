import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class SendTestMail1 extends HttpServlet{
	private ConnectionPool pool;
	Properties props = new Properties();
    private Message message;
  	private Session session;
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
		String aduser;
		HttpSession session1=req.getSession(false);
		aduser = (String)session1.getValue("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session1.getLastAccessedTime());
		if(session1==null||aduser==null||accessed.before(time_comp)){
			session1.invalidate();
			out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='admin.htm'>Click Here</a> To Re-Login");
			return;
		}
		String temp_camp_id = req.getParameter("camp_id");
		String email=req.getParameter("email");
		String name=req.getParameter("name");
		int camp_id;
		try{
			camp_id=Integer.parseInt(temp_camp_id);	
		}
		catch(Exception e){
			out.println("<H2>NO Campaign Found</H2>");
		  	out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  	return;	
		}
		if(email ==null||name==null)
		{
		  out.println("<H2>Some of the fields may have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}
		if(email.equals("")||name.equals(""))
		{
		  out.println("<H2>Some of the fields have been left empty</H2>");
		  out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
		  return;		
		}
		if(checkmail(email));
        else{
            out.println("<H2>Invalid Email Address</H2>");
            out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & try again");
            return;
		}			
		try{
			String html_data="",from_name="",from_email="",mail_subject="",attach_file="";
			con = pool.getConnection();
			Statement stmt = con.createStatement();			
			ResultSet rs = stmt.executeQuery("select html_data,from_name,from_email,mail_subject,attach_field from mail_campaign where camp_id="+camp_id);
			if(rs.next()){
				html_data=rs.getString("html_data");
				from_name=rs.getString("from_name");
				from_email=rs.getString("from_email");
				mail_subject=rs.getString("mail_subject");
				attach_file=rs.getString("attach_field");
			}
			//boolean debug = false;
        	props.put("mail.smtp.auth","true");
        	session = Session.getInstance(props, null);
        	session.setDebug(true);
        	// construct the message
        	message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from_email, from_name == null ? "" : from_name));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email,false));
    	    message.setSubject(mail_subject);        
   			StringBuffer sbtemp=new StringBuffer(html_data);
   			int nametag=0;
   			int mailtag=0;
   			while(nametag!=-1){
	   			nametag=sbtemp.toString().indexOf("%name%");
   				if(nametag!=-1)sbtemp.replace(nametag,nametag+6,name);
   			}
   			while(mailtag!=-1){
	   			mailtag=sbtemp.toString().indexOf("%email%");
   				if(mailtag!=-1)sbtemp.replace(mailtag,mailtag+7,email);
   			}    
   			BodyPart messageBodyPart = new MimeBodyPart();
   			messageBodyPart.setContent(sbtemp.toString(),"text/html");
   			Multipart multipart = new MimeMultipart();
   			multipart.addBodyPart(messageBodyPart);
   			if(attach_file!=null)
          {
          	
             messageBodyPart = new MimeBodyPart();
             String fileName=attach_file;
             attach_file=getServletContext().getRealPath("")+"/attachments/"+camp_id+"_"+attach_file;
	
   			File file = new File(attach_file);        
        // Get the attachment
        DataSource source = new FileDataSource(file);
        // Set the data handler to the attachment
        messageBodyPart.setDataHandler(new DataHandler(source));
        // Set the filename
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);

        
    }
   	
   			message.setContent(multipart);   
    		message.setSentDate(new java.util.Date());
        	sendMessage();
        	stmt.executeUpdate("Update mail_campaign set camp_tested=1 where camp_id="+camp_id);
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Testing Campaign..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><font face='Verdana' color='#000080'><small><center><br>Test Mail Sent.<br><br></small></font></td>");
			out.println("</tr>");
			out.println("<tr align='center'>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to goto Main Panel Page. </small></font></td>");
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
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		doGet(req,res);
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
    public void sendMessage() throws Exception
    {
        message.saveChanges();
        Transport trans = session.getTransport("smtp");
        trans.connect("zaptive.com","zaptive","zpt@adm2007");
        //trans.connect(props.getProperty("mail.smtp.host"), props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.passwd"));
        trans.sendMessage(message, message.getAllRecipients());
        trans.close();
    }
}
                
                

		
	