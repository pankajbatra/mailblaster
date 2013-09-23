import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class MailBlaster extends HttpServlet implements Runnable {
  	Thread runner;
	private ConnectionPool pool;
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	Properties props = new Properties();
    private Message message;
  	private Session session;

    private static final String SERVER_NAME="http://www.zaptive.com:8181/mailblaster";
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try{
			pool = new ConnectionPool(1,1);			
	    	con = pool.getConnection();
			stmt = con.createStatement();
			rs = null;	
		}
		catch(Exception e){
			throw new UnavailableException(this,"could not create a connection");
	    }
        try{
        	//FileInputStream is = new FileInputStream(propFileName);
        	//props.load(is);
        	props.put("mail.smtp.auth","true");
        	session = Session.getInstance(props, null);
        	session.setDebug(true);
        	// construct the message
        	message = new MimeMessage(session);
    	}    	
        catch(Exception e){			
				e.printStackTrace();
		}	
		System.out.println("Mail Blaster Started");
	    runner = new Thread(this);
	    runner.setPriority(Thread.MIN_PRIORITY);  
	    runner.start();
  	}
  	public void run() { 
		try{
			while (true) {
				//if camp_lauch=1 & launch time before time now & not sent yet(camp_report does not have camp_id=this id)
				//then send to all by replacing all links with new one and adding image at end with url that return image & update count.
				//make entries in camp_report,camp_links
				int camp_launch;
				int camp_id;
				java.util.Date launch_time,timenow;				
				rs=stmt.executeQuery("select camp_launch,launch_time,camp_id from mail_campaign");
				while(rs.next()){
					camp_launch=rs.getInt("camp_launch");
					launch_time=rs.getDate("launch_time");
					camp_id=rs.getInt("camp_id");
					timenow=new java.util.Date();					
					if(camp_launch==1){
						if(timenow.after(launch_time)){
							Statement stmt2=con.createStatement();
							ResultSet rs2=stmt2.executeQuery("select * from camp_report where camp_id="+camp_id);
							if(rs2.next());
							else{
								rs2.close();
								String html_data="";
								//make entries in camp_report & camp_links
								
								rs2 = stmt2.executeQuery("select html_data from mail_campaign where camp_id="+camp_id);
								if(rs2.next()){
									html_data=rs2.getString("html_data");
									
								}   
								rs2.close();
								int links=0;
								int ptr=0;
								while(html_data.indexOf("href=\"http://",ptr)!=-1){
									ptr=html_data.indexOf("href=\"http://",ptr);
									if(ptr!=-1) links++;
									ptr+=7;		
								}								
								stmt2.executeUpdate("insert into camp_report values("+camp_id+",0,0,"+links+",0)");
								String urls[]=new String[links];
								ptr=0;
								int end;
								for(int i=0;i<links;i++){
									ptr=html_data.indexOf("href=\"http://",ptr);
									end=html_data.indexOf("\"",ptr+7);
									//end--;
									urls[i]=html_data.substring(ptr+6,end);
									ptr+=12;	
								}
								for(int i=0;i<links;i++){
									stmt2.executeUpdate("insert into camp_links values("+i+","+camp_id+",'"+urls[i]+"',0)");
								}
								//send mails by replacement & image addition
								String from_name="",from_email="",mail_subject="";
								int unsubs=0;
								rs2 = stmt2.executeQuery("select html_data,unsubs_reqd,from_name,from_email,mail_subject from mail_campaign where camp_id="+camp_id);
								if(rs2.next()){
									html_data=rs2.getString("html_data");
									from_name=rs2.getString("from_name");
									from_email=rs2.getString("from_email");
									mail_subject=rs2.getString("mail_subject");
									unsubs=rs2.getInt("unsubs_reqd");
								}
								rs2.close();
								StringBuffer newhtml=new StringBuffer(html_data);
								ptr=0;								
								for(int i=0;i<links;i++){
									ptr=newhtml.toString().indexOf("href=\"http://",ptr);
									ptr++;
									//end=newhtml.indexOf(">",ptr);
									//end--;									
									newhtml.replace(ptr+5,ptr+12,SERVER_NAME+"/UpdateLinkCount?link_no="+i+"&camp_id="+camp_id+"&link=");
									ptr+=12;	
								}						
								//newhtml.append("<img src='>"); 
								
								//send mails  
                                rs2=stmt2.executeQuery("Select list_id from camp_list where camp_id="+camp_id);
								while(rs2.next()){
									int list_id=rs2.getInt("list_id");
									Statement stmt3=con.createStatement();
									ResultSet rs3=stmt3.executeQuery("select * from list_member where list_id="+list_id+" AND member_unsub=0");
									while(rs3.next()){
										String name=rs3.getString("member_name");
										String email=rs3.getString("member_email");
										message.setFrom(new InternetAddress(from_email, from_name == null ? "" : from_name));
										message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email,false));
										message.setSubject(mail_subject);        
										StringBuffer sbtemp=new StringBuffer(newhtml.toString());
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
   										int endbody=sbtemp.toString().indexOf("</body>");
   										if(endbody!=-1)	sbtemp=new StringBuffer((sbtemp.toString()).substring(0,endbody));	  										
										if(unsubs==1){
											sbtemp.append("<hr color=e0e0e0><table border=0 cellpadding=5 cellspacing=0 width=100%>");
											sbtemp.append("<tr><td><font face='arial,verdana' size=1>You are receiving this email because you requested to receive info and updates via email.");
                                            sbtemp.append("To unsubscribe, reply to this email with \"unsubscribe\" in the subject or simply click on the following link: <a href='" + SERVER_NAME + "/UnSubscribe?list_id=").append(list_id).append("&email=").append(email).append("&camp_id=").append(camp_id).append("'>Unsubscribe</a></td>");
											sbtemp.append("</tr></table>");
										}									
										sbtemp.append("<table border=0 cellpadding=5 cellspacing=0 width=100%>");
										sbtemp.append("<tr><td><font face='arial,verdana' size=1>"+
										//This message was sent by using MailBlaster 1.0 
										"<br>");
                                        sbtemp.append("<td align=right><img src='" + SERVER_NAME + "/UpdateReport/logo.gif?camp_id=").append(camp_id).append("&email=").append(email).append("' border=0></a></td>");
										sbtemp.append("</tr></table></body></html>");  	
   										message.setContent(sbtemp.toString(),"text/html");   
    									message.setSentDate(new java.util.Date());
    									try{
    										sendMessage();
    										stmt2.executeUpdate("Update camp_report set mail_sent=mail_sent+1 where camp_id="+camp_id);	
    										stmt2.executeUpdate("Insert into sent_mail_list values("+camp_id+",'"+email+"')");
    										//update mail_sent in camp_report
    										//stmt2.executeUpdate("");	
                                        }
    									catch(Exception e){
    										e.printStackTrace();
    									}
    								}
    							}
                                rs.close();
                            }
						}
					}
				}	    		
    			try {
		        	Thread.sleep(1000*60*10);
      			}
      			catch (InterruptedException ignored) { }
      		}
    	}
    	catch (Exception ignored) { 
    		ignored.printStackTrace(System.out);
    	}	
  	}
  	public void doGet(HttpServletRequest req, HttpServletResponse res)
                               	throws ServletException, IOException {    
  	}
  	public void destroy() {
  		if(con!=null) pool.returnConnection(con);
	    runner.stop();
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