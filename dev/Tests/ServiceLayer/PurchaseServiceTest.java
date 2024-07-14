package ServiceLayer;

import BusinessLayer.Fascades.CategoryFascade;
import BusinessLayer.Fascades.DiscountFacade;
import BusinessLayer.Fascades.ProductFacade;
import BusinessLayer.Fascades.PurchaseFacade;
import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest {
    private  PurchaseService purchaseService;
    private static ProductFacade productFacade;
    private static PurchaseFacade purchaseFacade;

    private static DiscountFacade discountFacade;
    private static CategoryFascade categoryFascade;
    private ProductService productService;
    private DiscountService discountService;

    @BeforeEach
     void setUp() {

        // Initialize services with shared facades
        productFacade = new ProductFacade();

        discountFacade = new DiscountFacade();
        purchaseFacade=new PurchaseFacade(productFacade,discountFacade);
        categoryFascade = new CategoryFascade(discountFacade,productFacade);
        discountService = new DiscountService(discountFacade,productFacade,categoryFascade);
        productService = new ProductService(productFacade, discountFacade, categoryFascade);
        purchaseService = new PurchaseService(productFacade, discountFacade,purchaseFacade );

        try {
            // Set up initial data
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse("03/12/2024", dateFormat);
            purchaseService.buildPurchase(date, 1);
            LocalDate date2 = LocalDate.parse("04/12/2024", dateFormat);
            purchaseService.buildPurchase(date2, 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void calculateTotal() throws Exception {
        productService.buildCategory("Cola");
        productService.buildProduct("cola zero", "adam", 10, 2,
                5, productService.getCategoryByName("Cola"),
                "Found in the Top", 3, 7);
        productService.buildProduct("fanta", "adam", 10, 2,
                5, productService.getCategoryByName("Cola"),
                "Found in the Top", 3, 7);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("12/12/2025", dateFormat);
        productService.buildItem(1, date,true);
        productService.buildItem(2, date,true);

        purchaseService.addItem(1, 1);
        purchaseService.addItem(1, 2);
        String res = purchaseService.calculateTotal(1);

        assertEquals("20.0", res);
    }


    @Test
    void addItem() throws Exception {
        productService = new ProductService(productFacade, discountFacade, categoryFascade);
        productService.buildCategory("Cola");
        productService.buildProduct("cola zero", "adam", 10, 2,
                5, productService.getCategoryByName("Cola"),
                "Found in the Top", 3, 7);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("12/12/2025", dateFormat);
        productService.buildItem(1, date,true);
        String res = purchaseService.addItem(1, 1);

        assertEquals("added item", res);
    }
}
