import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class SendTestMai3l {
	static Properties props = new Properties();
	
    static MimeMessage message;
  	static Session session;
  	
	public static void main(String args[])
	{	
		boolean debug = false;
        try{
        	props.put("mail.smtp.auth","true");
        	session = Session.getInstance(props, null);
        	session.setDebug(true);
        	
        	// construct the message
        	message = new MimeMessage(session);        
        	
			//String email="batra_pankaj11@yahoo.com";
			String email="pankajb@gmail.com";
			String name="Pankaj Batra";
			
			String plainText = "This is plain text";
			String htmlText = "<b>This is HTML text</b>";
			
			message.setHeader("Content-Type","multipart/related");       
			message.setFrom(new InternetAddress("mailblaster@sirfeducation.com", "Pankaj Batra"));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email,false));
	    	message.setSubject("This is testmail");    	
	    	
	    	 
	    	   	 
	        //Multipart multipart = new MimeMultipart("alternative");
	        Multipart multipart = new MimeMultipart("related");
	            
	        BodyPart messageBodyPart = new MimeBodyPart();	   		
	   		messageBodyPart.setContent(htmlText,"text/html");   			   			
	   		multipart.addBodyPart(messageBodyPart);
	   			
	   		messageBodyPart = new MimeBodyPart();
	   		messageBodyPart.setText(plainText);   			
	   		multipart.addBodyPart(messageBodyPart); 
	   		
	   		String attach_file="c:\\ws.zip"; 	
		   
        	messageBodyPart = new MimeBodyPart();
        	File file = new File(attach_file);  
        	DataSource source = new FileDataSource(file);
        	messageBodyPart.setDataHandler(new DataHandler(source));
        	messageBodyPart.setFileName(file.getName());
        	multipart.addBodyPart(messageBodyPart);
	          
			message.setContent(multipart); 	
	    	message.setSentDate(new java.util.Date());    	
	        sendMessage();      	             
	        
        }
        catch(Exception e)
        {
				e.printStackTrace();
		}
	}
    public static void sendMessage() throws Exception
    {
        message.saveChanges();
        Transport trans = session.getTransport("smtp");
        trans.connect("sirfeducation.com","mailblaster@sirfeducation.com","passwd");
        trans.sendMessage(message, message.getAllRecipients()); 
        trans.close();
    }
}
                
                

		
	