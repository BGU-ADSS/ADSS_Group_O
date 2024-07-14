package BusinessLayer.Fascades;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;



class ReportFascadeTest {
    private static Connection connection;
    private static CategoryFascade categoryFascade;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static ReportFascade reportFascade;
    @Before
    public static void setup() throws SQLException {

        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:" + path+ "\\StockData.db";
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            connection = DriverManager.getConnection(_connectionString);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Category");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Item");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Product");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Location");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Report");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM ReportProduct");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM ItemConditionReport");
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        discountFacade=new DiscountFacade();
        productFacade=new ProductFacade();
        categoryFascade = new CategoryFascade(discountFacade, productFacade);
        reportFascade = new ReportFascade(productFacade);

    }
    @Test
    void buildReport() {
        try {
            categoryFascade.buildCategory("Electronics");
            productFacade.buildProduct("Laptop","Dell",4000,15,0,
                    categoryFascade.getCategoryByName("Electronics"),"Near the window",1,1);
            productFacade.buildItem(1, LocalDate.of(2025,1,1),true);
            reportFascade.buildReport(categoryFascade.getCategoryByName("Electronics"));
            String query = "SELECT * FROM Report WHERE reportID = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, 0);
                try (ResultSet rs = pstmt.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(0, rs.getInt("reportID"));
                    assertEquals("Report ID: 0\n" +
                            "Date of report: 2024-07-05\n" +
                            "In Store: Item Information: ID = 1, Item Condition: GOOD\n" +
                            "The required product: Laptop\n", rs.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception occurred during the test.");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @After
    public static void deleteData() throws SQLException {
        productFacade.deleteData();
        discountFacade.deleteData();
        reportFascade.deleteData();
        categoryFascade.deleteData();
    }
}