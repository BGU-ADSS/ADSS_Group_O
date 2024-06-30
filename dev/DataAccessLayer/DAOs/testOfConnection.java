package DataAccessLayer.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import PresentationLayer.presentationController;

public class testOfConnection {
    private String url = "jdbc:sqlite:dev\\DataAccessLayer\\ADSS_DB_EMPLOYEE_MODULE.db";
    public testOfConnection(){}
    public void test(){
        Connection conn = null;
         try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void main(String[] args) {
        new testOfConnection().test();

    }
}
