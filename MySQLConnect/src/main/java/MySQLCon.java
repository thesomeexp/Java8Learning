import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author someexp
 * @date 2021/5/31
 */
public class MySQLCon {

    /**
     * 建表
     * create database sonoo;
     * use sonoo;
     * create table emp(id int(10),name varchar(40),age int(3));
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sonoo", "root", null);
            Statement statement = connection.createStatement();
//            ResultSet resultSet =

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
