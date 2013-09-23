import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Pankaj Batra
 * Date: Jan 22, 2008
 * Time: 5:51:13 PM
 */
public class Utils {

    public static boolean validateSession(HttpServletRequest req, HttpServletResponse res){
        HttpSession session=req.getSession(false);
		String aduser = (String)session.getAttribute("aduser");
		java.util.Date time_comp=new java.util.Date(System.currentTimeMillis()-20*60*1000);
		java.util.Date accessed=new java.util.Date(session.getLastAccessedTime());
		if(session==null||aduser==null||accessed.before(time_comp)){
			session.invalidate();
            res.setContentType("text/html");
            PrintWriter out;
            try {
                out = res.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            out.println("<H2>Your Session has expired </H2>");
			out.println("<a href='admin.htm' target='_top'>Click Here</a> To Re-Login");
			return false;
		}
        return true;
    }
}
