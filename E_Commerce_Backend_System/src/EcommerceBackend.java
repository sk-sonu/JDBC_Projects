import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class EcommerceBackend {
    public static void main(String[] args) throws SQLException {


        Scanner sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection connection = null;
        try {
             connection = DatabaseConnection.getConnection();
            System.out.println("Database Connected Successfully....");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }
}
