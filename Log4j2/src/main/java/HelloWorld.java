import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author someexp
 * @date 2021/6/4
 */
public class HelloWorld {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.error("Hello, World!");
    }
}
