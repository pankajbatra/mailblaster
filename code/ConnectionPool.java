import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConnectionPool{
	private final Hashtable connections;
	private int increment;
    private static final String DB_URL="jdbc:mysql://localhost:3306/mailblaster";
    private static final String DB_USER="mailblaster";
    private static final String DB_PASS="mbsp2007";

    //Properties props = new Properties();
	//static String propFileName="";
	public ConnectionPool(int intialConnections,int increment) throws SQLException,ClassNotFoundException,InstantiationException,IllegalAccessException{
		/*try{
			FileInputStream is = new FileInputStream(propFileName);
        	props.load(is);
    	}    	
        catch(Exception e){			
				e.printStackTrace();
		}*/	
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.increment=increment;
		connections=new Hashtable();
		for(int i=0;i<intialConnections;i++){
			connections.put(DriverManager.getConnection(DB_URL, DB_USER, DB_PASS),Boolean.FALSE);
		}
	}
	public Connection getConnection() throws SQLException{
		Connection con;
		Enumeration cons=connections.keys();
		synchronized (connections){
			while(cons.hasMoreElements()){
				con=(Connection)cons.nextElement();
				Boolean b=(Boolean)connections.get(con);
				if(b==Boolean.FALSE){
					try{
						con.setAutoCommit(true);
					}
					catch(SQLException e){
						con=DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
					}
					return con;
				}
			}
		}
		for(int i=0;i<increment;i++){
			connections.put(DriverManager.getConnection(DB_URL, DB_USER, DB_PASS),Boolean.FALSE);
		}
		return getConnection();
	}
	public void returnConnection(Connection returned){
		Connection con;
		Enumeration cons=connections.keys();
		while(cons.hasMoreElements()){
			con=(Connection)cons.nextElement();
			if(con==returned){
				connections.put(con,Boolean.FALSE);
				break;
			}
		}
	}
}