package DatabaseTests;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Product;
import DataAccessLayer.Categories.CategoryDAO;
import DataAccessLayer.ReportProduct.ProductDAO;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;


class ProductFacadeTest {
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
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void removeItem() throws Exception{
        productFacade.buildProduct("Laptop", "Dell",4000,15,0,categoryFascade.getCategoryByName("Electronics"),"near the door",
                4,4);
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        Date ex=Date.valueOf(expirationDate);
        productFacade.buildItem(4,expirationDate,false);
        productFacade.removeItem(3,false);
        // Verify the item is no longer in the database using an SQL query
        String query = "SELECT * FROM Item WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 3);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertFalse(rs.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }

    @Test
    void buildProduct() throws Exception{
        // Build a product using the facade
        productFacade.buildProduct("Laptop", "Dell",4000,15,0,categoryFascade.getCategoryByName("Electronics"),"near the door",
                1,1);

        // Verify the product is in the database using an SQL query
        String query = "SELECT * FROM Product WHERE MKT = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("MKT"));
                assertEquals("Laptop", rs.getString("productName"));
                assertEquals("Dell", rs.getString("companyManufacturer"));
                assertEquals(1, rs.getInt("categoryID"));
                assertEquals(4000, rs.getInt("priceBeforeDiscount"));
                assertEquals(4000, rs.getInt("priceAfterDiscount"));
                assertEquals(15, rs.getInt("size"));
                assertEquals(0, rs.getInt("minimumQuantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }

    @Test
    void buildItem() throws Exception{
        productFacade.buildProduct("Laptop", "Dell",4000,15,0,categoryFascade.getCategoryByName("Electronics"),"near the door",
                2,2);
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        Date ex=Date.valueOf(expirationDate);
        productFacade.buildItem(1,expirationDate,false);
        // Verify the product is in the database using an SQL query
        String query = "SELECT * FROM Item WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("itemID"));
                assertEquals(productFacade.getProductIDByName("Laptop"), rs.getInt("productID"));
                assertEquals(ex, rs.getDate("expirationDate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }
    @Test
    void addToStore() throws Exception{
        productFacade.buildProduct("Smartphone", "Apple",4000,15,0,categoryFascade.getCategoryByName("Electronics"),"near the door",
                3,3);
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        Date ex=Date.valueOf(expirationDate);
        productFacade.buildItem(3,expirationDate,false);
        productFacade.addToStore(2);
        // Verify the product is in the database using an SQL query
        String query = "SELECT * FROM Product WHERE MKT = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 3);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(3, rs.getInt("MKT"));
//                assertEquals(productFacade.getProductIDByName("Smartphone"), rs.getInt("productID"));
                assertEquals(1,rs.getInt("storeQuantity"));

            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }
}