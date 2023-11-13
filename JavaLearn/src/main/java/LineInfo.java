/**
 * @author lzr
 * @date 6/20/2022
 */

public class LineInfo {
    public static void main(String[] args) {
        System.out.println("This is " + getLineInfo());
    }

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }
}