package ServiceLayer;

import BusinessLayer.Objects.Category;
import BusinessLayer.Objects.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.*;

import org.junit.Assert;
class PurchaseServiceTest {
    private  PurchaseService purchaseService;
    private ProductService productService = new ProductService();
    private DiscountService discountService = new DiscountService();

    @BeforeEach
     void setUp() {

        // Initialize services with shared facades
        productService = new ProductService();
        discountService = new DiscountService();
        purchaseService = new PurchaseService(productService.getProductFascade(), discountService.getDiscountFacade());

        try {
            // Set up initial data
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse("03/12/2024", dateFormat);
            purchaseService.buildPurchase(date, 1, "omry");
            LocalDate date2 = LocalDate.parse("04/12/2024", dateFormat);
            purchaseService.buildPurchase(date2, 1, "omry");
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
        productService.buildItem(1, date);
        productService.buildItem(2, date);

        purchaseService.addItem(1, 1);
        purchaseService.addItem(1, 2);
        String res = purchaseService.calculateTotal(1);

        assertEquals("20.0", res);
    }


    @Test
    void addItem() throws Exception {
        productService = new ProductService();
        productService.buildCategory("Cola");
        productService.buildProduct("cola zero", "adam", 10, 2,
                5, productService.getCategoryByName("Cola"),
                "Found in the Top", 3, 7);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse("12/12/2025", dateFormat);
        productService.buildItem(1, date);
        String res = purchaseService.addItem(1, 1);

        assertEquals("added item", res);
    }
}
