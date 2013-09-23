import java.util.Properties;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Pankaj Batra
 * Date: Jan 22, 2008
 * Time: 6:29:21 PM
 */
public class AppProperties {

    private static final String CONFIG_FILE = "config.properties";

    private static final Properties props = new Properties();

    static
    {
        try {
            props.load(CustomConnectionPool.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getValue(String key, String defaultValue)
    {
        return props.getProperty(key, defaultValue);
    }
}
