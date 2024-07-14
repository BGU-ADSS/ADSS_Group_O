package ServiceLayer;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Discount;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;

import java.time.LocalDate;


class DiscountServiceTest {

    private static DiscountService discountService;
    private static ProductFacade productFacade;
    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;


    @Before
    static void setUp() {
        productFacade = new ProductFacade();
        discountFacade = new DiscountFacade();
        categoryFascade = new CategoryFascade(discountFacade,productFacade);
        discountService = new DiscountService(discountFacade,productFacade,categoryFascade);
    }

    @Test
    void builedProductDiscount() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);

        // Act
        String result = discountService.builedProductDiscount(10.0, startDate, endDate);

        // Assert
        assertEquals("Product discount built", result);
    }

    @Test
    void builedCategoryDiscount() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(10);

        // Act
        String result = discountService.builedCategoryDiscount(15.0, startDate, endDate);

        // Assert
        assertEquals("Category discount built", result);
    }


    @Test
    void applyProductDiscount() {
        // Arrange
        int productID = 1;

        Discount discount = new Discount(1, 10.0, LocalDate.now(), LocalDate.now().plusDays(10));

        // Act
        String result = discountService.applyProductDiscount(productID, discount);

        // Assert
        assertEquals("Applied product discount", result);
    }


    @Test
    void applyCategoryDiscount() {
        // Arrange
        try {
            Category category = categoryFascade.getCategoryByName("TestCatogory");
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusDays(10);

            // Act
            String result = discountService.applyCategoryDiscount(category, 20.0, startDate, endDate);

            // Assert
            assertEquals("Applied category discount", result);
        }
        catch (Exception e){System.out.println(e.getMessage());}

    }

    @Test
    public void test_getDiscountByNonExistentProductID_throwsException() {
        // Arrange
        int nonExistentProductID = 999; // Assuming this ID does not exist

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            discountService.getDiscountByProductID(nonExistentProductID);
        });

        // Assert
        assertEquals("Discount is not found with given product ID", exception.getMessage());
    }

}
