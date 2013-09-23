import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

public class CustomConnectionPool {

    private static String dbUrl = null;
    private static String dbUser = null;
    private static String dbPass = null;
    private int increment = 0;

    private final Hashtable connections= new Hashtable();


    private static CustomConnectionPool pool = new CustomConnectionPool();

    private CustomConnectionPool(){
        try
        {
            dbUrl =  AppProperties.getValue("DB_URL", "jdbc:mysql://localhost:3306/mailblaster");
            dbUser =  AppProperties.getValue("DB_USER", "mailblaster");
            dbPass =  AppProperties.getValue("DB_PASS", "mbsp2007");
            String dbClass = AppProperties.getValue("DB_CLASS", "com.mysql.jdbc.Driver");
            Class.forName(dbClass).newInstance();
            increment= Integer.parseInt(AppProperties.getValue("CONN_POOL_INCR_SIZE","1"));
            int intialSize = Integer.parseInt(AppProperties.getValue("CONN_POOL_INITIAL_SIZE","5"));
            for(int i=0;i<intialSize;i++){
                connections.put(DriverManager.getConnection(dbUrl, dbUser, dbPass),Boolean.FALSE);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	public static Connection getConnection() throws SQLException{
		synchronized (pool.connections){
            Hashtable connections = pool.connections;
            Enumeration cons = connections.keys();
            while(cons.hasMoreElements()){
				Connection con =(Connection)cons.nextElement();
				Boolean b=(Boolean)connections.get(con);
				if(b==Boolean.FALSE){
                    if(con.isClosed())
                    {
                        connections.remove(con);
                        con=DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    }
                    connections.put(con,Boolean.TRUE);
                    return con;
				}
			}
		}
		for(int i=0;i<pool.increment;i++){
			pool.connections.put(DriverManager.getConnection(dbUrl, dbUser, dbPass),Boolean.FALSE);
		}
		return getConnection();
	}

    public static void returnConnection(Connection returned){
		Connection con;
        Hashtable connections = pool.connections;
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