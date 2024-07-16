package DatabaseTests;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import DataAccessLayer.Categories.CategoryDAO;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;



class DiscountFacadeTest {
    private static Connection connection;
    private static CategoryFascade categoryFascade;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
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
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DELETE FROM Discount");
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
        // Clear the categories table before each test
        try {
            categoryFascade.buildCategory("Electronics");
            discountFacade.deleteData();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
    @Test
    public void builedProductDiscount() throws Exception {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.of(2026, 12, 25);
        categoryFascade.buildCategory("Milk");
        productFacade.buildProduct("Milk 3 Percent", "Tnova", 10, 3, 0,
                categoryFascade.getCategoryByName("Milk"), "near the window", 2, 2,1);
        discountFacade.builedProductDiscount(93, start, end);

        String query = "SELECT * FROM Discount WHERE percent = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 93);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt("discountID"));
                    assertEquals(93, rs.getInt("percent"));
                    assertEquals(Date.valueOf(start), rs.getDate("startDate"));
                    assertEquals(Date.valueOf(end), rs.getDate("endDate"));
                } else {
                    fail("No data found for discountID 93.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }

}