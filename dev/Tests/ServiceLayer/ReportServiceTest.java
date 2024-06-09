package ServiceLayer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {
    private static ReportService reportService;
    private ProductService productService;
    @BeforeAll
    static void setUp() {
        reportService = new ReportService();
    }
    @Test
    void buildReport() throws Exception {
        productService = new ProductService();
        productService.buildCategory("Cola");
        String res = reportService.buildReport(productService.getCategoryByName("Cola"));
        assertEquals("No report of category", res);
    }



}