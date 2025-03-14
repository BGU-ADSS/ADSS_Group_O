package DatabaseTests.DatabaseTests;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Category;
import DataAccessLayer.Categories.CategoryDAO;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class CategoryFascadeTest {
    private static Connection connection;
    private static CategoryFascade categoryFascade;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;


    @Before
    public static void setupFacade() throws SQLException {

        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:" + path + "\\StockData.db";
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

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Category");
            stmt.execute("DELETE FROM CategorySubs");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        discountFacade = new DiscountFacade();
        productFacade = new ProductFacade();
        categoryFascade = new CategoryFascade(discountFacade, productFacade);
        // Clear the categories table before each test
    }




    @Before
    public void setup() throws SQLException {
        categoryFascade.deleteData();
    }


    @Test
    public void testAddCategory() throws Exception {

        categoryFascade.buildCategory("Electronics");

        // Verify the category is in the database using an SQL query
        String query = "SELECT * FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(categoryFascade.getCategoryByName("Electronics").getCategoryID(), rs.getInt("categoryID"));
                assertEquals("Electronics", rs.getString("categoryName"));
                assertEquals(-1, rs.getInt("fatherCategoryID"));
            }
        } catch (Exception e) {

        }
    }


    @Test
    public void testFatherAddCategory() throws Exception {
        categoryFascade.buildCategory("Electronics");
        categoryFascade.buildCategory("kitchen", categoryFascade.getCategoryByName("Electronics"));
        // Verify the category is in the database using an SQL query
        String query = "SELECT * FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
//            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(2, rs.getInt("categoryID"));
                assertEquals("kitchen", rs.getString("categoryName"));
                assertEquals(1, rs.getInt("fatherCategoryID"));
            }
        } catch (Exception e) {

        }
    }

    @Test
    public void testSetSubAddCategory() throws Exception {
        categoryFascade.buildCategory("maxVerstappen");
        List<Category> sub = new LinkedList<>();
        sub.add(categoryFascade.getCategoryByName("maxVerstappen"));
        categoryFascade.buildCategory("F1", sub);
        // Verify the category is in the database using an SQL query
        String query = "SELECT * FROM Category WHERE categoryID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(categoryFascade.getCategoryByName("maxVerstappen").getCategoryID(), rs.getInt("categoryID"));
                assertEquals("maxVerstappen", rs.getString("categoryName"));
                assertEquals(categoryFascade.getCategoryByName("F1").getCategoryID(), rs.getInt("fatherCategoryID"));
            }
        } catch (Exception e) {

        }
    }


    @After
    public static void tearDown() throws SQLException {
        // Establish the connection


        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}