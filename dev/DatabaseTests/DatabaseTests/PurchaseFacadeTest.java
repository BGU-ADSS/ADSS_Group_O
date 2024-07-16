package DatabaseTests;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.PurchaseFacade;
import BusinessLayer.Objects.Purchase;
import DataAccessLayer.Categories.CategoryDAO;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;

class PurchaseFacadeTest {
    private static Connection connection;
    private static PurchaseFacade purchaseFacade;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;
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
                stmt.execute("DELETE FROM Purchase");
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
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }


        discountFacade=new DiscountFacade();
        productFacade=new ProductFacade();
        purchaseFacade = new PurchaseFacade(productFacade, discountFacade);
        categoryFascade=new CategoryFascade(discountFacade,productFacade);
        purchaseFacade.deleteData();


        // Clear the categories table before each test
//        try {
//            purchaseFacade.buildPurchase(LocalDate.of(1,1,2023),1);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }


    }

    @Before
    void setupEach() throws SQLException
    {
        productFacade.deleteData();
        purchaseFacade.deleteData();
        discountFacade.deleteData();
        categoryFascade.deleteData();
    }

    @Test
    void buildPurchase() throws Exception {
        categoryFascade.buildCategory("Electronics");

        productFacade.buildProduct("Laptop", "Dell",4000,15,0,categoryFascade.getCategoryByName("Electronics"),"near the door",
                2,2);
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        Date ex=Date.valueOf(expirationDate);
        productFacade.buildItem(1,expirationDate,false);


        purchaseFacade.buildPurchase(LocalDate.of(2023,1,1),1);
        Purchase purchase=purchaseFacade.getPurchase(1);
        purchaseFacade.addItem(1,1);
        String query = "SELECT * FROM Purchase WHERE purchaseID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, 1);
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(purchase.getPurchaseID(), rs.getInt("purchaseID"));
                assertEquals(productFacade.getProducts().get(1).getPriceAfterDiscount(), rs.getInt("total"));
                assertEquals(Date.valueOf(purchase.getPurchaseDate()),rs.getDate("purchaseDate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during the test.");
        }
    }
    @After
    public static void delete() throws SQLException {
        productFacade.deleteData();
        purchaseFacade.deleteData();
        categoryFascade.deleteData();
        discountFacade.deleteData();
    }
}