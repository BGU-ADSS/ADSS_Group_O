package ServiceLayer;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Item;
import BusinessLayer.Objects.Product;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

class ProductServiceTest {

    private static ProductService productService;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;


    @Before
    static void setUp() {
        // Initialize the service
        productFacade = new ProductFacade();
        discountFacade = new DiscountFacade();
        categoryFascade = new CategoryFascade(discountFacade,productFacade);
        productService = new ProductService(productFacade, discountFacade, categoryFascade);

        try {
            // Set up initial data
            productService.buildCategory("Electronics");
            productService.buildProduct("iPhone", "Apple", 5000, 10, 1, productService.getCategoryByName("Electronics"), "Test1 Location Description", 1, 2);
            productService.buildProduct("TV", "Samsung", 2000, 20, 1, productService.getCategoryByName("Electronics"), "Test2 Location Description", 1, 2);
            productService.buildItem(1, LocalDate.of(2025, 12, 31), true,0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void test_buildCategory_with_valid_name() {
        // Arrange
        String categoryName = "New Category";

        // Act
        String result = "";
        try {
            result = productService.buildCategory(categoryName);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Assert
        assertEquals("Category built successfully", result);
        try {
            Category category = productService.getCategoryByName(categoryName);
            assertNotNull(category);
            assertEquals(categoryName, category.getCategoryName());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void test_build_product_with_valid_inputs() {
        // Act
        String result = "";
        try {
            result = productService.buildProduct("Laptop", "Dell", 1000, 15, 5, productService.getCategoryByName("Electronics"), "High-end laptop", 1, 1);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Assert
        assertEquals("Product built successfully", result);
    }

    @Test
    void test_build_item_success() {
        // Arrange
        int productID = 1;
        LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        // Act
        String result = "";
        try {
            result = productService.buildItem(productID, expirationDate, true,0);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Assert
        assertEquals("Item built successfully", result);
    }


    @Test
    void testRemoveItemThrowsException() {
        // Arrange
        int itemID = 999;
        boolean inStore = true;

        // Act & Assert
        try {
            String res= productService.removeItem(itemID, inStore);
            System.out.println(res);
            assertEquals("item doesnt exist", res);
        }
catch (Exception e)
{
    System.out.println(e.getMessage());
}
        // Verify the exception message if needed
    }

    @Test
    void checkMinimumStock() {
        // Arrange
        int productID = 1;

        // Act
        try {
            String result = productService.checkMinimumStock(productID);
            // Assert
            assertEquals("false", result);
        }
catch (Exception e){System.out.println(e.getMessage());}

    }

    @Test
    void updateStock() {
        // Arrange
        int productID = 1;
        int newStock = 50;
        try {
            // Act
            String result = productService.updateStock(productID, newStock, newStock);

            // Assert
            assertEquals("Provided product doesn't have items yet", result);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
