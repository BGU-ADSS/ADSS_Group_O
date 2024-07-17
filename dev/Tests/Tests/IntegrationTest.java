package Tests.Tests;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;

import BusinessLayer.Fascades.*;
import BusinessLayer.Objects.Category;
import ServiceLayer.*;
import org.junit.Before;
import org.junit.Test;

import DTOs.Role;
import DTOs.ShiftTime;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    private static ServiceFactory sf ;
    private static ProductService productService;

    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;
    private static Connection connection;
    private static Category c;


    @Before
    public void setUpTestWithStoreKeeper() throws Exception {
        productFacade = new ProductFacade();
        discountFacade = new DiscountFacade();
        categoryFascade = new CategoryFascade(discountFacade, productFacade);
        productService = new ProductService(productFacade, discountFacade, categoryFascade);
        categoryFascade.deleteData();
        discountFacade.deleteData();
        productFacade.deleteData();
        
        String _connectionString = "jdbc:sqlite:dev\\StockData.db";
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection
            connection = DriverManager.getConnection(_connectionString);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        sf= new ServiceFactory(false);
        sf.addStore(0, "arraba", "arraba elbatof st");
        sf.addEmployee("1", "abo el ghanem", "123", 1000, new Role[]{Role.Storekeeper}, LocalDate.now(), LocalDate.now().plusYears(1), 0);
        sf.setShift(LocalDate.now(), ShiftTime.Day, "1", Role.Storekeeper);
        productService.buildCategory("Sweets");
        productService.buildCategory("Drinks");
        productService.buildCategory("Electronics");
        c = productService.getCategoryByName("Electronics");
        productService.buildProduct("Laptop","Dell",
                4000,15,0,
                c,
                "Near the window", 1 ,1,0);
        productService.buildProduct("Cake","Someone",
                30,3,0,
                productService.getCategoryByName("Sweets"),
                "At sweets section", 2 ,2,0);
        productService.buildItem(1,
                LocalDate.of(2025,3,12),false,0);
    }

    @Test
    public void test_use_case_of_storekeeper_adding_category() throws Exception {
        String query = "SELECT * FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 2);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Drinks", rs.getString("categoryName"));
                assertEquals(-1, rs.getInt("fatherCategoryID"));
            }
        } catch (Exception e) {

        }
    }
    @Test
    public void test_use_case_of_storekeeper_adding_product() throws Exception {
        String query = "SELECT * FROM Product WHERE MKT = ? AND storeId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 2);
            pstmt.setInt(2,0);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(1, rs.getInt("categoryID"));
                assertEquals("Laptop", rs.getString("categoryName"));
                assertEquals(0, rs.getInt("storageQuantity"));
                assertEquals(0, rs.getInt("storeQuantity"));
                assertEquals(4000, rs.getInt("priceBeforeDiscount"));
                assertEquals(4000, rs.getInt("priceAfterDiscount"));
                assertEquals(15, rs.getInt("size"));
                assertEquals(0, rs.getInt("minimumQuantity"));
            }
        } catch (Exception e) {

        }
    }
    @Test
    public void test_use_case_of_storekeeper_adding_item() throws Exception {
        String query = "SELECT * FROM Item WHERE itemID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Laptop", rs.getString("categoryName"));
                assertEquals(1, rs.getInt("prodID"));
                assertEquals("GOOD", rs.getInt("itemCondition"));
                assertEquals(-1, rs.getInt("purchaseID"));
            }
        } catch (Exception e) {

        }
    }



}
