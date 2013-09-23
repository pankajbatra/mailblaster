import com.oreilly.servlet.MultipartRequest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;

public class UploadNewList extends HttpServlet{
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
		String CSVFile;
		MultipartRequest multi = new MultipartRequest(req, ".");		
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
		String temp_list_id=multi.getParameter("list_id");
		int list_id=Integer.parseInt(temp_list_id);
		try{
			Enumeration file = multi.getFileNames();
			if(file==null){
				out.println("<H2>The CSV File Field is left blank</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>to go back to previous page & Choose a CSV File to Upload");
				return;
			}
			String file1= (String)file.nextElement();
			String temp_CSVFile = multi.getFilesystemName(file1);
			int ex=temp_CSVFile.indexOf('.');
            String exten=temp_CSVFile.substring(ex).toLowerCase();
            if(exten.equals(".csv")) ;
			else{
				out.println("<H2>The File you have selected to upload is not a CSV file</H2>");
				out.println("<a href='javascript:history.go(-1)'>Click Here</a>  to go back to previous page & Again Choose Photo to Upload");
				return;
			}
			File csv = multi.getFile(file1);
			CSVFile=csv.getName();
			FileReader in = new FileReader(CSVFile);
			BufferedReader reader = new BufferedReader(in);			
			String columnNames = reader.readLine();
			int ptr=0;
        	int nextLimit;
        	String fieldName;
        	int nameLocation=0;
        	int emailLocation=0;
        	int colno=1;
        	while(ptr!=-1){
	        	nextLimit=columnNames.indexOf(",",ptr);
        		if(nextLimit!=-1){
	        		fieldName=columnNames.substring(ptr,nextLimit);
        			if((fieldName.compareToIgnoreCase("First Name")==0)||(fieldName.compareToIgnoreCase("Name")==0))nameLocation=colno;
        			if(fieldName.compareToIgnoreCase("Email")==0)emailLocation=colno;
        			if(emailLocation!=0&&nameLocation!=0) break;
        			ptr=nextLimit+1;
        			colno++;
        		}
        		else{
	        		fieldName=columnNames.substring(ptr);
        			if(fieldName.compareToIgnoreCase("First Name")==0)nameLocation=colno;
        			if(fieldName.compareToIgnoreCase("Email")==0)emailLocation=colno;
        			ptr=-1;        	
        		}
        	} 
        	if(emailLocation==0||nameLocation==0)throw new Exception("Invalid CSV File Format");       
        	String line;
        	//store all the lines in hashtable with key as email
        	Hashtable htable = new Hashtable();
        	while ((line = reader.readLine()) != null)
        	{	
            	// ignore if the line is empty.
            	if (line.trim().length() == 0)
	                continue;
            	int i;
            	colno=1;
            	ptr=0;
            	nextLimit=0;
            	while(colno<nameLocation&&ptr!=-1){
	            	nextLimit=line.indexOf(",",ptr);
        			if(nextLimit==-1)ptr=-1;
        			else {
	        			ptr=nextLimit+1;
        				colno++;
        			}
        		}  
        		i=line.indexOf(",",ptr);
        		String name;
        		if(i!=-1) name= line.substring(ptr, i);
        		else name = line.substring(ptr);
        		i=0;
            	colno=1;
            	ptr=0;
            	nextLimit=0;
            	while(colno<emailLocation&&ptr!=-1){
	            	nextLimit=line.indexOf(",",ptr);
        			if(nextLimit==-1)ptr=-1;
        			else {
	        			ptr=nextLimit+1;
        				colno++;
        			}
        		}  
        		i=line.indexOf(",",ptr);
        		String email;
        		if(i!=-1) email= line.substring(ptr, i);
        		else email = line.substring(ptr);	      	
            	//check if htable already has this email id, if yes do nothing.
            	if (htable.containsKey(email));
            	else htable.put(email,name);
        	}
        	// close streams
        	in.close();
        	reader.close();
        	con = pool.getConnection();
			Statement stmt = con.createStatement();							
        	Enumeration enum = htable.keys();
        	int no_item=0;
        	while (enum.hasMoreElements()){
        		String email = (String) enum.nextElement();
        		if(checkmail(email));
        		else continue;
        		String name= (String) htable.get(email);
        		ResultSet rs=stmt.executeQuery("select list_id from list_member where list_id="+list_id+" AND member_email='"+email+"'");
        		if(rs.next());
        		else {
        			stmt.executeUpdate("Insert into list_member values("+list_id+",'"+name+"','"+email+"',0)");
        			no_item++;
        		}
        	} 
        	java.util.Date date = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String last_modi= sdf.format(date);	
			stmt.executeUpdate("Update global_list set modi_date='"+last_modi+"' where list_id="+list_id);       	
			csv.delete();			
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
			out.println("<td width='100%'><font face='Verdana' color='#FFFFFF'><small><strong>Adding Items in List..</strong></small></font></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("</div><div align='right'>");
			out.println("<table border='1' width='97%' cellspacing='0' cellpadding='0' height='271' bordercolor='#COCOCO' bordercolorlight='#COCOCO'>");
			out.println("<tr>");
			out.println("<td width='100%' bgcolor='#F0F0FF' height='271' valign='top' align='center'>");
			out.println("<table border='0' width='80%' cellspacing='0' cellpadding='0' height='161' <tr>");
			out.println("<tr>");
			out.println("<td width='100%' height='27' valign='top' align='left' colspan='2'><div align='center'><center><p><font face='Verdana' color='#000080'><small><br><p><br><p>Successfully Added "+no_item+" Items to the List.<br><a href='AdminControlMiddle'><font face='Verdana' color='#6767B4'>Click here</font></a> to goto Main page</small></font></td>");
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
    	atsign=value.indexOf(" ");
        return atsign == -1;
    }
}
                
                

		
	