import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * 在哪里调用打印方法的, Class 就是哪个
 * Logger 就是真正打印日志的 Logger
 *
 * @author someexp
 * @date 2021/6/5
 */
public class App {

    public static void main(String[] args) {
        Marker marker = MarkerManager.getMarker("CLASS");
        Child child = new Child();

        System.out.println("------- Parent Logger ----------");
        child.log(null);
        child.log(marker);
        child.logFromChild(null);
        child.logFromChild(marker);
        child.parentLog(null);
        child.parentLog(marker);

        child.setLogger(child.childLogger);

        System.out.println("------- Parent Logger set to Child Logger ----------");
        child.log(null);
        child.log(marker);
        child.logFromChild(null);
        child.logFromChild(marker);
    }
}
